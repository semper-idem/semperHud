package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.Matrix4f;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @Inject(method = "draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZIIZ)I", at=@At("HEAD"), cancellable = true)
    private void semperHud$draw(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean rightToLeft, CallbackInfoReturnable<Integer> cir) {
        if (SemperHudClient.isHudRendering && SemperHudClient.alpha <= 0.015) {
            cir.setReturnValue(0);
        }
    }
}
