package com.autolootmace.mixin;

import com.autolootmace.config.ModConfig;
import com.autolootmace.util.AutoClicker;
import com.autolootmace.util.LootDetector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class HandledScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        ModConfig config = ModConfig.getInstance();

        if (!config.modEnabled) return;
        if (!LootDetector.isContainerScreen(self)) return;

        AutoClicker.processScreen(self, config.clickDelayMs);
    }
}
