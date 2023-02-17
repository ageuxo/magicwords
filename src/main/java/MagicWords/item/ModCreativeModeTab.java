package MagicWords.item;


import MagicWords.MagicWords;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MagicWords.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    public static CreativeModeTab MAGICWORDS_TAB;

    @SubscribeEvent
    public static void buildCreativeTab(CreativeModeTabEvent.Register event) {
        MAGICWORDS_TAB = event.registerCreativeModeTab(new ResourceLocation(MagicWords.MODID, "magicwords_tab"),
                builder -> builder.title(Component.translatable("creativemodetab.magicwords_tab"))
                        .icon(() -> new ItemStack(ModItems.CHALK.get()))
        );
    }



}
