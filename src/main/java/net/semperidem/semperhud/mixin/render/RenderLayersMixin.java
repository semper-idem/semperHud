package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {
    // BlockItem transparency support
    @Redirect(method="getEntityBlockLayer", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/TexturedRenderLayers;getEntityCutout()Lnet/minecraft/client/render/RenderLayer;"))
    private static RenderLayer semperHud$getEntityCutout() {
        return (SemperHudClient.isHudRendering ? TexturedRenderLayers.getItemEntityTranslucentCull() : TexturedRenderLayers.getEntityCutout());
    }
}
