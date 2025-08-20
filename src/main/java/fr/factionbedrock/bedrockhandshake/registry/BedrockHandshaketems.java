package fr.factionbedrock.bedrockhandshake.registry;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.item.DebugItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class BedrockHandshaketems
{
    public static final Item DEBUG_ITEM = register(Keys.DEBUG_ITEM.getValue().getPath(), new DebugItem(new Item.Settings().maxCount(1).registryKey(Keys.DEBUG_ITEM)));

    public static class Keys
    {
        public static final RegistryKey<Item> DEBUG_ITEM = createKey("debug_item");

        private static RegistryKey<Item> createKey(String name)
        {
            return RegistryKey.of(RegistryKeys.ITEM, BedrockHandshake.id(name));
        }
    }

    public static <T extends Item> T register(String name, T item) {return Registry.register(Registries.ITEM, BedrockHandshake.id(name), item);}

    public static void load() {}
}
