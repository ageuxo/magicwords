package MagicWords.block.entity;

import MagicWords.block.custom.AssemblyBlock;
import MagicWords.item.crafting.AssemblyRecipe;
import MagicWords.item.crafting.ModRecipes;
import MagicWords.item.crafting.StackIngredient;
import MagicWords.menus.AssemblyBlockMenu;
import MagicWords.misc.SimpleMachineContainer;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Map;

public class AssemblyBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(5){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

      /*  @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot){
                case 3,4 -> false;
                default -> super.isItemValid(slot,stack);
        };
        }*/
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0 || index == 1,
                            (index, stack) -> itemHandler.isItemValid(0, stack) || itemHandler.isItemValid(1, stack))));

    private final EnergyStorage energyStorage = new EnergyStorage(8000, 256, 256, 8000);
    private LazyOptional<EnergyStorage> lazyEnergyStorage = LazyOptional.empty();


    protected final ContainerData data;
    private static final RecipeManager.CachedCheck<SimpleMachineContainer, AssemblyRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.Types.ASSEMBLY_RECIPE_TYPE.get());
    private int progress = 0;
    private int maxProgress = 100;
    private final int inputSlotCount;
    private final int extraSlotCount;
    private final int outputSlotCount;

    private AssemblyRecipe currentRecipe;

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int DEFAULT_INPUT_SIZE = 2;
    private static final int DEFAULT_EXTRA_SIZE = 1;
    private static final int DEFAULT_OUTPUT_SIZE = 2;

    public AssemblyBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(pPos, pBlockState, DEFAULT_INPUT_SIZE, DEFAULT_EXTRA_SIZE, DEFAULT_OUTPUT_SIZE);
    }

    public AssemblyBlockEntity(BlockPos pPos, BlockState pBlockState, int inputSize, int extraSize, int outputSize) {
        super(ModBlockEntities.ASSEMBLY_BLOCK_ENTITY.get(), pPos, pBlockState);

        this.inputSlotCount = inputSize;
        this.extraSlotCount = extraSize;
        this.outputSlotCount = outputSize;

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                    case 0 -> AssemblyBlockEntity.this.progress;
                    case 1 -> AssemblyBlockEntity.this.maxProgress;
                    case 2 -> AssemblyBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> AssemblyBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex){
                    case 0 -> AssemblyBlockEntity.this.progress = pValue;
                    case 1 -> AssemblyBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.magicwords.assembly_block.name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AssemblyBlockMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            if (side == null){
                return lazyItemHandler.cast();
            }

            if (directionWrappedHandlerMap.containsKey(side)){
                Direction localDir = this.getBlockState().getValue(AssemblyBlock.FACING);

                if (side == Direction.UP || side == Direction.DOWN){
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir){
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        } else if (cap == ForgeCapabilities.ENERGY) {
            if (side == null){
                return lazyEnergyStorage.cast();
            }

        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()-> itemHandler);
        lazyEnergyStorage = LazyOptional.of(()-> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyStorage.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("assembly_block_progress", this.progress);
        pTag.put("energy_storage", energyStorage.serializeNBT());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("assembly_block_progress");
        energyStorage.deserializeNBT(pTag.get("energy_storage"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AssemblyBlockEntity entity) {
        if(level.isClientSide){
            return;
        }
        if (hasRecipe(entity) && isFueled(entity)){
            entity.progress++;
            entity.energyStorage.extractEnergy(entity.currentRecipe.getEnergyCost(), false);
            setChanged(level, pos, state);

            if (entity.progress >= entity.maxProgress){
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(AssemblyBlockEntity entity) {
        if (hasRecipe(entity) && isFueled(entity)){
            SimpleMachineContainer container = new SimpleMachineContainer(entity.inputSlotCount, entity.extraSlotCount, entity.outputSlotCount);
            // Copy over inventory
            for (int i = 0; i < entity.itemHandler.getSlots(); i++){
                container.setItem(i, entity.itemHandler.getStackInSlot(i));
            }
//            entity.itemHandler.extractItem(0, 1, false);
//            entity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.CHALK.get(), entity.itemHandler.getStackInSlot(2).getCount() + 1));
            int counter = 0;
            for (StackIngredient ingredient : entity.currentRecipe.getStackIngredients()){
            /*    ItemStack ingStack;
                for (int index : container.getInputSlots()){
                    if (ingredient.getRecipeType() == StackIngredient.Type.STACK){
                        if (ItemStack.matches(ingredient.getStack(), container.getItem(index))){
                            ingStack = entity.itemHandler.extractItem(index, ingredient.getStack().getCount(), false);
                            entity.resetProgress();
                            counter++;
                            if (ingStack.isEmpty()){
                                break;
                            }
                        } else if (container.getItem(index).getItem() == ingredient.getStack().getItem() && ingredient.getStack().getCount() <= container.getItem(index).getCount()){
                            ingStack = entity.itemHandler.extractItem(index, ingredient.getStack().getCount(), false);
                            entity.resetProgress();
                            counter++;
                            if (ingStack.isEmpty()){
                                break;
                            }
                        }
                    } else {
                        if (container.getItem(index).getCount() >= ingredient.getCount() && container.getItem(index).getTags().anyMatch(tagKey -> tagKey.equals(ingredient.getTagKey())) ){
                            ingStack = entity.itemHandler.extractItem(index, ingredient.getCount(), false);
                            entity.resetProgress();
                            counter++;
                            if (ingStack.isEmpty()){
                                break;
                            }
                        }
                    }
                }*/

                int keepCount = 0;
                for (int index : container.getInputSlots() ) {
                    ItemStack stack = container.getItem(index);
                    if (ingredient.getRecipeType() == StackIngredient.Type.STACK ){
                        if (ItemStack.matches(stack, ingredient.getStack()) || (stack.is(ingredient.getStack().getItem())) ) {
                            if (stack.getCount() >= ingredient.getCount() || (stack.getCount() + keepCount) >= ingredient.getCount()){
                                entity.itemHandler.extractItem(index, ingredient.getCount(), false);
                                entity.resetProgress();
                                counter++;
                                break;
                            } else{
                                keepCount = entity.itemHandler.extractItem(index, ingredient.getCount(), false).getCount() + keepCount;
                            }
                        }
                    } else if (stack.getTags().anyMatch(tagKey -> tagKey.equals(ingredient.getTagKey()))){
                        if (stack.getCount() >= ingredient.getCount() || (stack.getCount() + keepCount) >= ingredient.getCount()){
                            entity.itemHandler.extractItem(index, ingredient.getCount(), false);
                            entity.resetProgress();
                            counter++;
                            break;
                        } else{
                            keepCount = entity.itemHandler.extractItem(index, stack.getCount(), false).getCount() + keepCount;
                        }
                    }
                }

            }

            // Output items, since we know there is enough space
            if (counter >= entity.currentRecipe.getStackIngredients().size()){
                ItemStack stack = entity.currentRecipe.getResultItem(entity.getLevel().registryAccess());
                for (int outSlot : container.getOutputSlots()){
                    stack = entity.itemHandler.insertItem(outSlot, stack, false);
                    if (stack.isEmpty()){
                        return;
                    }
                }
            }

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(AssemblyBlockEntity entity) {
        SimpleMachineContainer container = new SimpleMachineContainer(entity.inputSlotCount, entity.extraSlotCount, entity.outputSlotCount);
        // Copy over inventory
        for (int i = 0; i < entity.itemHandler.getSlots(); i++){

            container.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        AssemblyRecipe recipe = quickCheck.getRecipeFor(container, entity.level).orElse(null);
        if (recipe != null) {
            entity.currentRecipe = recipe;
            return recipe.matches(container, entity.level) && canOutput(container, recipe.getResultItem(entity.level.registryAccess()));
        }
        return false;
    }

    private static boolean canOutput(SimpleMachineContainer container, ItemStack recipeOutput){
        for (int index : container.getOutputSlots()) {
            if (container.canPlaceItem(index, recipeOutput)) {
                if (container.getItem(index).isEmpty()) {
                    return true;
                } else if (container.getItem(index).getItem() == recipeOutput.getItem()) {
                    if (container.getItem(index).getMaxStackSize() > container.getItem(index).getCount() + recipeOutput.getCount()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isFueled(AssemblyBlockEntity entity){
        return entity.energyStorage.getEnergyStored() >= entity.currentRecipe.getEnergyCost();
    }
}
