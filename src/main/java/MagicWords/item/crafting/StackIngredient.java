package MagicWords.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Predicate;



public class StackIngredient extends AbstractIngredient implements Predicate<ItemStack> {
    public static final StackIngredient EMPTY = new StackIngredient(ItemStack.EMPTY);
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ItemStack stack;
    private final TagKey<Item> tagKey;
    private final int count;
    private final Type recipeType;
//    private final int tagIngrCount;

    public StackIngredient(JsonObject object) {
        ItemStack ingredientItem = null;
        TagKey<Item> tmpTag;
        if (!object.has("count") || object.get("count").getAsInt() < 1){
            throw new JsonParseException("StackIngredient needs a 'count' property of at least 1");
        }

        CompoundTag nbt = null;

        if (object.has("nbt")){
            nbt = CraftingHelper.getNBT(object.get("nbt"));
        }
        if (object.has("item") && !object.has("tag")){
            ingredientItem = new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(object.get("item").getAsString())), object.get("count").getAsInt(), nbt);
            tmpTag = null;
            this.recipeType = Type.STACK;
            if (ingredientItem != ItemStack.EMPTY && ingredientItem.getItem() != Items.AIR) {
//                this.stack = ingredientItem;
                count = ingredientItem.getCount();
            } else {
                throw new JsonParseException("StackIngredient has invalid 'item' property: "+ ingredientItem);
            }
        } else if (object.has("tag") && !object.has("item")){
            TagKey<Item> readKey = new TagKey<>(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation( object.get("tag").getAsString() ));
            this.recipeType = Type.TAG_KEY;
            if (!AssemblyRecipe.ITAG_MANAGER.getTag(readKey).isBound()) {
                tmpTag = readKey;
                count = object.get("count").getAsInt();
            } else {
                throw new JsonParseException("StackIngredient has invalid 'tag' property: "+ object.get("tag").getAsString());
            }
        } else if (!object.has("item") && !object.has("tag")){
            throw new JsonParseException("StackIngredient needs an 'item' or a 'tag' property");
        } else {
            throw new JsonParseException("StackIngredient needs EITHER an 'item' OR a 'tag' property, NEVER both!");
        }

        this.stack = ingredientItem;
        this.tagKey = tmpTag;
    }

    public StackIngredient(ItemStack itemStack) {
        this.stack = itemStack;
        this.count = itemStack.getCount();
        this.tagKey = null;
        this.recipeType = Type.STACK;
//        this.tagIngrCount = 0;
    }

    public StackIngredient(TagKey<Item> tagKey, int tagIngrCount){
        this.stack = null;
        this.recipeType = Type.TAG_KEY;
        this.count = tagIngrCount;
        this.tagKey = tagKey;
//        this.tagIngrCount = tagIngrCount;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public @NotNull IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject ret = new JsonObject();
        JsonObject ingInput;
        if (this.stack != null){
            ingInput = stackToJson(this.stack);
        } else if (this.tagKey != null){
            ingInput = tagToJson(this.getTagKey(), this.getCount());
        } else {
            throw new JsonParseException("Invalid StackIngredient: Stack and TagKey null!");
        }

        ret.add("stack_ingredient", ingInput);

        return ret;
    }

    public JsonObject stackToJson(ItemStack stack){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        jsonObject.addProperty("count", stack.getCount());
        if (stack.getTag() != null){
            jsonObject.addProperty("nbt", stack.getTag().toString());
        }
        return jsonObject;
    }

    public JsonObject tagToJson(TagKey<Item> tagKey, int count){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tag", tagKey.location().toString());
        jsonObject.addProperty("count", count);
        return jsonObject;
    }

    public ItemStack getStack(){
        return this.stack;
    }

    public int getCount(){
        return this.count;
    }

    public TagKey<Item> getTagKey(){
        return this.tagKey;
    }

    public Type getRecipeType(){
        return this.recipeType;
    }

    public enum Type implements StringRepresentable{
        STACK("stack"),
        TAG_KEY("tag_key");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

    }



    public static class Serializer implements IIngredientSerializer<StackIngredient>{
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull StackIngredient parse(FriendlyByteBuf buffer) {
            return new StackIngredient(buffer.readItem());
        }

        @Override
        public @NotNull StackIngredient parse(@NotNull JsonObject json) {
            return new StackIngredient(json);
        }

        @Override
        public void write(FriendlyByteBuf buffer, StackIngredient ingredient) {
            buffer.writeItemStack(ingredient.stack, false);
        }
    }

}
