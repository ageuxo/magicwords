package MagicWords.datagen;

import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import MagicWords.item.ModItems;
import MagicWords.item.crafting.ModRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        nineBlockStorageRecipes(consumer, RecipeCategory.BUILDING_BLOCKS, ModItems.CHALK.get(), RecipeCategory.MISC, ModBlocks.CHALK_BLOCK.get());

        //cookRecipes();

        ModRecipeBuilder.AssemblyRecipeBuilder.builder(ModItems.CHALK.get(), 16)
                .requires(ModBlocks.CHALK_BLOCK.get(), 2)
                .save(consumer, new ResourceLocation(MagicWords.MODID, "chalk_from_block_assembly"));

        ModRecipeBuilder.AssemblyRecipeBuilder.builder(ModBlocks.CONNECTING_FACE_BLOCK.get(), 4)
                .requires(new TagKey<>(ForgeRegistries.ITEMS.getRegistryKey(), ResourceLocation.tryParse("forge:ingots/iron")), 4)
                .save(consumer, new ResourceLocation(MagicWords.MODID, "connecting_face_block_from_iron_assembly"));

    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> p_249580_, RecipeCategory p_251203_, ItemLike p_251689_, RecipeCategory p_251376_, ItemLike p_248771_) {
        nineBlockStorageRecipes(p_249580_, p_251203_, p_251689_, p_251376_, p_248771_, getSimpleRecipeName(p_248771_), (String)null, getSimpleRecipeName(p_251689_), (String)null);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> p_250423_, RecipeCategory p_250083_, ItemLike p_250042_, RecipeCategory p_248977_, ItemLike p_251911_, String p_250475_, @Nullable String p_248641_, String p_252237_, @Nullable String p_250414_) {
        ShapelessRecipeBuilder.shapeless(p_250083_, p_250042_, 9).requires(p_251911_).group(p_250414_).unlockedBy(getHasName(p_251911_), has(p_251911_)).save(p_250423_, new ResourceLocation(MagicWords.MODID, p_252237_));
        ShapedRecipeBuilder.shaped(p_248977_, p_251911_).define('#', p_250042_).pattern("###").pattern("###").pattern("###").group(p_248641_).unlockedBy(getHasName(p_250042_), has(p_250042_)).save(p_250423_, new ResourceLocation(MagicWords.MODID, p_250475_));
    }
}
