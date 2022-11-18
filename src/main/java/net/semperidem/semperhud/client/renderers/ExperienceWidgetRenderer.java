package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;

public class ExperienceWidgetRenderer {
    private static final String EXP_STRING = "textures/gui/experience/xp-";
    private static final Identifier EXP_BAR_EMPTY = new Identifier(SemperHudClient.getModId(), EXP_STRING + "bar-empty.png");
    private static final Identifier EXP_BAR_FULL = new Identifier(SemperHudClient.getModId(), EXP_STRING + "bar-full.png");
    private static final Identifier EXP_BAR_GAIN = new Identifier(SemperHudClient.getModId(), EXP_STRING + "bar-gaining.png");
    private static final Identifier EXP_INFO = new Identifier(SemperHudClient.getModId(), EXP_STRING + "info.png");


    private static final long animationLength = 2000;

    private PlayerEntity player;

    private long animationStartTimeStamp = 0;
    private float startLevel = 0;
    private float currentLevel = 0;
    private float animationTargetLevel = 0;

    public ExperienceWidgetRenderer(MinecraftClient client) {
        this.player = client.player;
        this.startLevel = getPlayerLevel();
        this.currentLevel = this.startLevel;
        this.animationTargetLevel = this.startLevel;
    }

    public void renderExperienceWidget(MatrixStack matrices) {
        long animationCurrentTimestamp = System.currentTimeMillis();
        if (this.animationTargetLevel != getPlayerLevel()) {
            this.animationTargetLevel = getPlayerLevel();
            this.animationStartTimeStamp = animationCurrentTimestamp;
            this.startLevel = currentLevel;
        }

        this.currentLevel = getCurrentAnimationLevel(animationCurrentTimestamp);
        renderExperienceBar(matrices);
        renderExperienceInfo(matrices);
    }

    private void renderExperienceBar(MatrixStack matrices) {
        float animationCurrentProgress = getLevelProgress(this.currentLevel);
        float animationTargetProgress = getAnimationTargetProgress();

        RenderSystem.setShaderTexture(0, EXP_BAR_EMPTY);
        DrawableHelper.drawTexture(matrices, 1, 50,0, 0, 0, 256, 5, 256, 5);
        RenderSystem.setShaderTexture(0, EXP_BAR_GAIN);
        DrawableHelper.drawTexture(matrices, 1, 50,0, 0, 0, (int)(animationTargetProgress * 256), 5, 256, 5);
        RenderSystem.setShaderTexture(0, EXP_BAR_FULL);
        DrawableHelper.drawTexture(matrices, 1, 50,0, 0, 0, (int)(animationCurrentProgress * 256), 5, 256, 5);

    }

    private float getAnimationTargetProgress() {
        return Math.floor(this.animationTargetLevel) - Math.floor(this.currentLevel) > 0 ?
            1 : getLevelProgress(this.animationTargetLevel);
    }

    private float getLevelProgress(float level) {
        return (float) (level - Math.floor(level));
    }

    private float getCurrentAnimationLevel(long animationCurrentTimestamp){
        return this.startLevel + ((this.animationTargetLevel - startLevel) * getAnimationPercent(animationCurrentTimestamp));
    }

    private float getPlayerLevel(){
        return this.player.experienceLevel + this.player.experienceProgress;
    }

    private float getAnimationPercent(long current){
       return current - this.animationStartTimeStamp > animationLength ?
            1 :
            (current -  this.animationStartTimeStamp)  / (animationLength * 1.0f);
    }

    private void renderExperienceInfo(MatrixStack matrices) {
        int currentLevelInt = (int)Math.floor(this.currentLevel);
        int nextLevelXp = getNextLevelExperience((int) Math.floor(this.currentLevel));
        int currentXp = (int) (nextLevelXp * (this.currentLevel - currentLevelInt));

        String currentLevelString = String.valueOf(currentLevelInt);
        String currentXpString = String.valueOf(currentXp);
        String nextXpString = String.valueOf(nextLevelXp);

        matrices.push();
        RenderSystem.setShaderTexture(0, EXP_INFO);
        DrawableHelper.drawTexture(matrices, 1, 55,1, 0, 0, 48, 48, 48, 48);
        matrices.scale(1.5f,1.5f,1.5f);
        DrawableHelper.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, currentLevelString, 1 + 16 + SemperHudRenderer.getCentredText(Text.of(currentLevelString)),41,16768512);
        matrices.scale(2/3f,2/3f, 2/3f);
        DrawableHelper.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, currentXpString, 1 + 24 + SemperHudRenderer.getCentredText(Text.of(String.valueOf(currentXp))),80,16768512);
        DrawableHelper.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, nextXpString, 1 + 24 + SemperHudRenderer.getCentredText(Text.of(String.valueOf(nextLevelXp))),91,16768512);

        matrices.pop();

    }


    private int getNextLevelExperience(int currentLevel) {
        if (currentLevel >= 30) {
            return 112 + (currentLevel - 30) * 9;
        } else {
            return currentLevel >= 15 ? 37 + (currentLevel - 15) * 5 : 7 + currentLevel * 2;
        }
    }
}
