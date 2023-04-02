package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;
import net.semperidem.semperhud.client.SemperHudHelper;

import static net.semperidem.semperhud.client.SemperHudClient.MOD_ID;

public class ExperienceWidgetRenderer {
    public static final int EXP_BAR_HEIGHT = 5;
    public static final int EXP_BAR_WIDTH = 256;

    public static final int EXP_INFO_HEIGHT = 48;
    public static final int EXP_INFO_WIDTH = 48;

    public static final int EXP_WIDGET_X = 0;
    public static final int EXP_WIDGET_Y = 0;

    //Move to config
    public static long ANIMATION_DURATION = 2000;

    private static final int INFO_COLOR = 0xE69D20;

    private static final String EXP_STRING = "textures/gui/experience/";
    private static final Identifier EXP_BAR_EMPTY = new Identifier(MOD_ID, EXP_STRING + "exp-bar-empty.png");
    private static final Identifier EXP_BAR_FULL = new Identifier(MOD_ID, EXP_STRING + "exp-bar-full.png");
    private static final Identifier EXP_BAR_GAIN = new Identifier(MOD_ID, EXP_STRING + "exp-bar-gain.png");
    private static final Identifier EXP_INFO = new Identifier(MOD_ID, EXP_STRING + "exp-info.png");

    private ClientPlayerEntity clientPlayer;

    private long animationStartTS = 0;
    private float animationStartLevel = 0;
    private float animationLevel = 0;
    private float animationTargetLevel = 0;
    private SemperHudRenderer parent;

    public ExperienceWidgetRenderer(SemperHudRenderer parent) {
        this.parent = parent;
        this.clientPlayer = MinecraftClient.getInstance().player;
        this.animationStartLevel = getPlayerLevel();
        this.animationLevel = this.animationStartLevel;
        this.animationTargetLevel = this.animationStartLevel;
    }

    public void renderExperienceWidget(MatrixStack matrices) {
        clientPlayer = MinecraftClient.getInstance().player;
        long animationCurrentTS = System.currentTimeMillis();
        triggerAnimation(animationCurrentTS);

        this.animationLevel = getCurrentAnimationLevel(animationCurrentTS);

        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        renderExperienceBar(matrices);
        renderExperienceInfo(matrices);
        matrices.pop();
    }

    private void renderExperienceBar(MatrixStack matrices) {
        float animationCurrentProgress = getLevelProgress(this.animationLevel);
        float animationTargetProgress = getAnimationTargetProgress();

        drawExpBar(matrices, 1, EXP_BAR_EMPTY);
        drawExpBar(matrices, animationTargetProgress, EXP_BAR_GAIN);
        drawExpBar(matrices, animationCurrentProgress, EXP_BAR_FULL);
    }

    private void triggerAnimation(long animationCurrentTS) {
        if (this.animationTargetLevel != getPlayerLevel()) {
            this.animationStartTS = animationCurrentTS;
            this.animationStartLevel = this.animationLevel;
            this.animationTargetLevel = getPlayerLevel();
        }
    }

    private void drawExpBar(MatrixStack matrices, float barProgress, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(
                matrices,
                EXP_WIDGET_X,
                EXP_WIDGET_Y,
                0,
                0,
                0,
                (int)(barProgress * EXP_BAR_WIDTH),
                EXP_BAR_HEIGHT,
                EXP_BAR_WIDTH,
                EXP_BAR_HEIGHT
        );
    }

    private float getAnimationTargetProgress() {
        return Math.floor(this.animationTargetLevel) - Math.floor(this.animationLevel) > 0 ?
            1 : getLevelProgress(this.animationTargetLevel);
    }

    private float getLevelProgress(float level) {
        return (float) (level - Math.floor(level));
    }

    private float getCurrentAnimationLevel(long animationCurrentTS){
        return this.animationStartLevel + ((this.animationTargetLevel - animationStartLevel) * getAnimationPercent(animationCurrentTS));
    }

    private float getPlayerLevel(){
            return this.clientPlayer.experienceLevel + this.clientPlayer.experienceProgress;
    }

    private float getAnimationPercent(long animationCurrentTS){
        boolean animationIsDone = animationCurrentTS - this.animationStartTS > ANIMATION_DURATION;
        return  animationIsDone ? 1 : (animationCurrentTS -  this.animationStartTS)  / ((float) ANIMATION_DURATION);
    }

    private void renderExperienceInfo(MatrixStack matrices) {
        renderExperienceInfoContainer(matrices);
        int currentLevel = (int)Math.floor(this.animationLevel);
        //int currentExpToLevel = (int)((Math.ceil(animationLevel) - animationLevel) * (getNextLevelExperience((int) Math.floor(animationLevel))));

        int textX = EXP_WIDGET_X + EXP_INFO_WIDTH - 4;
        int textY = EXP_WIDGET_Y + EXP_BAR_HEIGHT + 6;
        SemperHudHelper.drawTextWithShadow(matrices, String.valueOf(currentLevel), textX, textY, 1.5f, INFO_COLOR, 2);
        //SemperHudHelper.drawTextWithShadow(matrices, String.valueOf(currentExpToLevel), textX, textY + 20, 1f, INFO_COLOR, 2);
    }



    private void renderExperienceInfoContainer(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, EXP_INFO);
        DrawableHelper.drawTexture(
                matrices,
                EXP_WIDGET_X,
                EXP_WIDGET_Y + EXP_BAR_HEIGHT,
                0,
                0,
                0,
                EXP_INFO_WIDTH,
                EXP_INFO_HEIGHT,
                EXP_INFO_WIDTH,
                EXP_INFO_HEIGHT
        );
    }

    private int getNextLevelExperience(int currentLevel) {
        if (currentLevel >= 30) {
            return 112 + (currentLevel - 30) * 9;
        } else {
            return currentLevel >= 15 ? 37 + (currentLevel - 15) * 5 : 7 + currentLevel * 2;
        }
    }
}
