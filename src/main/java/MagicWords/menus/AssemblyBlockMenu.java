package MagicWords.menus;

import MagicWords.block.entity.AssemblyBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class AssemblyBlockMenu extends AbstractMachineMenu {
    public final AssemblyBlockEntity blockEntity;

    public AssemblyBlockMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public AssemblyBlockMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.ASSEMBLY_BLOCK_MENU.get(), id, inv, entity, data, 5);
        checkContainerSize(inv, 5);
        blockEntity = (AssemblyBlockEntity) entity;

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 46, 17));
            this.addSlot(new SlotItemHandler(handler, 1, 66, 17));
            this.addSlot(new SlotItemHandler(handler, 2, 56, 53));
            this.addSlot(new SlotItemHandler(handler, 3, 106, 35));
            this.addSlot(new SlotItemHandler(handler, 4, 126, 35));
        });


    }



}
