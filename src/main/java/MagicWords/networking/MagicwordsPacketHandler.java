package MagicWords.networking;

import MagicWords.MagicWords;
import MagicWords.networking.packet.FocusParticleS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MagicwordsPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MagicWords.MODID, "main"),
            ()->PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);


    public static void register(){
        int id = 0;
//        INSTANCE.registerMessage(id++, ItemStackSyncS2CPacket.class, ItemStackSyncS2CPacket::encode, ItemStackSyncS2CPacket::new, ItemStackSyncS2CPacket::handle);
        INSTANCE.registerMessage(id++, FocusParticleS2CPacket.class, FocusParticleS2CPacket::encode, FocusParticleS2CPacket::new, FocusParticleS2CPacket::handle);
    }

}
