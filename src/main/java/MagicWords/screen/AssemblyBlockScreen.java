package MagicWords.screen;

import MagicWords.MagicWords;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AssemblyBlockScreen extends AbstractContainerScreen<AssemblyBlockMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MagicWords.MODID,"textures/gui/container/assembly_block_gui.png");


    public AssemblyBlockScreen(AssemblyBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
    }


//    vars width/height = width/height of game window
//    vars imageWidth/imageHeight = width/height of Bg plate in image
    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = ((width-imageWidth)/2);
        int y = ((height-imageHeight)/2);

        blit(pPoseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressArrow(pPoseStack, x+78, y+40);
    }

    private void renderProgressArrow(PoseStack poseStack, int x, int y){
        if (menu.isCrafting()){
            blit(poseStack, x, y, 176, 0, menu.getScaledProgress(), 8, 256, 256);
        }
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
        renderMouseCoords(pPoseStack, pMouseX, pMouseY);
    }

    private void renderMouseCoords(PoseStack poseStack, int mouseX, int mouseY){
        drawString(poseStack, Minecraft.getInstance().font, mouseX + ":" + mouseY, width-50, height-10, 500);
        drawString(poseStack, Minecraft.getInstance().font, "Scaled: " + menu.getScaledProgress() + " Progress: " + menu.getCurrentProgress(), width-120, height-30, 500);
    }
}
