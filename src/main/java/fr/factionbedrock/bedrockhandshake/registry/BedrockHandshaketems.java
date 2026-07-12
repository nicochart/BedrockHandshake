package fr.factionbedrock.bedrockhandshake.registry;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.item.DebugItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class BedrockHandshaketems
{
    public static final Item DEBUG_ITEM = register(Keys.DEBUG_ITEM.location().getPath(), new DebugItem(new Item.Properties().stacksTo(1).setId(Keys.DEBUG_ITEM)));

    public static class Keys
    {
        public static final ResourceKey<Item> DEBUG_ITEM = createKey("debug_item");

        private static ResourceKey<Item> createKey(String name)
        {
            return ResourceKey.create(Registries.ITEM, BedrockHandshake.id(name));
        }
    }

    public static <T extends Item> T register(String name, T item) {return Registry.register(BuiltInRegistries.ITEM, BedrockHandshake.id(name), item);}

    public static void load() {}
}
