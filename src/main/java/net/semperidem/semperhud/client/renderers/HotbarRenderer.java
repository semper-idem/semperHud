package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;

import static net.semperidem.semperhud.client.SemperHudClient.MOD_ID;

public class HotbarRenderer {

    private static final String TEXTURE_LOCATION = "textures/gui/hotbar/";
    private static final Identifier WIDGET = new Identifier(MOD_ID,  TEXTURE_LOCATION + "widgets.png");
    private static final Identifier WIDGET_DARK = new Identifier(MOD_ID,  TEXTURE_LOCATION + "widgets_dark.png");
    private static final Identifier WIDGET_DARK_NUMBERED = new Identifier(MOD_ID,  TEXTURE_LOCATION + "widgets_dark_numbered.png");

    private ClientPlayerEntity clientPlayer;


    private long selectedSlotChangedTS;
    private int lastRenderSelectedSlot;
    private final long ANIMATION_TIME = 5000;
    private final long ANIMATION_WAIT = 1000;

    public HotbarRenderer(ClientPlayerEntity clientPlayer) {
        this.clientPlayer = clientPlayer;
        lastRenderSelectedSlot = clientPlayer.getInventory().selectedSlot;
    }

    private void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed) {

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,SemperHudClient.alpha);
        RenderSystem.applyModelViewMatrix();

        if (!stack.isEmpty()) {
            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            float f = (float)stack.getBobbingAnimationTime() - tickDelta;
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                matrixStack.push();
                matrixStack.translate(x + 8, y + 12, 0.0);
                matrixStack.scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                matrixStack.translate(-(x + 8), -(y + 12), 0.0);
                RenderSystem.applyModelViewMatrix();
            }
            MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(player, stack, x, y, seed);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                matrixStack.pop();
                RenderSystem.applyModelViewMatrix();
            }

            MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, stack, x, y);
        }
    }
        public void renderHotbar(float tickDelta, MatrixStack matrices) {
            long ts = System.currentTimeMillis();
            if (lastRenderSelectedSlot != clientPlayer.getInventory().selectedSlot) {
                selectedSlotChangedTS = ts;
                lastRenderSelectedSlot = clientPlayer.getInventory().selectedSlot;
            }

            long timeSinceSelectedSlotChange = ts - selectedSlotChangedTS;
            SemperHudClient.alpha = timeSinceSelectedSlotChange < ANIMATION_WAIT ? 1 :
                    timeSinceSelectedSlotChange < ANIMATION_WAIT + ANIMATION_TIME ?
                           0.5f + (0.5f * ((ANIMATION_TIME + ANIMATION_WAIT - timeSinceSelectedSlotChange) / (1f *(ANIMATION_TIME + ANIMATION_WAIT))) ) : 0.5F;
            matrices.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, SemperHudClient.alpha);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGET_DARK_NUMBERED);
            int y0 = (MinecraftClient.getInstance().getWindow().getScaledHeight() / (2)  - 91);
            DrawableHelper.drawTexture(matrices, 1,  (y0), 0,0,22,182 ,256,256);
            DrawableHelper.drawTexture(matrices, 1, (y0 - 1 + clientPlayer.getInventory().selectedSlot * 20), 23,0,22,22 ,256,256);
            for(int n = 0; n < 9; ++n) {
                int x = 3;
                int y = y0 + n * 20 + 3;
                this.renderHotbarItem(x, y, tickDelta, clientPlayer, clientPlayer.getInventory().main.get(n), n);
            }

            matrices.pop();

    }

}
