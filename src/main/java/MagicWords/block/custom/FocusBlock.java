package MagicWords.block.custom;

import MagicWords.block.entity.FocusBlockEntity;
import MagicWords.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FocusBlock extends BaseEntityBlock {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);

    public FocusBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FocusBlockEntity(pPos, pState);
    }


    @NotNull
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getUsedItemHand() == InteractionHand.MAIN_HAND) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            var stack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            if (entity instanceof FocusBlockEntity focusBlockEntity) {
                if (stack.is(ModItems.MAGIC_CHALK.get())) {
                    //doMagic
                    focusBlockEntity.scanForGlyphs();
                    if (!focusBlockEntity.getLevel().isClientSide()){
//                        focusBlockEntity.sendMagicParticles();
                    }
                } else if (!stack.isEmpty()){
                    ItemStack leftOver = focusBlockEntity.insertItem(stack);
                    pPlayer.setItemInHand(InteractionHand.MAIN_HAND, leftOver);
                } else {
                    pPlayer.setItemInHand(InteractionHand.MAIN_HAND, focusBlockEntity.extractItem());
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()){
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof FocusBlockEntity){
                ((FocusBlockEntity) blockEntity).drops();
            }
        }
    }
}
