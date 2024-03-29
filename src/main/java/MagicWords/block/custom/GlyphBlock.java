package MagicWords.block.custom;

import com.mojang.logging.LogUtils;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings({"deprecation"})
public class GlyphBlock extends HorizontalDirectionalBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty IS_FLAT = BooleanProperty.create("is_flat");
    private static final VoxelShape[] SHAPES = {
            Block.box(0, 0, 15, 16, 16, 16),
            Block.box(0, 0, 0, 1, 16, 16),
            Block.box(0, 0, 0, 16, 16, 1),
            Block.box(15, 0, 0, 16, 16, 16) };
    private static final VoxelShape SHAPE_FLAT = Block.box(0, 0, 0, 16, 1, 16);
    public static final int BLOCK_TINT = 0xff0000;


    public GlyphBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(IS_FLAT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, IS_FLAT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(IS_FLAT, !(pContext.getNearestLookingVerticalDirection()==Direction.DOWN));
        if (canSurvive(state, pContext.getLevel(), pContext.getClickedPos())){
            return state;
        } else {
            return null;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShapeForState(pState);
    }

    @ParametersAreNonnullByDefault
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos){
        if (state.getValue(IS_FLAT)){
            return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);
        } else {
            return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isFaceSturdy(level, pos.relative(state.getValue(FACING)), state.getValue(FACING));
        }
    }

    @ParametersAreNonnullByDefault
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos){
        if (!canSurvive(state, level, pos) ){
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    public static VoxelShape getShapeForState(BlockState state){
        int facingDirection = state.getValue(FACING).getOpposite().get2DDataValue();
        if (!state.getValue(IS_FLAT)) {
            return SHAPES[facingDirection];
        } else {
            return SHAPE_FLAT;
        }
    }

    public static void spawnParticlesForState(Level level, BlockPos glyphPos, ParticleOptions particleOptions, int spawnCount){
        AABB bb = getShapeForState(level.getBlockState(glyphPos)).bounds();
        for (int i = 0; i < spawnCount; i++) {
            double bbX = ThreadLocalRandom.current().nextDouble(bb.minX, bb.maxX);
            double bbY = ThreadLocalRandom.current().nextDouble(bb.minY, bb.maxY);
            double bbZ = ThreadLocalRandom.current().nextDouble(bb.minZ, bb.maxZ);
            level.addParticle(particleOptions, glyphPos.getX()+bbX, glyphPos.getY()+bbY, glyphPos.getZ()+bbZ, 1,1,1);
        }

    }



}
