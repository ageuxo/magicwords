package MagicWords.datagen;

import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import MagicWords.block.custom.ConnectingFaceBlock;
import MagicWords.block.state.ConnectingFaceShape;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;


public class ModBlockStateProvider extends BlockStateProvider {
//    private static final Logger LOGGER = LogUtils.getLogger();

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

        directionalBlock(ModBlocks.HORIZ_FACE.get(), models().getExistingFile(new ResourceLocation("magicwords","block/horiz_face")));
        connectingBlock(ModBlocks.CONNECTING_FACE_BLOCK, "connecting_block");
        simpleBlockItem(ModBlocks.CONNECTING_FACE_BLOCK.get(), models().withExistingParent("magicwords:connecting_block", "magicwords:block/connecting/connecting_block"));


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

    private void connectingBlock(RegistryObject<Block> blockRegistryObject, String model){
        ModelFile modelBase = models().getExistingFile(new ResourceLocation("magicwords", model));
        VariantBlockStateBuilder builder = getVariantBuilder(blockRegistryObject.get());

        Function<BlockState, ModelFile> modelFunc = m -> {
            ConnectingFaceShape connect = m.getValue(ConnectingFaceBlock.CONNECTING);
            String prefix = "block/connecting/";
            String suffix;
            if (connect == ConnectingFaceShape.NONE){
                return modelBase;
            } else if (connect == ConnectingFaceShape.ALL){
                suffix = "_all";
            } else if (connect == ConnectingFaceShape.NORTH || connect == ConnectingFaceShape.EAST || connect == ConnectingFaceShape.SOUTH || connect == ConnectingFaceShape.WEST ) {
                suffix = "_single";
            } else if (connect == ConnectingFaceShape.NORTH_EAST || connect == ConnectingFaceShape.NORTH_WEST || connect == ConnectingFaceShape.SOUTH_EAST || connect == ConnectingFaceShape.SOUTH_WEST) {
                suffix = "_corner";
            } else if (connect == ConnectingFaceShape.EAST_WEST || connect == ConnectingFaceShape.NORTH_SOUTH){
                suffix = "_straight";
            } else{
                suffix = "_t";
            }


            String connectDir = (connect == ConnectingFaceShape.ALL) ? "" : ("_" + connect.getName() );
            return models()
                    .withExistingParent(modelBase.getLocation().toString()+suffix+ connectDir, new ResourceLocation("magicwords", model))
                    .texture("1", new ResourceLocation("magicwords", prefix+model+suffix+ connectDir)) ;
        };
        builder.forAllStates(state->{
            Direction dir = state.getValue(ConnectingFaceBlock.FACING);
            int xRotOffset = 0, yRotOffset = 0;
            int xRotation = (dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0);
            int yRotation = (dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot()+ 180) % 360);


            return ConfiguredModel.builder()
                    .modelFile(modelFunc.apply(state))
                    .rotationX((xRotation + xRotOffset)%360)
                    .rotationY((yRotation + yRotOffset)%360)
                    .build();
        });

    }

}
