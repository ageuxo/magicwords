package MagicWords.datagen;

import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import MagicWords.misc.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModTagsProvider {


    public static class Blocks extends BlockTagsProvider{

        public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, MagicWords.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
            this.tag(ModTags.GLYPH_BLOCKS)
                    .add(ModBlocks.GLYPH_A_BLOCK.get(), ModBlocks.GLYPH_B_BLOCK.get(), ModBlocks.GLYPH_C_BLOCK.get(), ModBlocks.GLYPH_D_BLOCK.get(), ModBlocks.GLYPH_E_BLOCK.get());
        }
    }


    public static class Items extends ItemTagsProvider{

        public Items(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, providerCompletableFuture, lookupCompletableFuture, MagicWords.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
//            this.tag()
        }
    }
}
