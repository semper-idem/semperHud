package net.semperidem.semperhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.semperidem.semperhud.client.renderers.SemperHudRenderer;

@Environment(EnvType.CLIENT)
public class SemperHudClient implements ClientModInitializer {
    public static boolean isHudAlpha;
    public static float alpha = 0.5f;

    private static SemperHudRenderer instance;

    public static String getModId() {
        return "semperhud";
    }
    @Override
    public void onInitializeClient() {

    }

    public static int modifyArgb(int argb){
        int oldAlpha = (argb >> 24) & 0xFF;
        if ((oldAlpha & 0xFC) == 0) {
            oldAlpha = 0xFF;
        }
        return (Math.round(alpha * oldAlpha) << 24) | (argb & 0xFFFFFF);
    }

    public static SemperHudRenderer getInstance(){
        if (instance == null) {
            instance = new SemperHudRenderer();
        }
        return instance;
    }
}
