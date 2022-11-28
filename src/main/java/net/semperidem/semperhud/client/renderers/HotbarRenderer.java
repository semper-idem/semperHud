package net.semperidem.semperhud.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;
import net.semperidem.semperhud.client.SemperHudHelper;

import static net.semperidem.semperhud.client.SemperHudClient.MOD_ID;

public class HotbarRenderer {

    private static final String TEXTURE_LOCATION = "textures/gui/hotbar/";
    private static final Identifier WIDGET = new Identifier(MOD_ID,  TEXTURE_LOCATION + "widgets.png");
    private static final Identifier WIDGET_DARK = new Identifier(MOD_ID,  TEXTURE_LOCATION + "widgets_dark.png");
    private static final Identifier WIDGET_DARK_NUMBERED = new Identifier(MOD_ID,  TEXTURE_LOCATION + "widgets_dark_numbered.png");


    private final long ANIMATION_PAUSE = 5000;
    private final long ANIMATION_DURATION = ANIMATION_PAUSE + 2000;
    private final long TEXT_FADE_PAUSE = 2000;
    private final long TEXT_FADE_DURATION = TEXT_FADE_PAUSE + 1000;
    private final float MIN_ALPHA = 0.5f;


    private ClientPlayerEntity clientPlayer;


    private long selectedSlotChangedTS;
    private int lastRenderSelectedSlot;

    public HotbarRenderer(ClientPlayerEntity clientPlayer) {
        this.clientPlayer = clientPlayer;
        lastRenderSelectedSlot = clientPlayer.getInventory().selectedSlot;
    }

    private void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed) {
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


            SemperHudClient.alpha = getAnimationAlpha(ts - selectedSlotChangedTS);

            matrices.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, SemperHudClient.alpha);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGET_DARK_NUMBERED);
            int y0 = (MinecraftClient.getInstance().getWindow().getScaledHeight() / (2)  - 91);
            DrawableHelper.drawTexture(matrices, 1,  (y0), 0,0,22,182 ,256,256);
            DrawableHelper.drawTexture(matrices, 1, (y0 - 1 + clientPlayer.getInventory().selectedSlot * 20), 23,0,22,22 ,256,256);
            ItemStack currentStack = clientPlayer.getMainHandStack();
            float textAlpha = getTextAlpha(ts - selectedSlotChangedTS);
            if (!currentStack.isEmpty() && textAlpha > .15) {
                SemperHudHelper.drawTextWithShadow(
                        matrices,
                        (Text.empty().append(currentStack.getName()).formatted(currentStack.getRarity().formatting)).getString(),
                        25,
                        y0 + 6 + clientPlayer.getInventory().selectedSlot * 20,
                        1,
                        16777215,
                        0,
                        textAlpha
                );
            }
            for(int n = 0; n < 9; ++n) {
                int x = 3;
                int y = y0 + n * 20 + 3;
                this.renderHotbarItem(x, y, tickDelta, clientPlayer, clientPlayer.getInventory().main.get(n), n);
            }

            matrices.pop();

    }

    private float getAnimationAlpha(long timeSinceSelectedSlotChange){
        boolean isAnimationPaused = timeSinceSelectedSlotChange - ANIMATION_PAUSE < 0;
        boolean isAnimationOver = (timeSinceSelectedSlotChange - ANIMATION_DURATION) < 0;
        return isAnimationPaused ? 1 :
        !isAnimationOver ? MIN_ALPHA :
                MIN_ALPHA + (
                        (1 - MIN_ALPHA) * (
                                (ANIMATION_DURATION - timeSinceSelectedSlotChange) / (1f * ANIMATION_DURATION - ANIMATION_PAUSE)
                        )
                );
    }

    private float getTextAlpha(long timeSinceSelectedSlotChange){
        boolean isAnimationPaused = timeSinceSelectedSlotChange - TEXT_FADE_PAUSE < 0;
        boolean isAnimationOver = (timeSinceSelectedSlotChange - TEXT_FADE_DURATION) > 0;
        return isAnimationPaused ? 1 :
                isAnimationOver ? 0 :
                        0.15f + (
                                (0.85f) * (
                                        (TEXT_FADE_DURATION - timeSinceSelectedSlotChange) / (1f * TEXT_FADE_DURATION - TEXT_FADE_PAUSE)
                                )
                        );
    }

}
