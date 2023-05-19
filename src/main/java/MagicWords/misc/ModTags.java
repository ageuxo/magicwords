package MagicWords.misc;

import MagicWords.MagicWords;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static ResourceLocation modTagRL(String tagLocation){
        return new ResourceLocation(MagicWords.MODID, tagLocation);
    }

    public static final TagKey<Block> GLYPH_BLOCKS = BlockTags.create(modTagRL("glyph_blocks"));


}
