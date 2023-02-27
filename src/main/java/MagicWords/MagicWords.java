package MagicWords;


import MagicWords.block.ModBlocks;
import MagicWords.item.ModCreativeModeTab;
import MagicWords.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(MagicWords.MODID)
public class MagicWords {
    public static final String MODID = "magicwords";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MagicWords(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);


        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);


        modEventBus.addListener(this::addCreative);



    }

    private void commonSetup(final FMLCommonSetupEvent event){

    }

    private void addCreative(CreativeModeTabEvent.BuildContents event){
        if (event.getTab() == ModCreativeModeTab.MAGICWORDS_TAB){
            RegistryObject[] itemRegistryArray = ModItems.ITEMS.getEntries().toArray(new RegistryObject[0]);
            for (RegistryObject i: itemRegistryArray ) {
                event.accept(i);
            }
            RegistryObject[] blockRegistryArray = ModBlocks.BLOCKS.getEntries().toArray(new RegistryObject[0]);
            for (RegistryObject i: blockRegistryArray ) {
                event.accept(i);
            }
        }
    }



    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){

        }
        @SubscribeEvent
        public static void onLoadFinished(FMLLoadCompleteEvent event){

        }


    }

}
