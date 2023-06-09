package MagicWords.client.gui;

import MagicWords.client.ClientConfig;
import MagicWords.item.custom.MagicWritingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class WritingHudOverlay {

    public static final IGuiOverlay HUD_WRITING = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight / 2;
        int xOffset = (x / 100) * ClientConfig.writingHudOverlayXOffset.get();
        int yOffset = (y / 100) * ClientConfig.writingHudOverlayYOffset.get();


        if (Minecraft.getInstance().player != null) {
            ItemStack mainHandItem = Minecraft.getInstance().player.getMainHandItem();
            if ( mainHandItem.getItem() instanceof MagicWritingItem && mainHandItem.getTag() != null && mainHandItem.getTag().get("glyph_selected") != null) {

                guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal(" [").withStyle(ChatFormatting.GOLD).append(Component.translatable( MagicWritingItem.lang[mainHandItem.getTag().getInt("glyph_selected")] ).append("]")), x+xOffset, y+yOffset, 128);
            }
        }

    });
}
