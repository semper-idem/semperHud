package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.render.VertexConsumer;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin {
    @ModifyArg(
            method = "quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;[FFFF[IIZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;vertex(FFFFFFFFFIIFFF)V"),
            index = 6
    )
    default float semperHud$quad(float alpha) {
        // Transparency level
        return (SemperHudClient.isHudAlpha ? SemperHudClient.alpha * alpha : alpha);
    }
}
