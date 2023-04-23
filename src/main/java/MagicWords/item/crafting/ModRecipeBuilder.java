package MagicWords.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ModRecipeBuilder {
    public static class AssemblyRecipeBuilder implements RecipeBuilder {
        private final Item result;
        private final int count;
        private final ArrayList<ItemStack> ingredients = new ArrayList<>();
        private TagKey<Item> tagKey;
        private int ingredientCount;

        public AssemblyRecipeBuilder(ItemLike outItem, int outCount){
            this.result = outItem.asItem();
            this.count = outCount;
        }

        public static AssemblyRecipeBuilder builder(ItemLike outItem, int outCount){
            return new AssemblyRecipeBuilder(outItem, outCount);
        }

        public AssemblyRecipeBuilder requires(ItemLike inItem) {
            return requires(inItem, 1);
        }

        public AssemblyRecipeBuilder requires(ItemLike inItem, int inCount) {
            return requires(new ItemStack(inItem, inCount));
        }

        public AssemblyRecipeBuilder requires(ItemStack item){
            this.ingredients.add(item);
            return this;
        }

        public AssemblyRecipeBuilder requires(TagKey<Item> tagKey) {
            return requires(tagKey, 1);
        }

        public AssemblyRecipeBuilder requires(TagKey<Item> tagKey, int ingCount){
            this.tagKey = tagKey;
            if (ingCount >= 1) {
                this.ingredientCount = ingCount;
            } else {
                throw new IllegalArgumentException("AssemblyRecipeBuilder requires param 'ingCount' >= 1 to be valid");
            }
            return this;
        }

        @Override
        public AssemblyRecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
            return null;
        }

        @Override
        public AssemblyRecipeBuilder group(@Nullable String pGroupName) {
            return null;
        }

        @Override
        public Item getResult() {
            return this.result;
        }

        public int getCount(){
            return this.count;
        }

        public ArrayList<ItemStack> getIngredients() {
            return this.ingredients;
        }

        public TagKey<Item> getTagKey() {
            return this.tagKey;
        }

        public int getIngredientCount() {
            return this.ingredientCount;
        }

        @Override
        public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {

            if (!getIngredients().isEmpty()){
                pFinishedRecipeConsumer.accept(new AssemblyRecipeBuilder.Result(pRecipeId, this.getResult(), this.getCount(), this.getIngredients()));
            } else if (getTagKey() != null){
                pFinishedRecipeConsumer.accept(new AssemblyRecipeBuilder.Result(pRecipeId, this.getResult(), this.getCount(), this.getTagKey(), this.getIngredientCount()));
            } else {
                throw new IllegalStateException("AssemblyRecipeBuilder with invalid result");
            }

        }

        public static class Result implements FinishedRecipe{
            private final ResourceLocation id;
            private final Item result;
            private final int count;
            private final ArrayList<ItemStack> ingredients;
            private final TagKey<Item> tagKey;
            private final int tagIngrCount;

            public Result(ResourceLocation id, Item result, int resultCount, TagKey<Item> tagKey, int tagIngrCount) {
                this(id, result, resultCount, null, tagKey, tagIngrCount);
            }

            public Result(ResourceLocation id, Item result, int resultCount, ArrayList<ItemStack> ingredients) {
                this(id, result, resultCount, ingredients, null, 0);
            }

            public Result(ResourceLocation id, Item result, int resultCount, ArrayList<ItemStack> ingredients, TagKey<Item> tagKey, int tagIngrCount) {
                this.id = id;
                this.result = result;
                this.count = resultCount;
                this.ingredients = ingredients;
                this.tagIngrCount = tagIngrCount;
                this.tagKey = tagKey;
            }

            @Override
            public void serializeRecipeData(JsonObject pJson) {
                JsonArray jsonArray = new JsonArray();

                if (this.ingredients != null && !this.ingredients.isEmpty()){
                    for (ItemStack itemStack : this.ingredients){
                        jsonArray.add(new StackIngredient(itemStack).toJson());
                    }
                } else if (this.tagKey != null && this.tagIngrCount > 1){
                    jsonArray.add(new StackIngredient(this.tagKey, this.tagIngrCount).toJson());
                }

                pJson.add("ingredients", jsonArray);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
                jsonObject.addProperty("count", this.count);

                pJson.add("result", jsonObject);
            }

            @Override
            public ResourceLocation getId() {
                return this.id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipes.ASSEMBLY_SERIALIZER.get();
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }

    }

}
