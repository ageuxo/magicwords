package MagicWords.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    public static ForgeConfigSpec.IntValue writingHudOverlayXOffset;
    public static ForgeConfigSpec.IntValue writingHudOverlayYOffset;

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        writingHudOverlayXOffset = builder
                .comment("Writing HUD's horizontal offset, in percent of screen from center")
                .defineInRange("writinghud_x_offset", 15, -100, 100);
        writingHudOverlayYOffset = builder
                .comment("Writing HUD's vertical offset, in percent of screen from center")
                .defineInRange("writinghud_y_offset", 80, -100, 100);
    }
}
