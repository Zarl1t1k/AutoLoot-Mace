package com.autolootmace.mixin;

import com.autolootmace.config.ModConfig;
import com.autolootmace.util.AutoClicker;
import com.autolootmace.util.LootDetector;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        HandledScreen<?> self = (HandledScreen<?>) (Object) this;
        ModConfig config = ModConfig.getInstance();

        if (!config.modEnabled) return;
        if (!LootDetector.isContainerScreen(self)) return;

        AutoClicker.processScreen(self, config.clickDelayMs);
    }
}
