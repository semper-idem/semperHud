package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Color;
import net.minecraft.client.MinecraftClient;
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

    private static final float SEMI_VISIBLE_ICON_ALPHA = 0.5f;
    private static final int ICON_SPACING = 34;
    private static final int ICON_WIDTH = 11;
    private static final int ICON_HEIGHT = ICON_WIDTH;

    private static final String TEXTURE_LOCATION = "textures/gui/icons/";

    private static final Identifier ICONS = new Identifier(MOD_ID, TEXTURE_LOCATION + "icons.png");
    private static final Identifier DETAIL = new Identifier(MOD_ID, TEXTURE_LOCATION + "detail.png");

    private int iconIndex = 0;
    private MatrixStack matrices;
    private ClientPlayerEntity  clientPlayer;

    private final SemperHudRenderer parent;

    public IconsWidgetRenderer(SemperHudRenderer parent) {
        this.parent = parent;
    }


    public void renderIconsWidget(MatrixStack matrices){
        this.clientPlayer = MinecraftClient.getInstance().player;
        this.matrices = matrices;
        this.iconIndex = 0;
        renderMountIcon();

        //Food
        renderIcon(clientPlayer.getHungerManager().getFoodLevel(), false, 0.3f);
        //Air
        renderIcon(clientPlayer.getAir() / 15f, !clientPlayer.isSubmergedInWater(), 0f);
        //Armor
        renderIcon(clientPlayer.getArmor(), clientPlayer.getArmor() == 0, 1f);
        //Armor Toughness
        renderIcon(getArmorToughness(), getArmorToughness() == 0, 1f);
    }

    private void renderMountIcon(){
        if (clientPlayer.hasVehicle()){
            if (clientPlayer.getVehicle() instanceof LivingEntity) {
                float mountHealth = ((LivingEntity) this.clientPlayer.getVehicle()).getHealth();
                renderMountIconInner(String.valueOf((int)mountHealth));
            }
        }
    }

    private void renderMountIconInner(String mountHealth){
        RenderSystem.setShaderTexture(0, ICONS);
        DrawableHelper.drawTexture(
                matrices,
                parent.MAIN_WIDGET_X + ICONS_WIDGET_X + 6,
                parent.MAIN_WIDGET_Y +ICONS_WIDGET_Y + 4,
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
                parent.MAIN_WIDGET_X + ICONS_WIDGET_X + 2,
                parent.MAIN_WIDGET_Y + ICONS_WIDGET_Y + 4 + 4,
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
                parent.MAIN_WIDGET_X + ICONS_WIDGET_X + 32,
                parent.MAIN_WIDGET_Y + ICONS_WIDGET_Y + 2 + 4,
                1f,
                16777215,
                2
        );
    }
    private void renderIcon(float iconData, boolean isSemiVisible, float warnLevel) {
        matrices.push();
        renderIconTexture(isSemiVisible);
        renderIconTray();
        renderIconData(iconData, warnLevel);
        matrices.pop();
        this.iconIndex++;
    }

    private void renderIconTexture(boolean isSemiVisible){

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isSemiVisible ? SEMI_VISIBLE_ICON_ALPHA : 1);
        RenderSystem.setShaderTexture(0, ICONS);
        DrawableHelper.drawTexture(
                matrices,
                parent.MAIN_WIDGET_X + ICONS_WIDGET_X + ICON_SPACING * this.iconIndex + 48,
                parent.MAIN_WIDGET_Y + ICONS_WIDGET_Y,
                0,
                ICON_WIDTH * this.iconIndex,
                0,
                ICON_WIDTH,
                ICON_HEIGHT,
                ICONS_WIDGET_TEXTURE_WIDTH,
                ICONS_WIDGET_TEXTURE_HEIGHT
        );
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderIconTray(){
        RenderSystem.setShaderTexture(0, DETAIL);
        DrawableHelper.drawTexture(
                matrices,
                parent.MAIN_WIDGET_X + ICONS_WIDGET_X + ICON_SPACING * this.iconIndex + 44,
                parent.MAIN_WIDGET_Y + ICONS_WIDGET_Y + 4,
                0,
                0,
                0,
                ICON_SPACING,
                10,
                DETAIL_TEXTURE_WIDTH,
                DETAIL_TEXTURE_HEIGHT
        );
    }

    private int getWarningColor(float resourcePercent, float bottomClamp){
        resourcePercent = SemperHudHelper.clamp(0,1, resourcePercent);
        bottomClamp = SemperHudHelper.clamp(0,1, bottomClamp);
        double x = Math.max(resourcePercent, bottomClamp);
        double clampedPercent = ((x - bottomClamp) / (1 - bottomClamp));
        int r = 255;
        int g = (int) (255 * Math.max(clampedPercent, 0.3));
        int b = (int) (255 * Math.max(clampedPercent, 0.3));
        return Color.ofRGB(r,g,b).getColor();
    }

    private void renderIconData(float iconData, float warnLevel){
        int color = warnLevel == 1 ? 0xFFFFFF : getWarningColor(iconData / 20f, warnLevel);
        SemperHudHelper.drawTextWithShadow(
                matrices,
                String.valueOf((int)iconData),
                parent.MAIN_WIDGET_X + ICONS_WIDGET_X + this.iconIndex * ICON_SPACING + 73,
                parent.MAIN_WIDGET_X + ICONS_WIDGET_Y + 2,
                1f,
                color,
                2
        );
    }

    private int getArmorToughness(){
        return MathHelper.floor(clientPlayer.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
    }

}
