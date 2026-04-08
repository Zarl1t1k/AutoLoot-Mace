package com.autolootmace.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;

public class AutoClicker {

    private static long lastClickTime = 0;

    public static void processScreen(HandledScreen<?> screen, int delayMs) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < delayMs) return;

        List<Integer> targetSlots = LootDetector.findTargetSlots(screen);
        if (targetSlots.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null || client.player == null) return;

        int slotIndex = targetSlots.get(0);

        client.interactionManager.clickSlot(
                screen.getScreenHandler().syncId,
                slotIndex,
                0,
                SlotActionType.QUICK_MOVE,
                client.player
        );

        lastClickTime = now;
    }
}
