package com.autolootmace.client;

import com.autolootmace.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AutoLootMaceClient implements ClientModInitializer {

    public static KeyBinding configKey;
    public static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        // Загружаем конфиг
        ModConfig.getInstance();

        // Регистрируем клавишу открытия настроек (по умолчанию M)
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autolootmace.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.autolootmace"
        ));

        // Регистрируем клавишу быстрого вкл/выкл (по умолчанию N)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autolootmace.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.autolootmace"
        ));

        // Обработка нажатий клавиш
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ConfigScreen(null));
                }
            }

            while (toggleKey.wasPressed()) {
                ModConfig config = ModConfig.getInstance();
                config.modEnabled = !config.modEnabled;
                config.save();

                if (client.player != null) {
                    String status = config.modEnabled ? "§aВКЛЮЧЁН ✓" : "§cОТКЛЮЧЁН ✗";
                    client.player.sendMessage(
                            net.minecraft.text.Text.literal("§6[AutoLoot Mace] §fМод " + status),
                            true
                    );
                }
            }
        });

        System.out.println("[AutoLoot Mace] Мод загружен! Клавиша настроек: M | Вкл/Выкл: N");
    }
}
