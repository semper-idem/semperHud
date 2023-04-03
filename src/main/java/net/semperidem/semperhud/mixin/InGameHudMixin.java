package net.semperidem.semperhud.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void semperHud$injectRenderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        if (SemperHudClient.config.semperHud){
            ci.cancel();
        }
    }
    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    private void semperHud$injectRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!SemperHudClient.config.semperHud){
            return;
        }
        SemperHudClient.getInstance().render(matrices);

        if (!SemperHudClient.config.semperHotbar) {
            return;
        }
        SemperHudClient.isHudAlpha = SemperHudClient.alpha != 1;
        SemperHudClient.getInstance().renderHotbar(tickDelta, matrices);
        SemperHudClient.isHudAlpha = false;
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void semperHud$injectRenderExperienceBars(MatrixStack matrices, int x, CallbackInfo ci) {
        if (SemperHudClient.config.semperHud){
            ci.cancel();
        }

    }
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void semperHud$injectRenderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        if (SemperHudClient.config.semperHotbar) {
            ci.cancel();
        }
    }

    @Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
    private void semperHud$injectRenderJumpBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if (SemperHudClient.config.semperHud){
            ci.cancel();
        }
    }


    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    private void semperHud$injectRenderMountHealth(MatrixStack matrices, CallbackInfo ci) {
        if (SemperHudClient.config.semperHud){
            ci.cancel();
        }
    }



    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void SemperHud$injectStartRenderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
        if (SemperHudClient.config.semperHotbar) {
            ci.cancel();
        }
    }
}
