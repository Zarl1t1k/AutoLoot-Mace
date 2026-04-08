# ⚡ Mace Auto Loot — Fabric Mod для Minecraft 1.21.1

Мод автоматически выбивает нужный лут из хранилищ (сундуки, бочки и т.д.) через shift+click.

---

## 🎯 Что делает мод

Когда вы открываете любое хранилище (сундук, бочку, шалкер и т.д.), мод автоматически сканирует слоты и выполняет **Shift+Click** на предметы, которые вы настроили. Предмет мгновенно перемещается в ваш инвентарь.

---

## 📦 Поддерживаемый лут (настраиваемый)

| Предмет | ID | По умолчанию |
|---|---|---|
| Навершие булавы (Стержень бриза) | `minecraft:breeze_rod` | ✅ ВКЛ |
| Зловещая бутылочка | `minecraft:ominous_bottle` | ✅ ВКЛ |
| Булава | `minecraft:mace` | ❌ ВЫКЛ |
| Заряд ветра | `minecraft:wind_charge` | ❌ ВЫКЛ |
| Тяжёлое ядро | `minecraft:heavy_core` | ❌ ВЫКЛ |

---

## ⌨️ Управление

| Клавиша | Действие |
|---|---|
| `M` | Открыть меню настроек |
| `N` | Быстро включить/выключить мод |

> Клавиши можно переназначить в `Настройки → Управление → Mace Auto Loot`

---

## 🔧 Установка

### Требования
- Minecraft **1.21.1**
- [Fabric Loader](https://fabricmc.net/use/installer/) **≥ 0.15.0**
- [Fabric API](https://modrinth.com/mod/fabric-api) для 1.21.1

### Шаги установки
1. Установите Fabric Loader для Minecraft 1.21.1
2. Скачайте Fabric API и поместите в папку `mods`
3. Скомпилируйте мод командой `./gradlew build` (или `gradlew.bat build` на Windows)
4. Скопируйте `.jar` файл из `build/libs/` в папку `mods`
5. Запустите Minecraft с профилем Fabric

---

## 🔨 Сборка из исходников

```bash
# Клонируйте или распакуйте проект
cd mace-autoloot-mod

# Сборка (требуется JDK 21)
./gradlew build

# Готовый .jar будет в:
# build/libs/mace-autoloot-1.0.0.jar
```

На Windows используйте `gradlew.bat build`

---

## ⚙️ Конфигурационный файл

Настройки хранятся в:
```
.minecraft/config/maceautoloot.json
```

Пример:
```json
{
  "lootSettings": {
    "minecraft:breeze_rod": true,
    "minecraft:ominous_bottle": true,
    "minecraft:mace": false,
    "minecraft:wind_charge": false,
    "minecraft:heavy_core": false
  },
  "modEnabled": true,
  "clickDelayMs": 200
}
```

---

## 📋 Структура проекта

```
mace-autoloot-mod/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── gradle/wrapper/
└── src/main/
    ├── java/com/maceautoloot/
    │   ├── client/
    │   │   ├── MaceAutoLootClient.java   ← Точка входа, клавиши
    │   │   └── ConfigScreen.java         ← GUI настроек
    │   ├── config/
    │   │   └── ModConfig.java            ← Конфигурация (JSON)
    │   ├── mixin/
    │   │   └── HandledScreenMixin.java   ← Хук в экран хранилища
    │   └── util/
    │       ├── LootDetector.java         ← Поиск нужного лута
    │       └── AutoClicker.java          ← Авто Shift+Click
    └── resources/
        ├── fabric.mod.json
        ├── maceautoloot.mixins.json
        └── assets/maceautoloot/
            └── lang/
                ├── ru_ru.json
                └── en_us.json
```

---

## ⚠️ Внимание

Мод предназначен для одиночной игры или серверов, где разрешено использование клиентских модов. Используйте на своё усмотрение.
