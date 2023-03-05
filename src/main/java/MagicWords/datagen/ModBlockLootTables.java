package MagicWords.datagen;

import MagicWords.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.DUMMY_BLOCK.get());
        dropSelf(ModBlocks.CHALK_BLOCK.get());
        dropSelf(ModBlocks.DUMMY_LOG_BLOCK.get());

        dropSelf(ModBlocks.DUSTY_LOG.get());
        dropSelf(ModBlocks.DUSTY_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_DUSTY_LOG.get());
        dropSelf(ModBlocks.STRIPPED_DUSTY_WOOD.get());
        dropSelf(ModBlocks.DUSTY_PLANKS.get());
        dropSelf(ModBlocks.DUSTY_SAPLING.get());

        add(ModBlocks.DUSTY_LEAVES.get(), (block -> createLeavesDrops(block, ModBlocks.DUSTY_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
