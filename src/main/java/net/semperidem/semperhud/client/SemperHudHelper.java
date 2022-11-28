package net.semperidem.semperhud.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class SemperHudHelper {
    private static int getXofCenteredString(String string, int x){
        return x - (MinecraftClient.getInstance().textRenderer.getWidth(string) / 2);
    }

    private static int getXofRightString(String string, int x){
        return x - (MinecraftClient.getInstance().textRenderer.getWidth(string));
    }

    private static int getTextOffset(String text, int textAlignment) {
        return MinecraftClient.getInstance().textRenderer.getWidth(text) * textAlignment / 2;
    }
    public static void drawTextWithShadow(MatrixStack matrices, String text, int x, int y, float scalingFactor, int textColor, int textAlignment, float alpha){
        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.scale(scalingFactor,scalingFactor,scalingFactor);
        int scaledX = (int) (x / scalingFactor) - getTextOffset(text, textAlignment);
        int scaledY = (int) (y / scalingFactor);
        int a = Math.max(0,Math.min(255, (int)(alpha * 255)));
        DrawableHelper.drawStringWithShadow(
                matrices,
                MinecraftClient.getInstance().textRenderer,
                text,
                scaledX,
                scaledY,
                textColor + (a << 24)
        );
        matrices.pop();
    }

    public static void drawTextWithShadow(MatrixStack matrices, String text, int x, int y, float scalingFactor, int textColor, int textAlignment){
        matrices.push();
        matrices.scale(scalingFactor,scalingFactor,scalingFactor);
        int scaledX = (int) (x / scalingFactor) - getTextOffset(text, textAlignment);
        int scaledY = (int) (y / scalingFactor);
        DrawableHelper.drawStringWithShadow(
                matrices,
                MinecraftClient.getInstance().textRenderer,
                text,
                scaledX,
                scaledY,
                textColor
        );
        matrices.pop();
    }

    public static void drawTextWithShadow(MatrixStack matrices, String text, int x, int y, float scalingFactor, int textColor) {
        drawTextWithShadow(matrices, text, x, y, scalingFactor, textColor, 0);
    }
}
