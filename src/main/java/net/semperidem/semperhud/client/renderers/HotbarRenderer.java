package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;

import static net.minecraft.client.gui.widget.ClickableWidget.WIDGETS_TEXTURE;

public class HotbarRenderer {

    private static final Identifier WIDGET = new Identifier(SemperHudClient.getModId(),  "textures/gui/hotbar/widgets.png");

    private MinecraftClient client;

    public HotbarRenderer(MinecraftClient client) {
        assert client.player != null;
        this.client = client;
    }

    private void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed) {
        if (!stack.isEmpty()) {
            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            float f = (float)stack.getBobbingAnimationTime() - tickDelta;
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                matrixStack.push();
                matrixStack.translate((double)(x + 8), (double)(y + 12), 0.0);
                matrixStack.scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                matrixStack.translate((double)(-(x + 8)), (double)(-(y + 12)), 0.0);
                RenderSystem.applyModelViewMatrix();
            }

            this.client.getItemRenderer().renderInGuiWithOverrides(player, stack, x, y, seed);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                matrixStack.pop();
                RenderSystem.applyModelViewMatrix();
            }

            this.client.getItemRenderer().renderGuiItemOverlay(this.client.textRenderer, stack, x, y);
        }
    }

    public void renderHotbar(float tickDelta, MatrixStack matrices) {
        PlayerEntity player = this.client.player;
        if (player != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGET);
            int y0 = this.client.getWindow().getScaledHeight() / 2 - 91;
            DrawableHelper.drawTexture(matrices, 0, y0, 0,0,22,182 ,256,256);
            DrawableHelper.drawTexture(matrices, 1, y0 - 1 + player.getInventory().selectedSlot * 20, 23,0,22,22 ,256,256);
            for(int n = 0; n < 9; ++n) {
                int x = 3;
                int y = y0 + n * 20 + 3;
                this.renderHotbarItem(x, y, tickDelta, player, (ItemStack)player.getInventory().main.get(n), n);
            }

        }
//        PlayerEntity playerEntity = this.client.player;
//        if (playerEntity != null) {
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            RenderSystem.setShader(GameRenderer::getPositionTexShader);
//            RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
//            ItemStack itemStack = playerEntity.getOffHandStack();
//            Arm arm = playerEntity.getMainArm().getOpposite();
//            int i = this.scaledWidth / 2;
//            int j = this.getZOffset();
//            int k = true;
//            int l = true;
//            DrawableHelper.setZOffset(-90);
//            this.drawTexture(matrices, i - 91, this.scaledHeight - 22, 0, 0, 182, 22);
//            this.drawTexture(matrices, i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
//            if (!itemStack.isEmpty()) {
//                if (arm == Arm.LEFT) {
//                    this.drawTexture(matrices, i - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
//                } else {
//                    this.drawTexture(matrices, i + 91, this.scaledHeight - 23, 53, 22, 29, 24);
//                }
//            }
//
//            this.setZOffset(j);
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
//            int m = 1;
//
//            int n;
//            int o;
//            int p;
//            for(n = 0; n < 9; ++n) {
//                o = i - 90 + n * 20 + 2;
//                p = this.scaledHeight - 16 - 3;
//                this.renderHotbarItem(o, p, tickDelta, playerEntity, (ItemStack)playerEntity.getInventory().main.get(n), m++);
//            }
//
//            if (!itemStack.isEmpty()) {
//                n = this.scaledHeight - 16 - 3;
//                if (arm == Arm.LEFT) {
//                    this.renderHotbarItem(i - 91 - 26, n, tickDelta, playerEntity, itemStack, m++);
//                } else {
//                    this.renderHotbarItem(i + 91 + 10, n, tickDelta, playerEntity, itemStack, m++);
//                }
//            }
//
//            if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
//                float f = this.client.player.getAttackCooldownProgress(0.0F);
//                if (f < 1.0F) {
//                    o = this.scaledHeight - 20;
//                    p = i + 91 + 6;
//                    if (arm == Arm.RIGHT) {
//                        p = i - 91 - 22;
//                    }
//
//                    RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
//                    int q = (int)(f * 19.0F);
//                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                    this.drawTexture(matrices, p, o, 0, 94, 18, 18);
//                    this.drawTexture(matrices, p, o + 18 - q, 18, 112 - q, 18, q);
//                }
//            }
//
//            RenderSystem.disableBlend();
//        }
    }

}