package MagicWords.block.entity.renderer;

import MagicWords.block.entity.FocusBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class FocusBlockEntityRender implements BlockEntityRenderer<FocusBlockEntity> {
    private static final Quaternionf QUAT = new Quaternionf();

    public FocusBlockEntityRender(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(FocusBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getInventoryStack();
        pPoseStack.pushPose();
        float YOffset = 1.0f;
        float bobbing = (float) Math.sin((float) pBlockEntity.getLevel().getGameTime() /20);
        pPoseStack.translate(0.5f, YOffset+(bobbing*0.1f), 0.5f);
        pPoseStack.rotateAround(QUAT.rotateLocalY(0.01f), 0.0f, 0.0f, 0.0f);
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos blockPos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, blockPos);
        int skyLight = level.getBrightness(LightLayer.SKY, blockPos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
