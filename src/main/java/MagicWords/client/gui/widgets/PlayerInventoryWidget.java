package MagicWords.client.gui.widgets;

import MagicWords.MagicWords;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PlayerInventoryWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MagicWords.MODID, "textures/gui/widgets/inventory.png");
    private static final Component TITLE = Component.translatable("container.inventory");

    public PlayerInventoryWidget(int pX, int pY) {
        super(pX, pY, 176, 86, TITLE);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, TEXTURE);

        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, 0,  this.getWidth(), this.getHeight(), 176, 86);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }




}
