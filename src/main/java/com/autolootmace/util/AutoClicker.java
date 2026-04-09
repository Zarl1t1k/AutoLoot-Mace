package com.autolootmace.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;

import java.util.List;

public class AutoClicker {

    private static long lastClickTime = 0;

    public static void processScreen(AbstractContainerScreen<?> screen, int delayMs) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < delayMs) return;

        List<Integer> targetSlots = LootDetector.findTargetSlots(screen);
        if (targetSlots.isEmpty()) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gameMode == null || minecraft.player == null) return;

        int slotIndex = targetSlots.get(0);

        minecraft.gameMode.handleInventoryMouseClick(
                screen.getMenu().containerId,
                slotIndex,
                0,
                ClickType.QUICK_MOVE,
                minecraft.player
        );

        lastClickTime = now;
    }
}
