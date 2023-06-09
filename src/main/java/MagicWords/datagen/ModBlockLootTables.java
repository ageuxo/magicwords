package MagicWords.datagen;

import MagicWords.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.DUMMY_BLOCK.get());
        dropSelf(ModBlocks.CHALK_BLOCK.get());

        dropSelf(ModBlocks.DUSTY_LOG.get());
        dropSelf(ModBlocks.DUSTY_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_DUSTY_LOG.get());
        dropSelf(ModBlocks.STRIPPED_DUSTY_WOOD.get());
        dropSelf(ModBlocks.DUSTY_PLANKS.get());
        dropSelf(ModBlocks.DUSTY_SAPLING.get());

        dropSelf(ModBlocks.HORIZ_FACE.get());
        dropSelf(ModBlocks.CONNECTING_FACE_BLOCK.get());
        dropSelf(ModBlocks.ASSEMBLY_BLOCK.get());
        dropSelf(ModBlocks.FOCUS_BLOCK.get());

        add(ModBlocks.DUSTY_LEAVES.get(), (block -> createLeavesDrops(block, ModBlocks.DUSTY_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)));
        add(ModBlocks.GLYPH_A_BLOCK.get(), block -> noDrop());
        add(ModBlocks.GLYPH_B_BLOCK.get(), block -> noDrop());
        add(ModBlocks.GLYPH_C_BLOCK.get(), block -> noDrop());
        add(ModBlocks.GLYPH_D_BLOCK.get(), block -> noDrop());
        add(ModBlocks.GLYPH_E_BLOCK.get(), block -> noDrop());

    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
