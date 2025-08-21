package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.packet.BedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.packet.HandshakeData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class BedrockHandshakeVerifier
{
    public enum InfractionType{NONE, MOD, PACK, MOD_AND_PACK, NO_HANDSHAKE}
    public record Response(String list, InfractionType type) {}

    public static void onHandshake(ServerPlayerEntity player, HandshakeData data)
    {
        if (PendingHandshakeTracker.isStillWaiting(player.getUuid())) {PendingHandshakeTracker.unmark(player.getUuid());}
        Response response = verify(player, data.mods(), data.packs());
        InfractionType infractionType = response.type();
        String infractionList = response.list();
        BedrockHandshake.LOGGER.info("Server verifying player " + player.getName().getString() + " : " + ((infractionList.isEmpty()) ? "no infraction" : infractionList));
        BedrockHandshakeNetworking.sendPacketFromServer(player, BedrockHandshakeNetworking.createServerResponsePacket(infractionType, infractionList));
        if (infractionType != InfractionType.NONE)
        {
            manageInvalidClient(player, "Client Infraction detected");
        }
    }

    public static void onMissingHandshake(ServerPlayerEntity player)
    {
        PendingHandshakeTracker.unmark(player.getUuid());
        BedrockHandshake.LOGGER.info("Server verifying player " + player.getName().getString() + " : no handshake response, kicked.");
        BedrockHandshakeVerifier.manageInvalidClient(player, "Kicked for not receiving mod list. Contact server administrator.");
    }

    public static void manageInvalidClient(ServerPlayerEntity player, String message)
    {
        String disconnectMessage = message;
        if (!player.isDisconnected())
        {
            MinecraftServer server = player.getServer();
            if (BedrockHandshakeHelper.getInfractionCount(player) > 2 && server != null)
            {
                disconnectMessage = "You have been banned for multiple infractions.";
                server.getPlayerManager().getUserBanList().add(new BannedPlayerEntry(player.getGameProfile(), null, BedrockHandshake.MOD_ID, null, disconnectMessage));
            }
            BedrockHandshakeHelper.increaseInfractionCount(player);
            player.networkHandler.disconnect(Text.literal(disconnectMessage));
        }
    }

    public static Response verify(PlayerEntity player, List<String> loadedMods, List<String> loadedPacks)
    {
        if (BedrockHandshake.PLAYERS_WHITELIST.contains(player.getName().getString())) {return new Response("", InfractionType.NONE);}
        else
        {
            InfractionType infractionType = InfractionType.NONE;
            String list = "invalid mods : ";
            for (String mod : loadedMods) { if (!BedrockHandshake.MODS_WHITELIST.contains(mod))
            {
                infractionType = InfractionType.MOD;
                list += mod + ", ";
            }}
            if (list.endsWith(" : ")) {list += "none";}
            else if (list.endsWith(", ")) {list = list.substring(0, list.length() - 2);}

            list += ", invalid packs : ";
            for (String pack : loadedPacks) { if (!BedrockHandshake.PACKS_WHITELIST.contains(pack))
            {
                infractionType = (infractionType == InfractionType.MOD) ? InfractionType.MOD_AND_PACK : InfractionType.PACK;
                list += pack + ", ";
            }}
            if (list.endsWith(" : ")) {list += "none";}
            else if (list.endsWith(", ")) {list = list.substring(0, list.length() - 2);}

            return new Response(list, infractionType);
        }
    }
}
