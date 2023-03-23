package MagicWords.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;


public class GlyphBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape[] SHAPES = {
            Block.box(0, 0, 15, 16, 16, 16),
            Block.box(0, 0, 0, 1, 16, 16),
            Block.box(0, 0, 0, 16, 16, 1),
            Block.box(15, 0, 0, 16, 16, 16) };


    public GlyphBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

//    @Override
//    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack){
//
//    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        int facingDirection = pState.getValue(FACING).getOpposite().get2DDataValue();

        return SHAPES[facingDirection];
    }

    private boolean canAttachTo(BlockGetter getter, BlockPos pos, Direction dir){
        BlockState state = getter.getBlockState(pos);
        return state.isFaceSturdy(getter, pos, dir);
    }

    @ParametersAreNonnullByDefault
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos){
        Direction dir = state.getValue(FACING);
        return this.canAttachTo(level, pos.relative(dir.getOpposite()), dir);
    }

    @ParametersAreNonnullByDefault
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos){
        if (facing.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ){
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }
}
