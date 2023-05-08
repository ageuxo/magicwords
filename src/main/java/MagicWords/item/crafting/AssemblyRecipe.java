package MagicWords.item.crafting;

import MagicWords.MagicWords;
import MagicWords.misc.SimpleMachineContainer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class AssemblyRecipe implements Recipe<SimpleMachineContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<StackIngredient> recipeIngredients;
    private final int energyCost;
    private static final Logger LOGGER = LogUtils.getLogger();
    protected static final ITagManager<Item> ITAG_MANAGER = ForgeRegistries.ITEMS.tags();

    public AssemblyRecipe(ResourceLocation id, ItemStack output, NonNullList<StackIngredient> recipeIngredients, int energyCost){
        this.id = id;
        this.output = output;
        this.recipeIngredients = recipeIngredients;
        this.energyCost = energyCost;
    }


    @Override
    public boolean matches(SimpleMachineContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }
        return recipeIngredients.stream().allMatch( (ing) -> {
            int keepCount = 0;
            for (int index : pContainer.getInputSlots() ) {
                ItemStack stack = pContainer.getItem(index);
                if (ing.getRecipeType() == StackIngredient.Type.STACK ){
                    if (ItemStack.matches(stack, ing.getStack()) || (stack.is(ing.getStack().getItem())) ) {
                        if (stack.getCount() >= ing.getCount() || (stack.getCount() + keepCount) >= ing.getCount()){
                            return true;
                        } else {
                            keepCount = keepCount + stack.getCount();
                        }
                    }
                } else if (stack.getTags().anyMatch(tagKey -> tagKey.equals(ing.getTagKey()))){
                    if (stack.getCount() >= ing.getCount() || (stack.getCount() + keepCount) >= ing.getCount()){
                        return true;
                    } else {
                        keepCount = keepCount + stack.getCount();
                    }
                }
            }
            return false;
        });
    }

    @Override
    public ItemStack assemble(SimpleMachineContainer p_44001_, RegistryAccess p_267165_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public NonNullList<StackIngredient> getStackIngredients(){
        return this.recipeIngredients;
    }

    public int getEnergyCost(){
        return this.energyCost;
    }

    public static class Type implements RecipeType<AssemblyRecipe>{
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = "assembly";
    }

    public static class Serializer implements RecipeSerializer<AssemblyRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(MagicWords.MODID, "assembly");

        @Override
        public AssemblyRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
//            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            ItemStack output = CraftingHelper.getItemStack(pSerializedRecipe.get("result").getAsJsonObject(), true);

            int energyCost = pSerializedRecipe.get("energy_cost").getAsInt();

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<StackIngredient> inputs = NonNullList.withSize(ingredients.size(), StackIngredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++){
                inputs.set(i, new StackIngredient(ingredients.get(i).getAsJsonObject().get("stack_ingredient").getAsJsonObject()) );
            }

            return new AssemblyRecipe(pRecipeId, output, inputs, energyCost);
        }

        @Override
        public @Nullable AssemblyRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<StackIngredient> inputs = NonNullList.withSize(pBuffer.readInt(), StackIngredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++){
                inputs.set(i, StackIngredient.Serializer.INSTANCE.parse(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            int energy = pBuffer.readInt();
            return new AssemblyRecipe(pRecipeId, output, inputs, energy);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AssemblyRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getStackIngredients().size());

            for (StackIngredient ingredient : pRecipe.getStackIngredients()){
                CraftingHelper.write(pBuffer, ingredient);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(RegistryAccess.EMPTY), false);
            pBuffer.writeInt(pRecipe.getEnergyCost());
        }
    }
}
