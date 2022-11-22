package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkullBlockEntityRenderer.class)
public class SkullBlockEntityRendererMixin {
    @Redirect(method = "getRenderLayer", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private static RenderLayer semperHud$getEntityCutoutNoCull(Identifier texture) {
        return SemperHudClient.isHudRendering ? RenderLayer.getEntityTranslucent(texture) : RenderLayer.getEntityCutoutNoCull(texture);
    }
    @Redirect(method = "getRenderLayer", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityCutoutNoCullZOffset(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private static RenderLayer semperHud$getEntityCutoutNoCullZOffset(Identifier texture) {
        return SemperHudClient.isHudRendering ? RenderLayer.getEntityTranslucent(texture) : RenderLayer.getEntityCutoutNoCullZOffset(texture);
    }
}
