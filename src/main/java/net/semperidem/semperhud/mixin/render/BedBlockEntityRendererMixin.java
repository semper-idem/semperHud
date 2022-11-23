package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BedBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(BedBlockEntityRenderer.class)
public class BedBlockEntityRendererMixin {
    @ModifyArg(
            method = "renderPart",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;)Lnet/minecraft/client/render/VertexConsumer;")
    )
    private Function<Identifier, RenderLayer> semperHud$getVertexConsumer(Function<Identifier, RenderLayer> layerFactory) {
        return SemperHudClient.isHudAlpha ? RenderLayer::getEntityTranslucentCull : layerFactory;
    }
}
