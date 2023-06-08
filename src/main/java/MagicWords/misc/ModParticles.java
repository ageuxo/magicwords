package MagicWords.misc;

import MagicWords.MagicWords;
import MagicWords.client.particle.MagicParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MagicWords.MODID);

    public static final RegistryObject<ParticleType<MagicParticleOptions>> MAGIC_PARTICLE_TYPE = PARTICLE_TYPES
            .register("magic_particle_type", MagicParticleOptions.Type::new);

    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
