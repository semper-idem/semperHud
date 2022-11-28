package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.semperidem.semperhud.client.SemperHudHelper;

import static net.semperidem.semperhud.client.SemperHudClient.MOD_ID;

public class IconsWidgetRenderer {
    private static final int ICONS_WIDGET_TEXTURE_WIDTH = 64;
    private static final int ICONS_WIDGET_TEXTURE_HEIGHT = 32;

    private static final int DETAIL_TEXTURE_WIDTH = 64;
    private static final int DETAIL_TEXTURE_HEIGHT = 32;

    private static final int ICONS_WIDGET_X = 48;
    private static final int ICONS_WIDGET_Y = 18;

    private static final float ICON_ALPHA = 0.5f;
    private static final int ICON_SPACING = 34;
    private static final int ICON_WIDTH = 11;
    private static final int ICON_HEIGHT = ICON_WIDTH;

    private static final String TEXTURE_LOCATION = "textures/gui/icons/";

    private static final Identifier ICONS = new Identifier(MOD_ID, TEXTURE_LOCATION + "icons.png");
    private static final Identifier DETAIL = new Identifier(MOD_ID, TEXTURE_LOCATION + "detail.png");


    private ClientPlayerEntity  clientPlayer;

    public IconsWidgetRenderer(ClientPlayerEntity  clientPlayer) {
        this.clientPlayer = clientPlayer;
    }


    public void renderIconsWidget(MatrixStack matrices){
        renderMountIcon(matrices);
        renderIcon(matrices, 0, String.valueOf(clientPlayer.getHungerManager().getFoodLevel()), false);
        renderIcon(matrices, 1, String.valueOf(clientPlayer.getAir() / 15), !clientPlayer.isSubmergedInWater());
        renderIcon(matrices, 2, String.valueOf(clientPlayer.getArmor()), clientPlayer.getArmor() == 0);
        renderIcon(matrices, 3, String.valueOf(getArmorToughness()), getArmorToughness() == 0);
    }

    private void renderMountIcon(MatrixStack matrices){
        if (clientPlayer.hasVehicle()){
            if (clientPlayer.getVehicle() instanceof LivingEntity) {
                float mountHealth = ((LivingEntity) clientPlayer.getVehicle()).getHealth();
                renderMountIconInner(matrices, String.valueOf((int)mountHealth));
            }
        }
    }

    private void renderMountIconInner(MatrixStack matrices, String mountHealth){
        RenderSystem.setShaderTexture(0, ICONS);
        DrawableHelper.drawTexture(
                matrices,
                ICONS_WIDGET_X + 6,
                ICONS_WIDGET_Y + 4,
                0,
                ICON_WIDTH * 4,
                0,
                ICON_WIDTH,
                ICON_HEIGHT,
                ICONS_WIDGET_TEXTURE_WIDTH,
                ICONS_WIDGET_TEXTURE_HEIGHT
        );
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, DETAIL);
        DrawableHelper.drawTexture(
                matrices,
                ICONS_WIDGET_X + 2,
                ICONS_WIDGET_Y + 4 + 4,
                0,
                0,
                0,
                ICON_SPACING,
                10,
                DETAIL_TEXTURE_WIDTH,
                DETAIL_TEXTURE_HEIGHT
        );
        SemperHudHelper.drawTextWithShadow(
                matrices,
                mountHealth,
                ICONS_WIDGET_X + 32,
                ICONS_WIDGET_Y + 2 + 4,
                1f,
                16777215,
                2
        );
    }
    private void renderIcon(MatrixStack matrices, int iconIndex, String iconData, boolean isTransparent) {
        matrices.push();
        renderIconTexture(matrices, iconIndex, isTransparent);
        renderIconTray(matrices, iconIndex);
        renderIconData(matrices, iconIndex, iconData);
        matrices.pop();
    }

    private void renderIconTexture(MatrixStack matrices, int iconIndex, boolean isTransparent){

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isTransparent ?  ICON_ALPHA : 1);
        RenderSystem.setShaderTexture(0, ICONS);
        DrawableHelper.drawTexture(
                matrices,
                ICONS_WIDGET_X + ICON_SPACING * iconIndex + 48,
                ICONS_WIDGET_Y,
                0,
                ICON_WIDTH * iconIndex,
                0,
                ICON_WIDTH,
                ICON_HEIGHT,
                ICONS_WIDGET_TEXTURE_WIDTH,
                ICONS_WIDGET_TEXTURE_HEIGHT
        );
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderIconTray(MatrixStack matrices, int iconIndex){
        RenderSystem.setShaderTexture(0, DETAIL);
        DrawableHelper.drawTexture(
                matrices,
                ICONS_WIDGET_X + ICON_SPACING * iconIndex + 44,
                ICONS_WIDGET_Y + 4,
                0,
                0,
                0,
                ICON_SPACING,
                10,
                DETAIL_TEXTURE_WIDTH,
                DETAIL_TEXTURE_HEIGHT
        );
    }

    private void renderIconData(MatrixStack matrices, int iconIndex, String iconData){
        SemperHudHelper.drawTextWithShadow(
                matrices,
                iconData,
                ICONS_WIDGET_X + iconIndex * ICON_SPACING + 73,
                ICONS_WIDGET_Y + 2,
                1f,
                16777215,
                2
        );
    }

    private int getArmorToughness(){
        return MathHelper.floor(clientPlayer.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
    }

}
