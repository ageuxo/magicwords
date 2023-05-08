package MagicWords.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

public class BarWidget extends AbstractWidget {
    private static final int OUTLINE_THICKNESS = 1;
    private final int outlineColour;
    private final int gradientFrom;
    private final int gradientTo;

    private int barOffset;

    public BarWidget(int pX, int pY) {
        this(pX, pY, 25, 100, 0xff8e8e8e, FastColor.ARGB32.color(255, 200, 0, 0), FastColor.ARGB32.color(255, 255, 0, 0));
    }

    public BarWidget(int pX, int pY, int pWidth, int pHeight, int outlineColour, int gradientFrom, int gradientTo) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.outlineColour = outlineColour;
        this.gradientFrom = gradientFrom;
        this.gradientTo = gradientTo;
    }

    @Override
    public void renderWidget(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderOutline(poseStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.outlineColour);
        fill(poseStack, this.getX()+OUTLINE_THICKNESS, this.getY()+OUTLINE_THICKNESS, this.getX()+this.getWidth()-OUTLINE_THICKNESS, this.getY()+getHeight()-OUTLINE_THICKNESS, 0xff202020);
        fillGradient(poseStack, this.getX()+ OUTLINE_THICKNESS, this.getY()+ OUTLINE_THICKNESS +barOffset,
                this.getX()+this.getWidth()- OUTLINE_THICKNESS, this.getY()+this.getHeight()- OUTLINE_THICKNESS,
                gradientFrom, gradientTo,
                0);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    public void updateWidget(int currentValue, int maxValue){
        int scaledValue = currentValue * this.getHeight() / maxValue;
        this.barOffset = this.height - scaledValue;
        this.setTooltip(Tooltip.create(Component.translatable("gui.magicwords.energy.tooltip.1", currentValue, maxValue)));
    }

    @Override
    protected @NotNull ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }
}
