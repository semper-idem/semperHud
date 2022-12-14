package net.semperidem.semperhud.mixin.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestBlockEntityRendererMixin {

    @ModifyArg(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;)Lnet/minecraft/client/render/VertexConsumer;")
    )
    private Function<Identifier, RenderLayer> semperHud$getVertexConsumer(Function<Identifier, RenderLayer> layerFactory) {
        return SemperHudClient.isHudAlpha ? RenderLayer::getEntityTranslucentCull : layerFactory;
    }

}
