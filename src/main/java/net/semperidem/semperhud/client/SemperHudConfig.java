package net.semperidem.semperhud.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "semperhud")
public class SemperHudConfig implements ConfigData {
    public enum HotbarTexture{
        DARK_NUMBERED,
        DARK,
        VANILLA
    }

    public boolean semperHud = true;
    public boolean semperHotbar = false;
    public HotbarTexture hotbarTexture = HotbarTexture.DARK_NUMBERED;

}