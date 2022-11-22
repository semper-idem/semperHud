package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(BannerBlockEntityRenderer.class)
public class BannerBlockEntityRendererMixin {
    @ModifyArg(
            method = "render(Lnet/minecraft/block/entity/BannerBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;)Lnet/minecraft/client/render/VertexConsumer;")
    )
    private Function<Identifier, RenderLayer> semperHud$getVertexConsumer(Function<Identifier, RenderLayer> layerFactory) {
        return SemperHudClient.isHudRendering ? RenderLayer::getEntityTranslucent : layerFactory;
    }

    @ModifyArg(
            method = "renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;Z)Lnet/minecraft/client/render/VertexConsumer;")
    )
    private static Function<Identifier, RenderLayer> semperHud$getRenderLayer(Function<Identifier, RenderLayer> layerFactory) {
        return SemperHudClient.isHudRendering ? RenderLayer::getEntityTranslucent : layerFactory;
    }
}
