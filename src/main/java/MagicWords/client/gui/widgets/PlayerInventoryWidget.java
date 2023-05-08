package MagicWords.client.gui.widgets;

import MagicWords.MagicWords;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
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
    public void renderWidget(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        blit(poseStack, this.getX(), this.getY(), 0, 0,  this.getWidth(), this.getHeight(), 176, 86);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }




}
