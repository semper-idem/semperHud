package net.semperidem.semperhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.semperidem.semperhud.client.renderers.SemperHudRenderer;

@Environment(EnvType.CLIENT)
public class SemperHudClient implements ClientModInitializer {

    private static SemperHudRenderer instance;

    public static String getModId() {
        return "semperhud";
    }
    @Override
    public void onInitializeClient() {

    }

    public static SemperHudRenderer getInstance(){
        if (instance == null) {
            return new SemperHudRenderer();
        } else {
            return instance;
        }
    }
}
