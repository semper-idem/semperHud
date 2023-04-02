package net.semperidem.semperhud.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SemperHudRenderer {
    public int MAIN_WIDGET_X = 0;
    public int MAIN_WIDGET_Y = 0;
    private ExperienceWidgetRenderer experienceWidgetRenderer;
    private HealthWidgetRenderer healthWidgetRenderer;
    private IconsWidgetRenderer iconsWidgetRenderer;
    private HotbarRenderer hotbarRenderer;

    public SemperHudRenderer() {
            this.experienceWidgetRenderer = new ExperienceWidgetRenderer(this);
            this.healthWidgetRenderer = new HealthWidgetRenderer(this);
            this.iconsWidgetRenderer = new IconsWidgetRenderer(this);
            this.hotbarRenderer = new HotbarRenderer(this);
    }

    public void renderExperienceWidget(MatrixStack matrices){
        this.experienceWidgetRenderer.renderExperienceWidget(matrices);
    }

    public void renderStatusWidget(MatrixStack matrices) {
        this.healthWidgetRenderer.renderHealthWidget(matrices);
        this.iconsWidgetRenderer.renderIconsWidget(matrices);
    }

    public void renderHotbar(float tickDelta, MatrixStack matrices) {
        this.hotbarRenderer.renderHotbar(tickDelta, matrices);
    }
}
