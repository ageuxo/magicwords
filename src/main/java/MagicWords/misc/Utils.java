package MagicWords.misc;

import net.minecraft.util.FastColor;

public class Utils {
    //Packed Colour ARGB
    public static float colourUnpackedToAlpha(int colour){
        return ((float) colour /255);
    }

    public static float unpackAlphaToFloat(int packedARGB){
        return colourUnpackedToAlpha(FastColor.ARGB32.alpha(packedARGB));
    }

    public static float unpackRedToFloat(int packedARGB){
        return colourUnpackedToAlpha(FastColor.ARGB32.red(packedARGB));
    }

    public static float unpackGreenToFloat(int packedARGB){
        return colourUnpackedToAlpha(FastColor.ARGB32.green(packedARGB));
    }

    public static float unpackBlueToFloat(int packedARGB){
        return colourUnpackedToAlpha(FastColor.ARGB32.blue(packedARGB));
    }

}
