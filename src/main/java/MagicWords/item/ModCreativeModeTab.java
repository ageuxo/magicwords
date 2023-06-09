package MagicWords.item;


import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import MagicWords.client.ClientConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicWords.MODID);


    public static final RegistryObject<CreativeModeTab> MAGIC_WORDS_TAB = CREATIVE_TABS.register("magic_words_tab", ()-> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.MAGIC_CHALK.get()))
            .title(Component.translatable("creativemodetab.magicwords_tab"))
            .displayItems(ModCreativeModeTab::addContents)
            .build());

    protected static void addContents(CreativeModeTab.ItemDisplayParameters displayParams, CreativeModeTab.Output output){
        Collection<RegistryObject<Block>> blockRegistryArray = ModBlocks.BLOCKS.getEntries();
        for (RegistryObject<Block> i: blockRegistryArray ) {
            if ( !ClientConfig.creativeTabExclude.get().contains(i.getId().toString()) ) {
                output.accept(i.get());
            }
        }
        Collection<RegistryObject<Item>> itemRegistryArray = ModItems.ITEMS.getEntries();
        for (RegistryObject<Item> i: itemRegistryArray ) {
            if ( !ClientConfig.creativeTabExclude.get().contains(i.getId().toString()) ) {
                output.accept(i.get());
            }
        }
    }

    public static void register(IEventBus eventBus){
        CREATIVE_TABS.register(eventBus);
    }
}
