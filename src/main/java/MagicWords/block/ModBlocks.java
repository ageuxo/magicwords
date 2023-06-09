package MagicWords.block;

import MagicWords.MagicWords;
import MagicWords.block.custom.*;
import MagicWords.item.ModItems;
import MagicWords.worldgen.tree.DustyTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MagicWords.MODID);



    public static final RegistryObject<Block> DUMMY_BLOCK = registerBlock("dummy_block", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CHALK_BLOCK = registerBlock("chalk_block", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.SAND).strength(0.5f)));

    public static final RegistryObject<Block> DUSTY_LOG = registerBlock("dusty_log", ()-> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(2f)));
    public static final RegistryObject<Block> DUSTY_WOOD = registerBlock("dusty_wood", ()-> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(2f)));
    public static final RegistryObject<Block> STRIPPED_DUSTY_LOG = registerBlock("stripped_dusty_log", ()-> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(2f)));
    public static final RegistryObject<Block> STRIPPED_DUSTY_WOOD = registerBlock("stripped_dusty_wood", ()-> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(2f)));

    public static final RegistryObject<Block> DUSTY_PLANKS = registerBlock("dusty_planks", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2f)){
        @Override
        public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return true;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 5;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 20;
        }
    });
    public static final RegistryObject<Block> DUSTY_LEAVES = registerBlock("dusty_leaves", ()-> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(2f)){
        @Override
        public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return true;
        }
        @Override
        public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 30;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 60;
        }
    });

    public static final RegistryObject<Block> DUSTY_SAPLING = registerBlock("dusty_sapling", ()-> new SaplingBlock(new DustyTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).strength(2f)));

    public static final RegistryObject<Block> GLYPH_A_BLOCK = registerBlock("glyph_a", ()-> new GlyphBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).instabreak().noCollission().noOcclusion()));
    public static final RegistryObject<Block> GLYPH_B_BLOCK = registerBlock("glyph_b", ()-> new GlyphBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).instabreak().noCollission().noOcclusion()));
    public static final RegistryObject<Block> GLYPH_C_BLOCK = registerBlock("glyph_c", ()-> new GlyphBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).instabreak().noCollission().noOcclusion()));
    public static final RegistryObject<Block> GLYPH_D_BLOCK = registerBlock("glyph_d", ()-> new GlyphBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).instabreak().noCollission().noOcclusion()));
    public static final RegistryObject<Block> GLYPH_E_BLOCK = registerBlock("glyph_e", ()-> new GlyphBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).instabreak().noCollission().noOcclusion()));

    public static final RegistryObject<Block> HORIZ_FACE = registerBlock("horiz_face", ()-> new ModDirectionalBlock(BlockBehaviour.Properties.copy(Blocks.PISTON).strength(3f)));

    public static final RegistryObject<Block> CONNECTING_FACE_BLOCK = registerBlock("connecting_face_block", ()-> new ConnectingFaceBlock(BlockBehaviour.Properties.copy(Blocks.PISTON)));

    public static final RegistryObject<Block> ASSEMBLY_BLOCK = registerBlock("assembly_block", ()-> new AssemblyBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> FOCUS_BLOCK = registerBlock("focus_block", ()-> new FocusBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_ANDESITE).noOcclusion().strength(1f)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
