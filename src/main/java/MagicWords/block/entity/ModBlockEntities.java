package MagicWords.block.entity;

import MagicWords.MagicWords;
import MagicWords.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MagicWords.MODID);

    public static final RegistryObject<BlockEntityType<AssemblyBlockEntity>> ASSEMBLY_BLOCK_ENTITY = BLOCK_ENTITIES
            .register("assembly_block_entity", ()->BlockEntityType.Builder.of(AssemblyBlockEntity::new,
                                                    ModBlocks.ASSEMBLY_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
