package net.semperidem.semperhud.mixin.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.item.ItemRenderer;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @ModifyArg(method="renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"), index=3)
    private int semperHud$itemCount(int color) {
        return SemperHudClient.isHudAlpha ? SemperHudClient.modifyArgb(color) : color;
    }

    @ModifyVariable(method="renderGuiQuad", at=@At("HEAD"), ordinal=7, argsOnly = true)
    private int semperHud$renderQuad(int alpha) {
        if (SemperHudClient.isHudAlpha) {
            RenderSystem.enableBlend();
            return (int) (SemperHudClient.alpha * alpha);
        }
        return alpha;
    }

}
