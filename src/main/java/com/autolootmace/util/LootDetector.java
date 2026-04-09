package com.autolootmace.util;

import com.autolootmace.config.ModConfig;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.List;

public class LootDetector {

    public static List<Integer> findTargetSlots(AbstractContainerScreen<?> screen) {
        List<Integer> targetSlots = new ArrayList<>();
        ModConfig config = ModConfig.getInstance();

        if (!config.modEnabled) return targetSlots;

        AbstractContainerMenu menu = screen.getMenu();
        List<Slot> slots = menu.slots;

        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            if (!slot.hasItem()) continue;

            ItemStack stack = slot.getItem();
            String itemId = getItemId(stack);

            if (itemId != null && config.isItemEnabled(itemId)) {
                targetSlots.add(i);
            }
        }

        return targetSlots;
    }

    public static String getItemId(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    public static boolean isContainerScreen(AbstractContainerScreen<?> screen) {
        return screen.getMenu().slots.size() > 36;
    }
}
