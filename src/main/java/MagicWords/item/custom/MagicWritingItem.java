package MagicWords.item.custom;

import MagicWords.block.ModBlocks;
import MagicWords.block.custom.GlyphBlock;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class MagicWritingItem extends Item {
    public MagicWritingItem(Properties pProperties) {
        super(pProperties);
    }
    private static final Logger LOGGER = LogUtils.getLogger();

    public static String[] lang = {"magicwords.glyph_a", "magicwords.glyph_b", "magicwords.glyph_c", "magicwords.glyph_d", "magicwords.glyph_e"};


    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        int dmg = stack.getDamageValue();
        if (context.getPlayer().isSecondaryUseActive()){

            if (dmg + 1 < lang.length){
                stack.setDamageValue(dmg + 1);
            } else {
                stack.setDamageValue(0);
            }
        } else {

            boolean isClickedHorizontal = context.getClickedFace().getAxis().isHorizontal();
            BlockPos placingPos = context.getClickedPos().relative(context.getClickedFace());
            //Check for support underneath if vertical placement
            BlockState supportingState = !isClickedHorizontal && context.getClickedFace().getAxisDirection().getStep() < 0 ? context.getLevel().getBlockState(placingPos.below()) : context.getLevel().getBlockState(context.getClickedPos());

            BlockState replacingState = context.getLevel().getBlockState(placingPos);

            if (!isClickedHorizontal && context.getClickedFace().getAxisDirection().getStep() < 0) supportingState = context.getLevel().getBlockState(placingPos.below());

            if (supportingState.isFaceSturdy(context.getLevel(), context.getClickedPos(), context.getClickedFace()) && (replacingState.canBeReplaced() || replacingState.isAir()) ) {

                context.getLevel().setBlockAndUpdate(placingPos, ModBlocks.GLYPH_BLOCK.get().defaultBlockState()
                        .setValue(GlyphBlock.FACING, isClickedHorizontal ? context.getClickedFace() : context.getHorizontalDirection().getOpposite())
                        .setValue(GlyphBlock.IS_FLAT, !isClickedHorizontal));

            }
        }
        return InteractionResult.FAIL;
    }


}
