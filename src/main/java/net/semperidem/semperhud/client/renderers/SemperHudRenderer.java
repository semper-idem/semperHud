package net.semperidem.semperhud.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SemperHudRenderer {
    private ExperienceWidgetRenderer experienceWidgetRenderer;

    public SemperHudRenderer() {
        this.experienceWidgetRenderer = new ExperienceWidgetRenderer(MinecraftClient.getInstance());
    }

    static int getCentredText(Text text){
        return - (MinecraftClient.getInstance().textRenderer.getWidth(text) / 2);
    }

    public void renderExperienceWidget(MatrixStack matrices){
        this.experienceWidgetRenderer.renderExperienceWidget(matrices);
    }

    public static void renderStatusBars(MatrixStack matrices) {
        renderHealthBar(matrices);
        renderIcons(matrices);
    }

    private static void renderHealthBar(MatrixStack matrices){

    }

    private static void renderIcons(MatrixStack matrices){

    }
}
