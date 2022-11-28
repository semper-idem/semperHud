package net.semperidem.semperhud.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SemperHudRenderer {
    private ExperienceWidgetRenderer experienceWidgetRenderer;
    private HealthWidgetRenderer healthWidgetRenderer;
    private IconsWidgetRenderer iconsWidgetRenderer;
    private HotbarRenderer hotbarRenderer;

    public SemperHudRenderer() {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer == null) {
            throw new NullPointerException("Client Player cannot be null here");
        }
        this.experienceWidgetRenderer = new ExperienceWidgetRenderer(clientPlayer);
        this.healthWidgetRenderer = new HealthWidgetRenderer(clientPlayer);
        this.iconsWidgetRenderer = new IconsWidgetRenderer(clientPlayer);
        this.hotbarRenderer = new HotbarRenderer(clientPlayer);
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
