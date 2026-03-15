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
        int modCount = modList.size();

        String numberOfMods = modCount + " loaded mods" + (modCount != 0 ? " :" : "");
        player.sendMessage(Text.literal(numberOfMods).styled(style -> style.withBold(true)), false);

        int modNumber = 0;

        for (ModInfo mod : modList)
        {
            modNumber++;

            String modId = mod.modId();
            String name = mod.name();
            String version = mod.version();

            Formatting color = allowedModIds.contains(modId) ? Formatting.WHITE : Formatting.RED;
            MutableText modInfo = Text.literal("Mod " + modNumber + " : ").styled(style -> style.withColor(color));

            Text modIdText = Text.literal(modId).styled(style -> style
                    .withClickEvent(new ClickEvent.CopyToClipboard(modId))
                    .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                    .withUnderline(true));

            modInfo.append("\"").append(name).append("\" : ").append(modIdText).append(" version ").append(version);

            player.sendMessage(modInfo, false);
        }
    }

    public static void messageResourcePackListToPlayer(PlayerEntity player, List<ResourcePackInfo> packList, List<String> allowedPacksSignatures)
    {
        int loadedPacksCount = packList.size();

        String numberOfLoadedPacks = loadedPacksCount + " loaded packs" + (loadedPacksCount != 0 ? " : " : "");
        player.sendMessage(Text.literal(numberOfLoadedPacks).styled(style -> style.withBold(true)), false);

        int packNumber = 0;
        for (ResourcePackInfo profile : packList)
        {
            packNumber++;

            String displayName = profile.fileName();
            String description = profile.description();
            String packSignature = profile.packSignature();

            Formatting color = allowedPacksSignatures.contains(packSignature) ? Formatting.WHITE : Formatting.RED;
            MutableText packInfo = Text.literal("Pack "+packNumber+" : ").styled(style -> style.withColor(color));

            Text hashText = Text.literal(packSignature).styled(style -> style
                                .withClickEvent(new ClickEvent.CopyToClipboard(packSignature))
                                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                                .withUnderline(true));

            packInfo.append("\"").append(displayName).append("\" : ").append(description).append(", ").append(hashText);

            player.sendMessage(packInfo, false);
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
