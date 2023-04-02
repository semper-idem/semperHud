package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudHelper;

import static net.semperidem.semperhud.client.SemperHudClient.MOD_ID;

public class HealthWidgetRenderer {
    private static final int HP_WIDGET_WIDTH = 192;
    private static final int HP_WIDGET_HEIGHT = 16;
    private static final int HP_INFO_OFFSET = 32;

    private static final int HP_BAR_WIDTH = 160;
    private static final int HP_BAR_HEIGHT = 8;

    private static final int HP_BAR_CONTAINER_WIDTH = 164;
    private static final int HP_BAR_CONTAINER_HEIGHT = 12;

    private static final int HP_INFO_WIDTH = 40;
    private static final int HP_INFO_HEIGHT = 16;

    private static final int HP_WIDGET_X = 48;
    private static final int HP_WIDGET_Y = 5;

    private static final int ANIMATION_DURATION = 4000;

    private static final int INFO_COLOR = 16777215;

    private static final String TEXTURE_LOCATION = "textures/gui/health/";

    private static final Identifier HP_INFO_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-info.png");
    private static final Identifier HP_BAR_CONTAINER_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-empty.png");
    private static final Identifier HP_BAR_CONTAINER_TEXTURE2 = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-empty2.png");
    private static final Identifier HP_BAR_BACKGROUND_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-bg.png");
    private static final Identifier ABSORPTION_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-abso.png");
    private static final Identifier DAMAGING_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-damaged.png");
    private static final Identifier POISON_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-poison.png");
    private static final Identifier WITHER_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-wither.png");
    private static final Identifier HP_TEXTURE = new Identifier(MOD_ID, TEXTURE_LOCATION + "hp-bar-full.png");

    private ClientPlayerEntity clientPlayer;

    private long animationStartTS;
    private float startHealth = 0;
    private float animationHealth = 0;
    private float animationTargetHealth = 0;

    private SemperHudRenderer parent;

    public HealthWidgetRenderer(SemperHudRenderer parent) {
        this.parent = parent;
        this.clientPlayer = MinecraftClient.getInstance().player;
        this.startHealth = clientPlayer.getHealth();
        this.animationHealth = startHealth;
        this.animationTargetHealth = startHealth;
    }

    public void renderHealthWidget(MatrixStack matrices){
        clientPlayer = MinecraftClient.getInstance().player;
        matrices.push();

        drawHealthInfo(matrices);
        drawBar(matrices, 1, HP_BAR_BACKGROUND_TEXTURE);
        drawDamageBar(matrices);
        drawBar(matrices,getHPBarPercent(clientPlayer.getHealth()), HP_TEXTURE);
        drawAbsorptionBar(matrices);
        drawPoisonBar(matrices);
        drawWitherBar(matrices);

        drawContainer(matrices);

        matrices.pop();
    }


    private void drawDamageBar(MatrixStack matrices) {
        long animationCurrentTS = System.currentTimeMillis();
        if (animationTargetHealth > clientPlayer.getHealth()) {
            animationStartTS = animationCurrentTS;
        }
        animationTargetHealth = clientPlayer.getHealth();
        startHealth = animationHealth;
        animationHealth = getCurrentAnimationHealth(animationCurrentTS);
        drawBar(matrices, getHPBarPercent(animationHealth), DAMAGING_TEXTURE);
    }

    private void drawAbsorptionBar(MatrixStack matrices){
        if (clientPlayer.hasStatusEffect(StatusEffects.ABSORPTION)) {
            drawBar(matrices,getHPBarPercent(clientPlayer.getAbsorptionAmount()), ABSORPTION_TEXTURE);
        }
    }
    private void drawWitherBar(MatrixStack matrices){
        if (clientPlayer.hasStatusEffect(StatusEffects.POISON)) {
            drawBar(matrices,getHPBarPercent(clientPlayer.getHealth()), POISON_TEXTURE);
        }
    }
    private void drawPoisonBar(MatrixStack matrices){
        if (clientPlayer.hasStatusEffect(StatusEffects.WITHER)) {
            drawBar(matrices,getHPBarPercent(clientPlayer.getHealth()), WITHER_TEXTURE);
        }
    }

    private void drawHealthInfo(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, HP_INFO_TEXTURE);
        DrawableHelper.drawTexture(
                matrices,
                HP_WIDGET_X,
                HP_WIDGET_Y,
                0,
                0,
                0,
                HP_INFO_WIDTH,
                HP_INFO_HEIGHT,
                HP_INFO_WIDTH,
                HP_INFO_HEIGHT
        );

        String hpString = String.format(animationTargetHealth >= 100 ? "%.0f" :"%.1f", animationTargetHealth);
        SemperHudHelper.drawTextWithShadow(matrices, hpString, HP_WIDGET_X + HP_INFO_WIDTH - 5, HP_WIDGET_Y + 4, 1f, INFO_COLOR, 2);
   }

    private float getHPBarPercent(float health){
        return Math.min(1, health / clientPlayer.getMaxHealth());
    }

    private float getCurrentAnimationHealth(long animationCurrentTS) {
        return this.animationTargetHealth + ((this.startHealth - this.animationTargetHealth) * (1 - getAnimationPercent(animationCurrentTS)));
    }

    private float getAnimationPercent(long animationCurrentTS){
        boolean animationIsDone = animationCurrentTS - this.animationStartTS > ANIMATION_DURATION;
        return  animationIsDone ? 1 : (animationCurrentTS -  this.animationStartTS)  / ((float) ANIMATION_DURATION);
    }

    private void drawContainer(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, HP_BAR_CONTAINER_TEXTURE2);
        DrawableHelper.drawTexture(
                matrices,
                HP_WIDGET_X + HP_INFO_WIDTH,
                HP_WIDGET_Y,
                0,
                0,
                0,
                HP_BAR_CONTAINER_WIDTH,
                HP_BAR_CONTAINER_HEIGHT,
                HP_BAR_CONTAINER_WIDTH,
                HP_BAR_CONTAINER_HEIGHT
        );
    }
    private void drawBar(MatrixStack matrices, float barProgress, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(
                matrices,
                HP_WIDGET_X + HP_INFO_WIDTH + 2,
                HP_WIDGET_Y + 2,
                0,
                0,
                0,
                (int)(barProgress * (HP_BAR_WIDTH   )),
                HP_BAR_HEIGHT,
                HP_BAR_WIDTH,
                HP_BAR_HEIGHT
        );
    }
}
