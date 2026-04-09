package com.autolootmace.client;

import com.autolootmace.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;

public class AutoLootMaceClient implements ClientModInitializer {

    public static KeyMapping configKey;
    public static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        ModConfig.getInstance();

        configKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.autolootmace.config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.autolootmace"
        ));

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.autolootmace.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.autolootmace"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new ConfigScreen(null));
                }
            }

            while (toggleKey.consumeClick()) {
                ModConfig config = ModConfig.getInstance();
                config.modEnabled = !config.modEnabled;
                config.save();

                if (client.player != null) {
                    String status = config.modEnabled ? "§aВКЛЮЧЁН ✓" : "§cОТКЛЮЧЁН ✗";
                    client.player.displayClientMessage(
                            Component.literal("§6[AutoLoot Mace] §fМод " + status),
                            true
                    );
                }
            }
        });

        System.out.println("[AutoLoot Mace] Мод загружен! Клавиша настроек: M | Вкл/Выкл: N");
    }
}
        System.out.println("[AutoLoot Mace] Мод загружен! Клавиша настроек: M | Вкл/Выкл: N");
    }
}
