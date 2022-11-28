package net.semperidem.semperhud.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.semperidem.semperhud.client.renderers.SemperHudRenderer;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SemperHudClient implements ClientModInitializer {
    public static final String MOD_ID = "semperhud";
    public static boolean isHudAlpha;
    public static float alpha = 0.5f;
    private static SemperHudConfig config;

    private static SemperHudRenderer instance;
    private static KeyBinding openConfigKey;
    @Override
    public void onInitializeClient() {
        AutoConfig.register(SemperHudConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(SemperHudConfig.class).getConfig();
        registerConfigKey();
    }

    private void registerConfigKey(){
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.semperhud.openConfig",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "text.autoconfig.semperhud.title"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(openConfigKey.wasPressed()){
                client.setScreen(AutoConfig.getConfigScreen(SemperHudConfig.class, client.currentScreen).get());
            }
        });
    }

    public static boolean isSemperHudEnabled(){
        return config.semperHotbar;
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
