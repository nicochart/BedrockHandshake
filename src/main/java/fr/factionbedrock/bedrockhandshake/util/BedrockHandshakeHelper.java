package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.network.handshake.ModInfo;
import fr.factionbedrock.bedrockhandshake.network.handshake.ResourcePackInfo;
import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerInterface;
import net.minecraft.world.entity.player.Player;
import java.util.List;

public class BedrockHandshakeHelper
{
    public static void messageListsToPlayer(Player player, List<ModInfo> modList, List<String> allowedModIds, List<ResourcePackInfo> packList, List<String> allowedPacksSignatures)
    {
        messageModListToPlayer(player, modList, allowedModIds);
        messageResourcePackListToPlayer(player, packList, allowedPacksSignatures);
    }

    public static void messageModListToPlayer(Player player, List<ModInfo> modList, List<String> allowedModIds)
    {
        int loadedModCount = modList.size();

        String numberOfMods = loadedModCount + " loaded mods" + (loadedModCount != 0 ? " :" : "");
        player.displayClientMessage(Component.literal(numberOfMods).withStyle(style -> style.withBold(true)), false);

        int modCount = 0;
        for (ModInfo modInfo : modList)
        {
            modCount++;
            ChatFormatting color = allowedModIds.contains(modInfo.modId()) ? ChatFormatting.GREEN : ChatFormatting.RED;
            player.displayClientMessage(modInfo.toText(color, Component.literal("Mod " + modCount + " : ")), false);
        }
    }

    public static void messageResourcePackListToPlayer(Player player, List<ResourcePackInfo> packList, List<String> allowedPacksSignatures)
    {
        int loadedPacksCount = packList.size();

        String numberOfLoadedPacks = loadedPacksCount + " loaded packs" + (loadedPacksCount != 0 ? " : " : "");
        player.displayClientMessage(Component.literal(numberOfLoadedPacks).withStyle(style -> style.withBold(true)), false);

        int packCount = 0;
        for (ResourcePackInfo packInfo : packList)
        {
            packCount++;
            ChatFormatting color = allowedPacksSignatures.contains(packInfo.packSignature()) ? ChatFormatting.GREEN : ChatFormatting.RED;
            player.displayClientMessage(packInfo.toText(color, Component.literal("Pack " + packCount + " : ")), false);
        }
    }

    public static int getInfractionCount(Player player)
    {
        return player.getEntityData().get(BedrockHandshakeTrackedData.INFRACTION_COUNT);
    }

    public static void increaseInfractionCount(Player player)
    {
        player.getEntityData().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, getInfractionCount(player) + 1);
    }

    public static boolean isDedicated(MinecraftServer server)
    {
        return server instanceof ServerInterface;
    }

    public static Component textCopyable(String text)
    {
        //inspired of ComponentUtils "copyOnClickText" method
        return Component.literal(text).withStyle(style -> style
                .withUnderlined(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click"))));
    }
}
