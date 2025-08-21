package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class BedrockHandshakeVerifier
{
    public static boolean verify(PlayerEntity player, List<String> loadedMods, List<String> loadedPacks)
    {
        if (BedrockHandshake.PLAYERS_WHITELIST.contains(player.getName().getString())) {return true;}
        else
        {
            for (String mod : loadedMods) {if (!BedrockHandshake.MODS_WHITELIST.contains(mod)) {return false;}}
            for (String pack : loadedPacks) {if (!BedrockHandshake.PACKS_WHITELIST.contains(pack)) {return false;}}

            return true;
        }
    }
}
