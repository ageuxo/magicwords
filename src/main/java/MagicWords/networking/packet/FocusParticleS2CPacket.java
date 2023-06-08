package MagicWords.networking.packet;

import MagicWords.client.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class FocusParticleS2CPacket {
    private final Collection<BlockPos> posCollection;
    private final BlockPos focusPos;

    public FocusParticleS2CPacket(Collection<BlockPos> blockPosCollection, BlockPos focusBlockPos){
        this.posCollection = blockPosCollection;
        this.focusPos = focusBlockPos;
    }

    public FocusParticleS2CPacket(FriendlyByteBuf buf){
        this.posCollection = buf.readList(FriendlyByteBuf::readBlockPos);
        this.focusPos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeCollection(posCollection, FriendlyByteBuf::writeBlockPos);
        buf.writeBlockPos(focusPos);
    }

    public static void handle(FocusParticleS2CPacket msg, Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()-> ClientPacketHandler.handlePacket(msg,supplier)));
        context.setPacketHandled(true);
    }

    public Collection<BlockPos> getPosCollection() {
        return posCollection;
    }

    public BlockPos getFocusPos() {
        return focusPos;
    }
}
