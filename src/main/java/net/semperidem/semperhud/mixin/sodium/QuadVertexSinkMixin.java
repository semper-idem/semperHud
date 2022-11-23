package net.semperidem.semperhud.mixin.sodium;

import me.jellysquid.mods.sodium.client.model.vertex.formats.quad.QuadVertexSink;
import net.semperidem.semperhud.client.SemperHudClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = QuadVertexSink.class, remap = false)
public interface QuadVertexSinkMixin {
	@ModifyVariable(method = "writeQuad(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFFIFFIII)V", at=@At("HEAD"), ordinal = 0, argsOnly = true)
	default int autoHud$alpha(int color) {
		return (SemperHudClient.isHudAlpha ? SemperHudClient.modifyArgb(color) : color);
	}
}
