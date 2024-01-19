package safro.auroral.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.auroral.client.ClientEvents;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("TAIL"))
    private void renderAuroralHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();
        if (!this.client.options.hudHidden) {
            RenderSystem.enableBlend();
            ClientEvents.renderHud(context, this.client.textRenderer, player, this.scaledWidth, this.scaledHeight);
            RenderSystem.disableBlend();
        }
    }
}
