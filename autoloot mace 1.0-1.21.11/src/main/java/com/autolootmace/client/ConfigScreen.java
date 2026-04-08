package com.autolootmace.client;

import com.autolootmace.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final ModConfig config;
    private final List<ButtonWidget> toggleButtons = new ArrayList<>();

    private static final int COLOR_PANEL  = 0xFF16213E;
    private static final int COLOR_ACCENT = 0xFF0F3460;
    private static final int COLOR_GREEN  = 0xFF00D4AA;

    public ConfigScreen(Screen parent) {
        super(Text.literal("§6⚙ §lAutoLoot Mace §r§7- Настройки"));
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

        // Кнопка включения/выключения мода
        ButtonWidget enableButton = ButtonWidget.builder(
                getEnableText(),
                btn -> {
                    config.modEnabled = !config.modEnabled;
                    config.save();
                    btn.setMessage(getEnableText());
                }
        ).dimensions(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build();
        this.addDrawableChild(enableButton);

        int lootStartY = startY + spacing + 20;

        int i = 0;
        for (Map.Entry<String, String> entry : ModConfig.AVAILABLE_ITEMS.entrySet()) {
            final String itemId   = entry.getKey();
            final String itemName = entry.getValue();
            int y = lootStartY + i * spacing;

            ButtonWidget toggleBtn = ButtonWidget.builder(
                    getItemText(itemId, itemName),
                    btn -> {
                        boolean newState = !config.isItemEnabled(itemId);
                        config.setItemEnabled(itemId, newState);
                        config.save();
                        btn.setMessage(getItemText(itemId, itemName));
                    }
            ).dimensions(centerX - buttonWidth / 2, y, buttonWidth, buttonHeight)
             .tooltip(Tooltip.of(Text.literal("Нажмите, чтобы " + (config.isItemEnabled(itemId) ? "отключить" : "включить") + " авто-выбивание")))
             .build();

            this.addDrawableChild(toggleBtn);
            toggleButtons.add(toggleBtn);
            i++;
        }

        int bottomY = lootStartY + i * spacing + 20;

        // Слайдер задержки
        SliderWidget delaySlider = new SliderWidget(
                centerX - buttonWidth / 2, bottomY, buttonWidth, buttonHeight,
                Text.literal("Задержка: " + config.clickDelayMs + " мс"),
                (config.clickDelayMs - 50.0) / 950.0
        ) {
            @Override
            protected void updateMessage() {
                int ms = (int)(50 + value * 950);
                setMessage(Text.literal("§7Задержка клика: §e" + ms + " мс"));
            }

            @Override
            protected void applyValue() {
                config.clickDelayMs = (int)(50 + value * 950);
                config.save();
            }
        };
        this.addDrawableChild(delaySlider);

        ButtonWidget closeButton = ButtonWidget.builder(
                Text.literal("§7◄ Закрыть"),
                btn -> this.close()
        ).dimensions(centerX - 60, bottomY + 40, 120, 20).build();
        this.addDrawableChild(closeButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int centerX   = this.width / 2;
        int panelWidth = 320;
        int panelHeight = this.height - 40;
        int panelX = centerX - panelWidth / 2;
        int panelY = 20;

        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, COLOR_PANEL);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + 50, COLOR_ACCENT);
        context.fill(panelX, panelY + 50, panelX + panelWidth, panelY + 52, COLOR_GREEN);

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("⚡ AUTOLOOT MACE").styled(s -> s.withColor(0xFFD700).withBold(true)),
                centerX, panelY + 12, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Авто-выбивание лута из хранилищ").styled(s -> s.withColor(0xFFAAAAAA)),
                centerX, panelY + 28, 0xFFFFFFFF);

        context.drawTextWithShadow(this.textRenderer,
                Text.literal("━━ НАСТРОЙКА ЛУТА ━━").styled(s -> s.withColor(0xFF00D4AA)),
                panelX + 20, 110, 0xFFFFFFFF);

        context.drawTextWithShadow(this.textRenderer,
                Text.literal("━━ ПАРАМЕТРЫ ━━").styled(s -> s.withColor(0xFF00D4AA)),
                panelX + 20, 80 + 30 + 20 + (ModConfig.AVAILABLE_ITEMS.size() * 30) + 10, 0xFFFFFFFF);

        String statusText = config.modEnabled ? "§aВКЛЮЧЁН ✓" : "§cОТКЛЮЧЁН ✗";
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Статус мода: " + statusText),
                centerX, panelY + panelHeight - 20, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    private Text getEnableText() {
        if (config.modEnabled) {
            return Text.literal("§a● МОД ВКЛЮЧЁН  §7[нажать для отключения]");
        } else {
            return Text.literal("§c○ МОД ВЫКЛЮЧЕН §7[нажать для включения]");
        }
    }

    private Text getItemText(String itemId, String itemName) {
        boolean enabled = config.isItemEnabled(itemId);
        String emoji = getItemEmoji(itemId);
        if (enabled) {
            return Text.literal("§a✓ §f" + emoji + " " + itemName + " §7[авто-выбивание ВКЛ]");
        } else {
            return Text.literal("§c✗ §7" + emoji + " " + itemName + " [авто-выбивание ВЫКЛ]");
        }
    }

    private String getItemEmoji(String itemId) {
        return switch (itemId) {
            case "minecraft:breeze_rod"     -> "🌪";
            case "minecraft:ominous_bottle" -> "🧪";
            case "minecraft:mace"           -> "🔨";
            case "minecraft:wind_charge"    -> "💨";
            case "minecraft:heavy_core"     -> "⚙";
            default -> "◆";
        };
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
