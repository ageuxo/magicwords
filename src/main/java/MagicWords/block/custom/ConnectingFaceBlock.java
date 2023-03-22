package MagicWords.block.custom;

import MagicWords.block.state.ConnectingFaceShape;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

public class ConnectingFaceBlock extends ModDirectionalBlock{
    public static final EnumProperty<ConnectingFaceShape> CONNECTING = EnumProperty.create("connecting", ConnectingFaceShape.class);
    public static final Logger LOGGER = LogUtils.getLogger();

    public ConnectingFaceBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, CONNECTING);
    }

    private BlockState calculateState(Direction pDirection, LevelAccessor pLevel, BlockPos pCurrentPos) {
        ConnectingFaceShape connecting = ConnectingFaceShape.NONE;

        BlockPos relNorth = pCurrentPos.north();
        BlockPos relSouth = pCurrentPos.south();
        BlockPos relEast = pCurrentPos.east();
        BlockPos relWest = pCurrentPos.west();

        BlockPos[] relativePos = {
                relNorth,
                relSouth,
                relEast,
                relWest};
        int count = 0;
        boolean north = false, south = false, east = false, west = false;

        if (pDirection.getAxis().isHorizontal()){
            relativePos[0] = pCurrentPos.above();
            relativePos[1] = pCurrentPos.below();
            relativePos[2] = pCurrentPos.relative(pDirection.getClockWise());
            relativePos[3] = pCurrentPos.relative(pDirection.getCounterClockWise());
        }
//        if (pLevel.isClientSide()){ LOGGER.debug("Check did run");}
//        LOGGER.debug("clickedPos: "+ pCurrentPos);
//        LOGGER.debug("north:      "+ relativePos[0] + " instanceof: " +(pLevel.getBlockState(relNorth).getBlock() instanceof ConnectingFaceBlock)+" facing:"+(getFacing(pContext, relativePos[0])));
//        LOGGER.debug("east:       "+ relativePos[1] + " instanceof: " +(pLevel.getBlockState(relEast).getBlock() instanceof ConnectingFaceBlock)+" facing:"+(getFacing(pContext, relativePos[0])));
//        LOGGER.debug("south:      "+ relativePos[2] + " instanceof: " +(pLevel.getBlockState(relSouth).getBlock() instanceof ConnectingFaceBlock)+" facing:"+(getFacing(pContext, relativePos[0])));
//        LOGGER.debug("west:       "+ relativePos[3] + " instanceof: " +(pLevel.getBlockState(relWest).getBlock() instanceof ConnectingFaceBlock)+" facing:"+(getFacing(pContext, relativePos[0])));
        for (BlockPos i : relativePos){
            if (pLevel.getBlockState(i).getBlock() instanceof ConnectingFaceBlock && pLevel.getBlockState(i).getValue(FACING) == pDirection){
//                LOGGER.debug("i:"+ i );
                if (i == relativePos[0]){
                    north = true;
                    count++;
                }else if (i == relativePos[1]){
                    south = true;
                    count++;
                }else if (i == relativePos[2]){
                    east = true;
                    count++;
                }else if (i == relativePos[3]){
                    west = true;
                    count++;
                }else {
                    LOGGER.error("BIG BAD SPOOKY ERROR");
                }
//                LOGGER.debug("Count: "+count+". North:"+String.valueOf(north)+", south:"+String.valueOf(south)+", east:"+String.valueOf(east)+", west:"+String.valueOf(west));
            }
        }
        switch (count){
            case 0:
                break;
            case 1:
                if (north){
                    connecting = ConnectingFaceShape.NORTH;
                } else if (south) {
                    connecting = ConnectingFaceShape.SOUTH;
                } else if (east) {
                    connecting = ConnectingFaceShape.EAST;
                } else if (west) {
                    connecting = ConnectingFaceShape.WEST;
                } else {
                    LOGGER.error("Big error here! count is 1, but no direction is valid!");
                }
                break;
            case 2:
                if (north){
                    if (west){
                        connecting = ConnectingFaceShape.NORTH_WEST;
                    } else if (east) {
                        connecting = ConnectingFaceShape.NORTH_EAST;
                    } else if (south) {
                        connecting = ConnectingFaceShape.NORTH_SOUTH;
                    } else {
                        LOGGER.error("Big error here! count is 2, but no north direction is valid!");
                    }
                } else if (south) {
                    if (west){
                        connecting = ConnectingFaceShape.SOUTH_WEST;
                    } else if (east) {
                        connecting = ConnectingFaceShape.SOUTH_EAST;
                    } else {
                        LOGGER.error("Big error here! count is 2, but no south direction is valid!");
                    }
                } else if (east && west) {
                    connecting = ConnectingFaceShape.EAST_WEST;
                } else {
                    LOGGER.error("Big error here! count is 2, but no direction is valid!");
                }
                break;
            case 3:
                if (north){
                    if (west){
                        if (east){
                            connecting = ConnectingFaceShape.NORTH_EAST_WEST;
                        } else if (south) {
                            connecting = ConnectingFaceShape.NORTH_SOUTH_WEST;
                        } else {
                            LOGGER.error("Big error here! count is 3, but no north-west direction is valid!");
                        }
                    } else if (east) {
                        if (south){
                            connecting = ConnectingFaceShape.NORTH_EAST_SOUTH;
                        } else {
                            LOGGER.error("Big error here! count is 3, but no north-east direction is valid!");
                        }
                    } else {
                        LOGGER.error("Big error here! count is 3, but no north direction is valid!");
                    }
                } else if (south) {
                    if (west){
                        if (east){
                            connecting = ConnectingFaceShape.EAST_SOUTH_WEST;
                        } else {
                            LOGGER.error("Big error here! count is 3, but no south-west direction is valid!");
                        }
                    } else if (east) {
                        LOGGER.error("Big error here! count is 3, but no south-east direction is valid!");
                    } else {
                        LOGGER.error("Big error here! count is 3, but no south direction is valid!");
                    }
                } else {
                    LOGGER.error("Big error here! count is 3, but no direction is valid!");
                }
                break;
            case 4:
                connecting = ConnectingFaceShape.ALL;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + count);
        }


        return this.defaultBlockState().setValue(FACING, pDirection).setValue(CONNECTING, connecting);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return calculateState(pContext.getNearestLookingDirection().getOpposite(), pContext.getLevel(), pContext.getClickedPos());
    }


    @Override
    @ParametersAreNonnullByDefault
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
//        LOGGER.debug("pState: "+pState+" pNeighbourState: "+pNeighborState);
        return calculateState(pState.getValue(FACING), pLevel, pCurrentPos);
    }
}
