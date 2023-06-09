package MagicWords.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ClientConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static final String[] creativeTabExcludeList = { "magicwords:glyph_a","magicwords:glyph_b","magicwords:glyph_c","magicwords:glyph_d","magicwords:glyph_e"};

    private static final Predicate<Object> isValidPredicate = x -> {
        if (x instanceof String){
            return ResourceLocation.isValidResourceLocation( (String) x);
        } else {
            return false;
        }
    };

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    public static ForgeConfigSpec.IntValue writingHudOverlayXOffset;
    public static ForgeConfigSpec.IntValue writingHudOverlayYOffset;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> creativeTabExclude;

    private static void setupConfig(ForgeConfigSpec.Builder builder){

        builder.push("Writing HUD Overlays");

        writingHudOverlayXOffset = builder
                .comment("Writing HUD's horizontal offset, in percent of screen from center")
                .defineInRange("writinghud_x_offset", 15, -100, 100);
        writingHudOverlayYOffset = builder
                .comment("Writing HUD's vertical offset, in percent of screen from center")
                .defineInRange("writinghud_y_offset", 80, -100, 100);

        builder.pop();
        builder.push("Creative Mode Tab Exclusions");

        creativeTabExclude = builder
                .comment("A list of blocks & items that shouldn't be in the creative mode tab")
                .defineList("creative_tab_exclude", Arrays.asList(creativeTabExcludeList), isValidPredicate);


    }

}
