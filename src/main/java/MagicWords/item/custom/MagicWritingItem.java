package MagicWords.item.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.slf4j.Logger;

public class MagicWritingItem extends Item {
    public MagicWritingItem(Properties pProperties) {
        super(pProperties);
    }
    private static final Logger LOGGER = LogUtils.getLogger();

    public static String[] lang = {"magicwords.glyph_a", "magicwords.glyph_b", "magicwords.glyph_c", "magicwords.glyph_d", "magicwords.glyph_e"};



//    @Override
//    public Component getHighlightTip(ItemStack item, Component displayName) {
//        int dmg = item.getDamageValue();
//        if (dmg < lang.length) {
//            Component suffix = Component.literal(" [").withStyle(ChatFormatting.GOLD).append(Component.translatable(lang[dmg]).append("]"));
////            LOGGER.debug(String.valueOf(displayName.copy().append(suffix)));
//            return super.getHighlightTip(item, displayName.copy().append(suffix));
//        } else {
//            LOGGER.error("Out of bound Magic Writing Item");
//            return super.getHighlightTip(item, displayName);
//        }
//    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        int dmg = stack.getDamageValue();
        if (context.getPlayer().isSecondaryUseActive()){

            if (dmg + 1 < lang.length){
                stack.setDamageValue(dmg + 1);
            } else {
                stack.setDamageValue(0);
            }
        }
        if (context.getLevel().isClientSide()){
            LOGGER.debug("Client DMG: " + stack.getDamageValue());
        } else {
            LOGGER.debug("Server DMG: " + stack.getDamageValue());
        }
        return InteractionResult.FAIL;
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

        if (ItemStack.matches(oldStack, newStack) && oldStack.getDamageValue() == newStack.getDamageValue()) {
//            LOGGER.debug("same stacks");
            return false;
        } else {
//            LOGGER.debug("different stacks");
            return true;
        }
    }

}
