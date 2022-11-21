package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;

public class HealthWidgetRenderer {
    private static final int HP_BAR_WIDTH = 128;
    private static final int HP_WIDGET_WIDTH = 144;
    private static final int HP_WIDGET_HEIGHT = 8;

    private static final int HP_WIDGET_X = 50;

    private static final int HP_WIDGET_Y = 20;

    private static final String HP_BAR_STRING = "textures/gui/health/";

    private static final Identifier HP_BAR_EMPTY = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-empty.png");
    private static final Identifier HP_BAR_FULL = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-full.png");


    private MinecraftClient client;
    private float health = 20;
    private float maxHealth = 20;

    public HealthWidgetRenderer(MinecraftClient client) {
        assert client.player != null;
        this.client = client;
        this.health = client.player.getHealth();
        this.maxHealth = client.player.getMaxHealth();
    }

    public void renderHealthWidget(MatrixStack matrices){
        float hpBar = this.client.player.getHealth() / this.client.player.getMaxHealth();
        drawHPBar(matrices, 1, HP_BAR_EMPTY,true);
        drawHPBar(matrices,hpBar, HP_BAR_FULL, false);

    }


    private void drawHPBar(MatrixStack matrices, float barProgress, Identifier texture, boolean isContainer) {
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(
                matrices,
                HP_WIDGET_X,
                HP_WIDGET_Y,
                0,
                0,
                0,
                isContainer ? HP_WIDGET_WIDTH : (int)(barProgress * HP_BAR_WIDTH),
                HP_WIDGET_HEIGHT,
                isContainer ? HP_WIDGET_WIDTH : HP_BAR_WIDTH,
                HP_WIDGET_HEIGHT
        );
    }
}
