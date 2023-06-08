package MagicWords.client;

import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import MagicWords.block.custom.GlyphBlock;
import MagicWords.block.entity.ModBlockEntities;
import MagicWords.block.entity.renderer.FocusBlockEntityRender;
import MagicWords.client.gui.WritingHudOverlay;
import MagicWords.client.particle.MagicParticle;
import MagicWords.misc.ModParticles;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

public class ClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Mod.EventBusSubscriber(modid = MagicWords.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("magicwritingitem_hud", WritingHudOverlay.HUD_WRITING);
        }

        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
            event.register((state, getter, pos, tint)-> (tint > 0 ? GlyphBlock.BLOCK_TINT : 0),
                    ModBlocks.GLYPH_A_BLOCK.get(), ModBlocks.GLYPH_B_BLOCK.get(), ModBlocks.GLYPH_C_BLOCK.get(), ModBlocks.GLYPH_D_BLOCK.get(), ModBlocks.GLYPH_E_BLOCK.get());

        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(ModBlockEntities.FOCUS_BLOCK_ENTITY.get(), FocusBlockEntityRender::new);
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event){
            event.registerSpriteSet(ModParticles.MAGIC_PARTICLE_TYPE.get(), MagicParticle.Provider::new);
        }
    }
}
