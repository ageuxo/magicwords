package MagicWords.client.gui;

import MagicWords.MagicWords;
import MagicWords.client.gui.widgets.BarWidget;
import MagicWords.client.gui.widgets.PlayerInventoryWidget;
import MagicWords.menus.AbstractMachineMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AssemblyBlockScreen extends AbstractMachineScreen<AbstractMachineMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MagicWords.MODID,"textures/gui/container/assembly_block_gui.png");
    private BarWidget barWidget;


    public AssemblyBlockScreen(AbstractMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableOnly(new PlayerInventoryWidget(leftPos, topPos+80));
        this.barWidget = new BarWidget(leftPos-40,topPos);
        this.addRenderableWidget(barWidget);
    }


//    vars width/height = width/height of game window
//    vars imageWidth/imageHeight = width/height of Bg plate in image
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = ((width-imageWidth)/2);
        int y = ((height-imageHeight)/2);

        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressArrow(guiGraphics, x+78, y+40);
    }

    private void renderProgressArrow(GuiGraphics poseStack, int x, int y){
        if (menu.isCrafting()){
            poseStack.blit(TEXTURE, x, y, 176, 0, menu.getScaledProgress(26), 8, 256, 256);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        barWidget.updateWidget(menu.getCurrentEnergy(), menu.getMaxEnergy());
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        renderMouseCoords(guiGraphics, pMouseX, pMouseY);
    }

    private void renderMouseCoords(GuiGraphics guiGraphics, int mouseX, int mouseY){
        guiGraphics.drawString( Minecraft.getInstance().font, mouseX + ":" + mouseY, width-50, height-10, 500);
        guiGraphics.drawString( Minecraft.getInstance().font, "Scaled: " + menu.getScaledProgress(26) + " Progress: " + menu.getCurrentProgress(), width-120, height-30, 500);
        guiGraphics.drawString( Minecraft.getInstance().font, "Scaled: " + menu.getScaledEnergy(barWidget.getHeight()) + " Energy: " + menu.getCurrentEnergy(), width-140, height-50, 500);
    }
}
