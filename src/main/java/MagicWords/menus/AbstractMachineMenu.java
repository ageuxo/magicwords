package MagicWords.menus;

import MagicWords.block.ModBlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractMachineMenu extends AbstractContainerMenu {
    protected final Level level;
    protected final ContainerData data;
    protected final BlockEntity blockEntity;

    public <T extends BlockEntity> AbstractMachineMenu(MenuType<?> pMenuType, int pContainerId, Inventory inv, T blockEntity, ContainerData data, int slotCount) {
        super(pMenuType, pContainerId);
        this.blockEntity = blockEntity;
        this.level = inv.player.level;
        this.data = data;
        this.TE_INVENTORY_SLOT_COUNT = slotCount;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress(int progressArrowSize) {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getCurrentProgress() {
        return this.data.get(0);
    }

    public int getCurrentEnergy() {
        return this.data.get(2);
    }

    public int getScaledEnergy(int maxBarHeight) {
        int currentEnergy = this.data.get(2);
        int maxEnergy = this.data.get(3);
        int scaled = currentEnergy * maxBarHeight / maxEnergy;

        return maxEnergy != 0 && currentEnergy != 0 ? scaled : 0;
    }

    public int getMaxEnergy() {
        return this.data.get(3);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.ASSEMBLY_BLOCK.get());
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36+ = TileInventory slots, which map to our TileEntity slot numbers
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private final int TE_INVENTORY_SLOT_COUNT;  // must be the number of slots you have!

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    protected void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i<3;i++){
            for (int l = 0;l<9;l++){
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0;i<9;i++){
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
