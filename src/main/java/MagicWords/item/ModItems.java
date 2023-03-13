package MagicWords.item;

import MagicWords.MagicWords;
import MagicWords.item.custom.MagicWritingItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MagicWords.MODID);

    public static final RegistryObject<Item> CHALK = ITEMS.register("chalk",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGIC_CHALK = ITEMS.register("magic_chalk",
            ()-> new MagicWritingItem(new Item.Properties()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
