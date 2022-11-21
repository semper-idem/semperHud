package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;

public class ExperienceWidgetRenderer {
    private static final int EXP_BAR_HEIGHT = 5;
    private static final int EXP_BAR_WIDTH = 256;

    private static final int EXP_INFO_HEIGHT = 48;
    private static final int EXP_INFO_WIDTH = 48;

    private static final int EXP_WIDGET_X = 0;
    private static final int EXP_WIDGET_Y = 0;

    private static final long ANIMATION_DURATION = 2000;

    private static final int TEXT_COLOR = 16768512;

    private static final String EXP_STRING = "textures/gui/experience/";
    private static final Identifier EXP_BAR_EMPTY = new Identifier(SemperHudClient.getModId(), EXP_STRING + "exp-bar-empty.png");
    private static final Identifier EXP_BAR_FULL = new Identifier(SemperHudClient.getModId(), EXP_STRING + "exp-bar-full.png");
    private static final Identifier EXP_BAR_GAIN = new Identifier(SemperHudClient.getModId(), EXP_STRING + "exp-bar-gain.png");
    private static final Identifier EXP_INFO = new Identifier(SemperHudClient.getModId(), EXP_STRING + "exp-info.png");

    private MinecraftClient client;

    private long animationStartTS = 0;
    private float startLevel = 0;
    private float animationLevel = 0;
    private float animationTargetLevel = 0;

    public ExperienceWidgetRenderer(MinecraftClient client) {
        this.client = client;
        this.startLevel = getPlayerLevel();
        this.animationLevel = this.startLevel;
        this.animationTargetLevel = this.startLevel;
    }

    public void renderExperienceWidget(MatrixStack matrices) {
        long animationCurrentTS = System.currentTimeMillis();
        triggerAnimation(animationCurrentTS);

        this.animationLevel = getCurrentAnimationLevel(animationCurrentTS);

        matrices.push();
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
            this.animationTargetLevel = getPlayerLevel();
            this.animationStartTS = animationCurrentTS;
            this.startLevel = this.animationLevel;
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
        return this.startLevel + ((this.animationTargetLevel - startLevel) * getAnimationPercent(animationCurrentTS));
    }

    private float getPlayerLevel(){
        if (this.client.player != null) {
            return this.client.player.experienceLevel + this.client.player.experienceProgress;
        }
        return 0;
    }

    private float getAnimationPercent(long animationCurrentTS){
        boolean animationIsDone = animationCurrentTS - this.animationStartTS > ANIMATION_DURATION;
        return  animationIsDone ? 1 : (animationCurrentTS -  this.animationStartTS)  / (ANIMATION_DURATION * 1.0f);
    }

    private void renderExperienceInfo(MatrixStack matrices) {
        renderExperienceInfoContainer(matrices);
        int currentLevelInt = (int)Math.floor(this.animationLevel);
        int nextLevelXp = getNextLevelExperience((int) Math.floor(this.animationLevel));
        int xpLeft = nextLevelXp - (int) (nextLevelXp * (this.animationLevel - currentLevelInt));

        String currentLevelString = String.valueOf(currentLevelInt);
        String xpLeftString = String.valueOf(xpLeft);

        renderExperienceInfoText(matrices, 1.5f, client.textRenderer, currentLevelString, 6);
        renderExperienceInfoText(matrices, 1, client.textRenderer, xpLeftString, 26);
    }


    private void renderExperienceInfoText(MatrixStack matrices, float scalingFactor, TextRenderer textRenderer, String text, int textY){
        matrices.scale(scalingFactor,scalingFactor,scalingFactor);
        int x = (int) ((EXP_WIDGET_X + (EXP_INFO_WIDTH/2) + 20) / scalingFactor) - textRenderer.getWidth(text);
        int y = (int) ((EXP_WIDGET_Y + EXP_BAR_HEIGHT + textY) / scalingFactor);
        DrawableHelper.drawStringWithShadow(
                matrices,
                textRenderer,
                text,
                x,
                y,
                TEXT_COLOR
        );
        matrices.scale(1 / scalingFactor,1 / scalingFactor, 1 / scalingFactor);
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
