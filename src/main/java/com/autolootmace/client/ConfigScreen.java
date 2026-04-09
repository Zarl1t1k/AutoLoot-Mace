package com.autolootmace.client;

import com.autolootmace.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final ModConfig config;
    private final List<Button> toggleButtons = new ArrayList<>();

    private static final int COLOR_PANEL  = 0xFF16213E;
    private static final int COLOR_ACCENT = 0xFF0F3460;
    private static final int COLOR_GREEN  = 0xFF00D4AA;

    public ConfigScreen(Screen parent) {
        super(Component.literal("§6⚙ §lAutoLoot Mace §r§7- Настройки"));
        this.parent = parent;
        this.config = ModConfig.getInstance();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 80;
        int buttonWidth = 280;
        int buttonHeight = 24;
        int spacing = 30;

        Button enableButton = Button.builder(
                getEnableText(),
                btn -> {
                    config.modEnabled = !config.modEnabled;
                    config.save();
                    btn.setMessage(getEnableText());
                }
        ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build();
        this.addRenderableWidget(enableButton);

        int lootStartY = startY + spacing + 20;

        int i = 0;
        for (Map.Entry<String, String> entry : ModConfig.AVAILABLE_ITEMS.entrySet()) {
            final String itemId   = entry.getKey();
            final String itemName = entry.getValue();
            int y = lootStartY + i * spacing;

            Button toggleBtn = Button.builder(
                    getItemText(itemId, itemName),
                    btn -> {
                        boolean newState = !config.isItemEnabled(itemId);
                        config.setItemEnabled(itemId, newState);
                        config.save();
                        btn.setMessage(getItemText(itemId, itemName));
                    }
            ).bounds(centerX - buttonWidth / 2, y, buttonWidth, buttonHeight)
             .tooltip(Tooltip.create(Component.literal("Нажмите для переключения авто-выбивания")))
             .build();

            this.addRenderableWidget(toggleBtn);
            toggleButtons.add(toggleBtn);
            i++;
        }

        int bottomY = lootStartY + i * spacing + 20;

        Button delayDown = Button.builder(
                Component.literal("§e- §7Задержка: §e" + config.clickDelayMs + " мс"),
                btn -> {
                    config.clickDelayMs = Math.max(50, config.clickDelayMs - 50);
                    config.save();
                    updateDelayButtons();
                }
        ).bounds(centerX - buttonWidth / 2, bottomY, buttonWidth / 2 - 2, buttonHeight).build();
        this.addRenderableWidget(delayDown);

        Button delayUp = Button.builder(
                Component.literal("§e+"),
                btn -> {
                    config.clickDelayMs = Math.min(1000, config.clickDelayMs + 50);
                    config.save();
                    updateDelayButtons();
                }
        ).bounds(centerX + 2, bottomY, buttonWidth / 2 - 2, buttonHeight).build();
        this.addRenderableWidget(delayUp);

        Button closeButton = Button.builder(
                Component.literal("§7◄ Закрыть"),
                btn -> this.onClose()
        ).bounds(centerX - 60, bottomY + 40, 120, 20).build();
        this.addRenderableWidget(closeButton);
    }

    private void updateDelayButtons() {
        this.clearWidgets();
        this.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int centerX    = this.width / 2;
        int panelWidth  = 320;
        int panelHeight = this.height - 40;
        int panelX = centerX - panelWidth / 2;
        int panelY = 20;

        guiGraphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, COLOR_PANEL);
        guiGraphics.fill(panelX, panelY, panelX + panelWidth, panelY + 50, COLOR_ACCENT);
        guiGraphics.fill(panelX, panelY + 50, panelX + panelWidth, panelY + 52, COLOR_GREEN);

        guiGraphics.drawCenteredString(this.font,
                Component.literal("⚡ AUTOLOOT MACE"),
                centerX, panelY + 12, 0xFFD700);
        guiGraphics.drawCenteredString(this.font,
                Component.literal("Авто-выбивание лута из хранилищ"),
                centerX, panelY + 28, 0xAAAAAA);

        guiGraphics.drawString(this.font,
                Component.literal("━━ НАСТРОЙКА ЛУТА ━━"),
                panelX + 20, 110, 0x00D4AA);

        guiGraphics.drawString(this.font,
                Component.literal("━━ ЗАДЕРЖКА КЛИКА ━━"),
                panelX + 20, 80 + 30 + 20 + (ModConfig.AVAILABLE_ITEMS.size() * 30) + 10, 0x00D4AA);

        String statusText = config.modEnabled ? "§aВКЛЮЧЁН ✓" : "§cОТКЛЮЧЁН ✗";
        guiGraphics.drawCenteredString(this.font,
                Component.literal("Статус мода: " + statusText),
                centerX, panelY + panelHeight - 20, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private Component getEnableText() {
        if (config.modEnabled) {
            return Component.literal("§a● МОД ВКЛЮЧЁН  §7[нажать для отключения]");
        } else {
            return Component.literal("§c○ МОД ВЫКЛЮЧЕН §7[нажать для включения]");
        }
    }

    private Component getItemText(String itemId, String itemName) {
        boolean enabled = config.isItemEnabled(itemId);
        String emoji = getItemEmoji(itemId);
        if (enabled) {
            return Component.literal("§a✓ §f" + emoji + " " + itemName + " §7[ВКЛ]");
        } else {
            return Component.literal("§c✗ §7" + emoji + " " + itemName + " [ВЫКЛ]");
        }
    }

    private String getItemEmoji(String itemId) {
        return switch (itemId) {
            case "minecraft:breeze_rod"             -> "🌪";
            case "minecraft:ominous_bottle"         -> "🧪";
            case "minecraft:mace"                   -> "🔨";
            case "minecraft:wind_charge"            -> "💨";
            case "minecraft:heavy_core"             -> "⚙";
            case "minecraft:enchanted_golden_apple" -> "🍎";
            default -> "◆";
        };
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
