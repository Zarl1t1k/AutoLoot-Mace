package com.autolootmace.util;

import com.autolootmace.config.ModConfig;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;

public class LootDetector {

    public static List<Integer> findTargetSlots(HandledScreen<?> screen) {
        List<Integer> targetSlots = new ArrayList<>();
        ModConfig config = ModConfig.getInstance();

        if (!config.modEnabled) return targetSlots;

        ScreenHandler handler = screen.getScreenHandler();
        List<Slot> slots = handler.slots;

        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            if (!slot.hasStack()) continue;

            ItemStack stack = slot.getStack();
            String itemId = getItemId(stack);

            if (itemId != null && config.isItemEnabled(itemId)) {
                targetSlots.add(i);
            }
        }

        return targetSlots;
    }

    public static String getItemId(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return Registries.ITEM.getId(stack.getItem()).toString();
    }

    public static boolean isContainerScreen(HandledScreen<?> screen) {
        return screen.getScreenHandler().slots.size() > 36;
    }
}
