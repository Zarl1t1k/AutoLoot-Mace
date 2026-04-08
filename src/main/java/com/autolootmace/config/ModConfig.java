package com.autolootmace.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("autolootmace.json");

    private static ModConfig instance;

    public Map<String, Boolean> lootSettings = new HashMap<>();
    public boolean modEnabled = true;
    public int clickDelayMs = 200;

    public static final Map<String, String> AVAILABLE_ITEMS = new HashMap<>();

    static {
        AVAILABLE_ITEMS.put("minecraft:mace",           "Булава");
        AVAILABLE_ITEMS.put("minecraft:breeze_rod",     "Навершие булавы (Стержень бриза)");
        AVAILABLE_ITEMS.put("minecraft:ominous_bottle", "Зловещая бутылочка");
        AVAILABLE_ITEMS.put("minecraft:wind_charge",    "Заряд ветра");
        AVAILABLE_ITEMS.put("minecraft:heavy_core",     "Тяжёлое ядро");
    }

    public static ModConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static ModConfig load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                ModConfig config = GSON.fromJson(reader, ModConfig.class);
                if (config != null) {
                    config.fillDefaults();
                    return config;
                }
            } catch (IOException e) {
                System.err.println("[AutoLoot Mace] Не удалось загрузить конфиг: " + e.getMessage());
            }
        }
        ModConfig config = new ModConfig();
        config.fillDefaults();
        config.save();
        return config;
    }

    private void fillDefaults() {
        for (String key : AVAILABLE_ITEMS.keySet()) {
            lootSettings.putIfAbsent(key, key.equals("minecraft:breeze_rod") || key.equals("minecraft:ominous_bottle"));
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[AutoLoot Mace] Не удалось сохранить конфиг: " + e.getMessage());
        }
    }

    public boolean isItemEnabled(String itemId) {
        return lootSettings.getOrDefault(itemId, false);
    }

    public void setItemEnabled(String itemId, boolean enabled) {
        lootSettings.put(itemId, enabled);
    }
}
