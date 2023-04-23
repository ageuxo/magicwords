package MagicWords.item.crafting;

import MagicWords.MagicWords;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MagicWords.MODID);

    public static final RegistryObject<RecipeSerializer<AssemblyRecipe>> ASSEMBLY_SERIALIZER = SERIALIZERS.register("assembly", ()-> AssemblyRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }

    public static class Types{
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MagicWords.MODID);

        public static final RegistryObject<RecipeType<AssemblyRecipe>> ASSEMBLY_RECIPE_TYPE = RECIPE_TYPES.register("assembly", ()-> AssemblyRecipe.Type.INSTANCE);

        public static void register(IEventBus eventBus){
            RECIPE_TYPES.register(eventBus);
        }
    }
}
