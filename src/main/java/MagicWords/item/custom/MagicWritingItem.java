package MagicWords.item.custom;

import MagicWords.block.custom.GlyphBlock;
import MagicWords.misc.ModTags;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.slf4j.Logger;

import java.util.List;

public class MagicWritingItem extends Item {
    public MagicWritingItem(Properties pProperties) {
        super(pProperties);
    }
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String[] lang = {"magicwords.glyph_a", "magicwords.glyph_b", "magicwords.glyph_c", "magicwords.glyph_d", "magicwords.glyph_e"};
    private static final ITag<Block> blocks = ForgeRegistries.BLOCKS.tags().getTag(ModTags.GLYPH_BLOCKS);
    private static List<Block> glyphList = blocks.stream().toList();


    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
//        int dmg = stack.getDamageValue();
        if (!context.getLevel().getBlockState(context.getClickedPos()).is(ModTags.FOCUS_BLOCKS)){
            CompoundTag nbt = stack.getOrCreateTag();
            if (nbt.contains("glyph_selected")) {
                int selector = nbt.getInt("glyph_selected");
                if (context.getPlayer().isSecondaryUseActive()) {
                    if (selector + 1 < lang.length && selector + 1 < glyphList.size()) {
                        nbt.putInt("glyph_selected", ++selector);
                    } else {
                        nbt.putInt("glyph_selected", 0);
                    }
                } else if (selector < glyphList.size()) {
                    return doPlacement(context, selector);
                }
            } else {
                nbt.putInt("glyph_selected", 0);
            }
            return InteractionResult.FAIL;
        } else {
            return InteractionResult.PASS;
        }
    }

    protected InteractionResult doPlacement(UseOnContext context, int glyphIndex){
        var placeContext = new BlockPlaceContext(context);
        boolean isClickedHorizontal = placeContext.getClickedFace().getAxis().isHorizontal();
        BlockState placingState = glyphList.get(glyphIndex).defaultBlockState()
                .setValue(GlyphBlock.FACING, isClickedHorizontal ? context.getClickedFace() : context.getHorizontalDirection().getOpposite())
                .setValue(GlyphBlock.IS_FLAT, !isClickedHorizontal);

        if (placeContext.canPlace() && placingState.canSurvive(context.getLevel(), placeContext.getClickedPos())){
            placeContext.getLevel().setBlockAndUpdate(placeContext.getClickedPos(), placingState);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    public static void updateGlyphList(){
        var tagKey = ForgeRegistries.BLOCKS.tags().getTag(ModTags.GLYPH_BLOCKS);
        glyphList = tagKey.stream().toList();
    }


}
