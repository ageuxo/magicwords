package MagicWords.client;

import MagicWords.block.custom.GlyphBlock;
import MagicWords.block.entity.FocusBlockEntity;
import MagicWords.client.particle.MagicParticleOptions;
import MagicWords.networking.packet.FocusParticleS2CPacket;
import MagicWords.networking.packet.ItemStackSyncS2CPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ClientPacketHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void handlePacket(ItemStackSyncS2CPacket packet, Supplier<NetworkEvent.Context> supplier){
        if (Minecraft.getInstance().level.getBlockEntity(packet.getBlockPos()) instanceof FocusBlockEntity blockEntity){
            blockEntity.setHandler(packet.getItemStackHandler());
        }
    }

    public static void handlePacket(FocusParticleS2CPacket msg, Supplier<NetworkEvent.Context> supplier) {
        var lvl = Minecraft.getInstance().level;
        BlockPos focusPos = msg.getFocusPos();
        if (lvl != null){
            msg.getPosCollection().forEach((pos) -> {
                var particle = new MagicParticleOptions(focusPos.getX()+0.5d, focusPos.getY()+1.1d, focusPos.getZ()+0.5d, FastColor.ARGB32.color(255, 255, 0, 0));
                GlyphBlock.spawnParticlesForState(lvl, pos, particle, 3);
            }); // TODO target offset by block type
        }
    }
}
