package MagicWords;


import MagicWords.block.ModBlocks;
import MagicWords.block.entity.ModBlockEntities;
import MagicWords.client.ClientConfig;
import MagicWords.client.ClientEvents;
import MagicWords.client.gui.AssemblyBlockScreen;
import MagicWords.item.ModCreativeModeTab;
import MagicWords.item.ModItems;
import MagicWords.item.crafting.ModRecipes;
import MagicWords.item.custom.MagicWritingItem;
import MagicWords.menus.ModMenuTypes;
import MagicWords.misc.ModParticles;
import MagicWords.networking.MagicwordsPacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MagicWords.MODID)
public class MagicWords {
    public static final String MODID = "magicwords";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MagicWords(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        MagicwordsPacketHandler.register();
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModRecipes.Types.register(modEventBus);
        ModParticles.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(ClientEvents.ClientModBusEvents::registerBlockColors);


        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.GENERAL_SPEC);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            event.enqueueWork(
                    ()-> MenuScreens.register(ModMenuTypes.ASSEMBLY_BLOCK_MENU.get(), AssemblyBlockScreen::new)
            );
        }
        @SubscribeEvent
        public static void onLoadFinished(FMLLoadCompleteEvent event){
            LOGGER.debug(ModCreativeModeTab.MAGIC_WORDS_TAB.get().toString());
        }


    }
    @Mod.EventBusSubscriber
    public static class CommonEvents{
        @SubscribeEvent
        public static void onTagsUpdated(TagsUpdatedEvent event){
            MagicWritingItem.updateGlyphList();
        }
    }

}
