package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.network.handshake.ModInfo;
import fr.factionbedrock.bedrockhandshake.network.handshake.ResourcePackInfo;
import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.List;

public class BedrockHandshakeHelper
{
    public static void messageListsToPlayer(PlayerEntity player, List<ModInfo> modList, List<String> allowedModIds, List<ResourcePackInfo> packList, List<String> allowedPacksSignatures)
    {
        messageModListToPlayer(player, modList, allowedModIds);
        messageResourcePackListToPlayer(player, packList, allowedPacksSignatures);
    }

    public static void messageModListToPlayer(PlayerEntity player, List<ModInfo> modList, List<String> allowedModIds)
    {
        int loadedModCount = modList.size();

        String numberOfMods = loadedModCount + " loaded mods" + (loadedModCount != 0 ? " :" : "");
        player.sendMessage(Text.literal(numberOfMods).styled(style -> style.withBold(true)), false);

        int modCount = 0;
        for (ModInfo modInfo : modList)
        {
            modCount++;
            Formatting color = allowedModIds.contains(modInfo.modId()) ? Formatting.GREEN : Formatting.RED;
            player.sendMessage(modInfo.toText(color, Text.literal("Mod " + modCount + " : ")), false);
        }
    }

    public static void messageResourcePackListToPlayer(PlayerEntity player, List<ResourcePackInfo> packList, List<String> allowedPacksSignatures)
    {
        int loadedPacksCount = packList.size();

        String numberOfLoadedPacks = loadedPacksCount + " loaded packs" + (loadedPacksCount != 0 ? " : " : "");
        player.sendMessage(Text.literal(numberOfLoadedPacks).styled(style -> style.withBold(true)), false);

        int packCount = 0;
        for (ResourcePackInfo packInfo : packList)
        {
            packCount++;
            Formatting color = allowedPacksSignatures.contains(packInfo.packSignature()) ? Formatting.GREEN : Formatting.RED;
            player.sendMessage(packInfo.toText(color, Text.literal("Pack " + packCount + " : ")), false);
        }
    }

    public static int getInfractionCount(PlayerEntity player)
    {
        return player.getDataTracker().get(BedrockHandshakeTrackedData.INFRACTION_COUNT);
    }

    public static void increaseInfractionCount(PlayerEntity player)
    {
        player.getDataTracker().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, getInfractionCount(player) + 1);
    }

    public static boolean isDedicated(MinecraftServer server)
    {
        return server instanceof DedicatedServer;
    }
}
