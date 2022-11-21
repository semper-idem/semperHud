package net.semperidem.semperhud.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.semperhud.client.SemperHudClient;
import net.semperidem.semperhud.client.renderers.SemperHudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {


    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void overrideRenderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        SemperHudClient.getInstance().renderStatusWidget(matrices);
        //ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void overrideRenderExperienceBars(MatrixStack matrices, int x, CallbackInfo ci) {
        SemperHudClient.getInstance().renderExperienceWidget(matrices);
        //ci.cancel();
    }
}
