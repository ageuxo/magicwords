package MagicWords.datagen;

import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;


public class ModBlockStateProvider extends BlockStateProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MagicWords.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.CHALK_BLOCK);
        blockWithItem(ModBlocks.DUMMY_BLOCK);
        logBlockWithItem(ModBlocks.DUMMY_LOG_BLOCK);

        blockWithItem(ModBlocks.DUSTY_PLANKS);
        blockWithItem(ModBlocks.DUSTY_LEAVES);

        logBlockWithItem(ModBlocks.DUSTY_LOG);
        axisBlock( (RotatedPillarBlock) ModBlocks.STRIPPED_DUSTY_LOG.get(), new ResourceLocation(MagicWords.MODID, "block/stripped_dusty_log"), new ResourceLocation(MagicWords.MODID, "block/dusty_log_top"));
        axisBlock( (RotatedPillarBlock) ModBlocks.DUSTY_WOOD.get(), blockTexture(ModBlocks.DUSTY_LOG.get()), blockTexture(ModBlocks.DUSTY_LOG.get()));
        axisBlock( (RotatedPillarBlock) ModBlocks.STRIPPED_DUSTY_WOOD.get(), new ResourceLocation(MagicWords.MODID, "block/stripped_dusty_log"), new ResourceLocation(MagicWords.MODID, "block/stripped_dusty_log"));

        simpleBlockItem(ModBlocks.DUSTY_WOOD.get(), models().withExistingParent("magicwords:dusty_wood", "minecraft:block/cube_column"));
        simpleBlockItem(ModBlocks.STRIPPED_DUSTY_LOG.get(), models().withExistingParent("magicwords:stripped_dusty_log", "minecraft:block/cube_column"));
        simpleBlockItem(ModBlocks.STRIPPED_DUSTY_WOOD.get(), models().withExistingParent("magicwords:stripped_dusty_wood", "minecraft:block/cube_column"));

        saplingBlock(ModBlocks.DUSTY_SAPLING);

        rotatableVariantBlock(ModBlocks.GLYPH_BLOCK, "block/glyph");


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void logBlockWithItem(RegistryObject<Block> blockRegistryObject){
        Block block = blockRegistryObject.get();
        ResourceLocation rl = blockTexture(block);
        logBlock( (RotatedPillarBlock) block);
        simpleBlockItem(block, models().cubeColumn(rl.getPath(), rl, new ResourceLocation(rl.getNamespace(), rl.getPath() + "_top")));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject){
        simpleBlock(blockRegistryObject.get(), models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void rotatableVariantBlock(RegistryObject<Block> blockRegistryObject, String model){
        VariantBlockStateBuilder builder = getVariantBuilder(blockRegistryObject.get());

        builder.partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(models().getExistingFile(new ResourceLocation("magicwords",model))).rotationY(0).addModel();
        builder.partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(models().getExistingFile(new ResourceLocation("magicwords",model))).rotationY(90).addModel();
        builder.partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(models().getExistingFile(new ResourceLocation("magicwords",model))).rotationY(180).addModel();
        builder.partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(models().getExistingFile(new ResourceLocation("magicwords",model))).rotationY(270).addModel();

    }
}
