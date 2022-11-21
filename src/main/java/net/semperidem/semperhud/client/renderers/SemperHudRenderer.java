package net.semperidem.semperhud.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SemperHudRenderer {
    private ExperienceWidgetRenderer experienceWidgetRenderer;
    private HealthWidgetRenderer healthWidgetRenderer;

    public SemperHudRenderer() {
        this.experienceWidgetRenderer = new ExperienceWidgetRenderer(MinecraftClient.getInstance());
        this.healthWidgetRenderer = new HealthWidgetRenderer(MinecraftClient.getInstance());
    }

    static int getCentredText(Text text){
        return - (MinecraftClient.getInstance().textRenderer.getWidth(text) / 2);
    }

    public void renderExperienceWidget(MatrixStack matrices){
        this.experienceWidgetRenderer.renderExperienceWidget(matrices);
    }

    public void renderStatusWidget(MatrixStack matrices) {
        this.healthWidgetRenderer.renderHealthWidget(matrices);
        renderIcons(matrices);
    }


    private void renderHealthWidget(MatrixStack matrices){

    }

    private void renderIcons(MatrixStack matrices){

    }
}
