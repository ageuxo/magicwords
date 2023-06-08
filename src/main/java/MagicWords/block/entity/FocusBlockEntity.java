package MagicWords.block.entity;

import MagicWords.misc.ModTags;
import MagicWords.networking.MagicwordsPacketHandler;
import MagicWords.networking.packet.FocusParticleS2CPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class FocusBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ItemStackHandler itemHandler = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            FocusBlockEntity.this.sendUpdate();
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int progress;
    private final Map<BlockPos, BlockState> glyphMap = new HashMap<>();



    public FocusBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FOCUS_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()->itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("focus_block_stack", itemHandler.getStackInSlot(0).serializeNBT());
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        itemHandler.setStackInSlot(0, ItemStack.of(tag.getCompound("focus_block_stack")));
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        return super.triggerEvent(pId, pType);
    }

    public void scanForGlyphs(){
        BlockPos focusPos = this.worldPosition;
        glyphMap.clear();
        BlockPos.betweenClosedStream( new AABB(focusPos.offset(4, 3, 4), focusPos.offset(-4, -2, -4)) ).forEach(pos -> {
            BlockState posState = level.getBlockState(pos);
            if (posState.is(ModTags.GLYPH_BLOCKS)){
//                LOGGER.debug("Putting: {} {}", pos, posState);
                glyphMap.put(pos.immutable(), posState);

            }
        });
        if (!level.isClientSide()){
            MagicwordsPacketHandler.INSTANCE
                    .send(PacketDistributor.NEAR.with(()-> {
                        return new PacketDistributor.TargetPoint(focusPos.getX(), focusPos.getY(), focusPos.getZ(), 30, this.level.dimension());
                    }), new FocusParticleS2CPacket(glyphMap.keySet(), this.getBlockPos()));
        }
    }

    public ItemStack insertItem(ItemStack stack){
        return itemHandler.insertItem(0, stack, false);
    }

    public ItemStack extractItem(){
        return itemHandler.extractItem(0, getInventoryStack().getCount(), false);
    }

    public ItemStack getInventoryStack(){
        return itemHandler.getStackInSlot(0);
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void sendUpdate(){
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void sendMagicParticles(){
        var pos = this.getBlockPos();
        MagicwordsPacketHandler.INSTANCE.send(PacketDistributor.NEAR
                .with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 30, this.level.dimension())),
                new FocusParticleS2CPacket(glyphMap.keySet(), this.getBlockPos()));
    }
}
