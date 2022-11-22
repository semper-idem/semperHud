package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.model.ModelPart;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ModelPart.class)
public class ModelPartMixin {
    @ModifyVariable(method="render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", at=@At("HEAD"), ordinal = 3, argsOnly = true)
    public float autoHud$render(float alpha) {
        return SemperHudClient.isHudRendering ? SemperHudClient.alpha * alpha : alpha;
    }
}
