package MagicWords.networking.packet;

import MagicWords.client.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
    private final ItemStackHandler itemStackHandler;
    private final BlockPos blockPos;

    public ItemStackSyncS2CPacket(ItemStackHandler itemStackHandler, BlockPos blockPos) {
        this.itemStackHandler = itemStackHandler;
        this.blockPos = blockPos;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        List<ItemStack> stackList = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        itemStackHandler = new ItemStackHandler(stackList.size());
        for (int i = 0; i < stackList.size(); i++) {
            itemStackHandler.insertItem(i, stackList.get(i), false);
        }

        this.blockPos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf){
        Collection<ItemStack> stackCollection = new ArrayList<>();
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            stackCollection.add(itemStackHandler.getStackInSlot(i));
        }
        buf.writeCollection(stackCollection, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(blockPos);
    }

    public static void handle(ItemStackSyncS2CPacket msg, Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()-> ()-> ClientPacketHandler.handlePacket(msg, supplier));
        });
        context.setPacketHandled(true);
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
