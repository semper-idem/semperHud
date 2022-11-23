package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.semperidem.semperhud.client.SemperHudClient;

public class IconsWidgetRenderer {
    private static final int ICON_WIDTH = 64;
    private static final int ICON_HEIGHT = 32;

    private static final int DETAIL_WIDTH = 64;
    private static final int DETAIL_HEIGHT = 32;

    private static final int ICONS_WIDGET_X = 92;
    private static final int ICONS_WIDGET_Y = 18;

    private static final String ICONS_STRING = "textures/gui/icons/";

    private static final Identifier ICONS = new Identifier(SemperHudClient.getModId(), ICONS_STRING + "icons.png");
    private static final Identifier DETAIL = new Identifier(SemperHudClient.getModId(), ICONS_STRING + "detail.png");


    private MinecraftClient client;

    public IconsWidgetRenderer(MinecraftClient client) {
        assert client.player != null;
        this.client = client;
    }


    public void renderIconsWidget(MatrixStack matrices){
        renderIcon(matrices, 0, String.valueOf(this.client.player.getHungerManager().getFoodLevel()));
        matrices.push();
        this.client.player.isSubmergedInWater();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, client.player.isSubmergedInWater() ?  1.0F : 0.5F);
        renderIcon(matrices, 1, String.valueOf(this.client.player.getAir() / 15));
        matrices.pop();
        renderIcon(matrices, 2, String.valueOf(this.client.player.getArmor()));
        renderIcon(matrices, 3, String.valueOf(MathHelper.floor(this.client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))));

        RenderSystem.setShaderTexture(0, DETAIL);
        for (int i = 0; i < 4; i++) {
            DrawableHelper.drawTexture(
                    matrices,
                    ICONS_WIDGET_X + 34 * i,
                    ICONS_WIDGET_Y + 4,
                    0,
                    0,
                    0,
                    34,
                    10,
                    DETAIL_WIDTH,
                    DETAIL_HEIGHT
            );
        }
    }


    //drawTexture(MatrixStack matrices, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
    //
    private void renderIcon(MatrixStack matrices, int iconNumber, String data) {
            RenderSystem.setShaderTexture(0, ICONS);
            DrawableHelper.drawTexture(
                    matrices,
                    ICONS_WIDGET_X + 34 * iconNumber + 4,
                    ICONS_WIDGET_Y,
                    0,
                    11 * iconNumber,
                    0,
                    11,
                    11,
                    ICON_WIDTH,
                    ICON_HEIGHT
            );
            int dataLenght = client.textRenderer.getWidth(data);
        DrawableHelper.drawStringWithShadow(
                matrices,
                client.textRenderer,
                data,
                ICONS_WIDGET_X + iconNumber * 34 + 29 - dataLenght,
                ICONS_WIDGET_Y + 2,
                16777215);

    }
}
