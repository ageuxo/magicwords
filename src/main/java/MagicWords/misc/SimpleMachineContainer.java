package MagicWords.misc;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.StackedContentsCompatible;

import java.util.stream.IntStream;

public class SimpleMachineContainer extends SimpleContainer implements Container, StackedContentsCompatible {
    private final int[] inputSlots;
    private final int[] extraSlots;
    private final int[] outputSlots;


    public SimpleMachineContainer(int[] inputSlots, int[] extraSlots, int[] outputSlots){
        this.inputSlots = inputSlots;
        this.extraSlots = extraSlots;
        this.outputSlots = outputSlots;
    }

    public SimpleMachineContainer(int inSlotCount, int extSlotCount, int outSlotCount) {
        super(inSlotCount + extSlotCount+ outSlotCount);
        this.inputSlots = IntStream.range(0, inSlotCount).toArray();
        this.extraSlots = IntStream.range(inSlotCount, inSlotCount + extSlotCount).toArray();
        this.outputSlots = IntStream.range(inSlotCount + extSlotCount, inSlotCount + extSlotCount + outSlotCount).toArray();
    }

    public int[] getInputSlots() {
        return this.inputSlots;
    }

    public int[] getExtraSlots() {
        return this.extraSlots;
    }

    public int[] getOutputSlots() {
        return this.outputSlots;
    }
}

