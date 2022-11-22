package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;

public class HealthWidgetRenderer {
    private static final int HP_WIDGET_WIDTH = 192;
    private static final int HP_WIDGET_HEIGHT = 16;

    private static final int HP_WIDGET_X = 48;
    private static final int HP_WIDGET_Y = 5;

    private static final long ANIMATION_DURATION = 8000;

    private static final String HP_BAR_STRING = "textures/gui/health/";

    private static final Identifier HP_BAR_EMPTY = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-empty.png");
    private static final Identifier HP_BAR_BG = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-bg.png");
    private static final Identifier HP_BAR_ABSO = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-abso.png");
    private static final Identifier HP_BAR_DAMAGED = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-damaged.png");
    private static final Identifier HP_BAR_POISON = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-poison.png");
    private static final Identifier HP_BAR_WITHER = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-wither.png");
    private static final Identifier HP_BAR_FULL = new Identifier(SemperHudClient.getModId(), HP_BAR_STRING + "hp-bar-full.png");


    private MinecraftClient client;

    private long animationStartTS;
    private float startHealth = 0;
    private float animationHealth = 0;
    private float animationTargetHealth = 0;
    private float maxHealth = 0;

    public HealthWidgetRenderer(MinecraftClient client) {
        assert client.player != null;
        this.client = client;
        this.startHealth = client.player.getHealth();
        this.animationHealth = this.startHealth;
        this.animationTargetHealth = this.startHealth;
        this.maxHealth = client.player.getMaxHealth();
    }

    public void renderHealthWidget(MatrixStack matrices){
        long animationCurrentTS = System.currentTimeMillis();
        triggerAnimation(animationCurrentTS);

        this.animationHealth = getCurrentAnimationHealth(animationCurrentTS);

        matrices.push();
        drawHPContainer(matrices);
        //drawHPBar(matrices, getHPBarPercent(this.animationHealth), HP_BAR_DAMAGED,false);
        drawHPBar(matrices,getHPBarPercent(this.client.player.getHealth()), HP_BAR_FULL);
        if (this.client.player.hasStatusEffect(StatusEffects.ABSORPTION)) {
            drawHPBar(matrices,getHPBarPercent(this.client.player.getAbsorptionAmount()), HP_BAR_ABSO);
        }
        if (this.client.player.hasStatusEffect(StatusEffects.POISON)) {
            drawHPBar(matrices,getHPBarPercent(this.client.player.getHealth()), HP_BAR_POISON);
        }
        if (this.client.player.hasStatusEffect(StatusEffects.WITHER)) {
            drawHPBar(matrices,getHPBarPercent(this.client.player.getHealth()), HP_BAR_WITHER);
        }
        renderHealthInfo(matrices);
        matrices.pop();

    }

    private void renderHealthInfo(MatrixStack matrices){
        String hpString = String.format(this.animationTargetHealth >= 100 ? "%.0f" :"%.1f", this.animationTargetHealth);
        int hpStringOffset = client.textRenderer.getWidth(hpString);
        DrawableHelper.drawStringWithShadow(matrices, client.textRenderer, hpString, (HP_WIDGET_X + HP_WIDGET_WIDTH - 5) - hpStringOffset, (HP_WIDGET_Y + 4), 16777215);
    }

    private float getHPBarPercent(float health){
        return health / this.maxHealth;
    }

    private float getCurrentAnimationHealth(long animationCurrentTS) {
        return this.animationTargetHealth + ((this.startHealth - this.animationTargetHealth) * (1 - getAnimationPercent(animationCurrentTS)));
    }

    private float getAnimationPercent(long animationCurrentTS){
        boolean animationIsDone = animationCurrentTS - this.animationStartTS > 8000;
        return  animationIsDone ? 1 : (animationCurrentTS -  this.animationStartTS)  / (8000 * 1.0f);
    }

    private void triggerAnimation(long animationCurrentTS) {
        if (this.animationTargetHealth > this.client.player.getHealth()) {
            this.animationTargetHealth = this.client.player.getHealth();
            this.animationStartTS = animationCurrentTS;
            this.startHealth = this.animationHealth;
        } else {
            this.animationTargetHealth = this.client.player.getHealth();
            this.startHealth = this.animationHealth;
        }
    }

    private void drawHPContainer(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, HP_BAR_BG);
        DrawableHelper.drawTexture(
                matrices,
                HP_WIDGET_X,
                HP_WIDGET_Y,
                0,
                0,
                0,
                HP_WIDGET_WIDTH,
                HP_WIDGET_HEIGHT,
                HP_WIDGET_WIDTH,
                HP_WIDGET_HEIGHT
        );
    }
    private void drawHPBar(MatrixStack matrices, float barProgress, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(
                matrices,
                HP_WIDGET_X + 2,
                HP_WIDGET_Y,
                0,
                2,
                0,
                (int)(barProgress * (HP_WIDGET_WIDTH - 32)),
                HP_WIDGET_HEIGHT,
                HP_WIDGET_WIDTH,
                HP_WIDGET_HEIGHT
        );
    }
}
