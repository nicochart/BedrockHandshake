package fr.factionbedrock.bedrockhandshake.network.handshake.server;

import com.mojang.authlib.GameProfile;
import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.network.BedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.network.handshake.ModInfo;
import fr.factionbedrock.bedrockhandshake.network.handshake.ResourcePackInfo;
import fr.factionbedrock.bedrockhandshake.network.payload.HandshakeData;
import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.entity.player.Player;
import java.util.List;

public class BedrockHandshakeVerifier
{
    public enum InfractionType{NONE, MOD, PACK, MOD_AND_PACK, NO_HANDSHAKE}
    public record Response(String list, InfractionType type) {}

    public static void onHandshake(ServerPlayer player, HandshakeData data)
    {
        if (PendingHandshakeTracker.isStillWaiting(player.getUUID())) {PendingHandshakeTracker.unmark(player.getUUID());}

        if (data.fromAdminTool())
        {
            BedrockHandshakeHelper.messageListsToPlayer(player, data.mods(), BedrockHandshake.MODS_WHITELIST, data.packs(), BedrockHandshake.PACKS_WHITELIST);
        }

        if (!BedrockHandshakeHelper.isDedicated(player.level().getServer()) && !BedrockHandshake.DO_HANDSHAKE_ON_INTEGRATED_SERVER)
        {
            BedrockHandshake.LOGGER.info("Bedrock Handshake - skipped player verification because server is not dedicated");
            return;
        }
        Response response = verify(player, data.mods(), data.packs());
        InfractionType infractionType = response.type();
        String infractionList = response.list();
        BedrockHandshake.LOGGER.info("Bedrock Handshake - Server verifying player " + player.getName().getString() + " : " + infractionList);
        BedrockHandshakeNetworking.sendPacketFromServer(player, BedrockHandshakeNetworking.createServerResponsePacket(infractionType, infractionList));
        if (infractionType != InfractionType.NONE)
        {
            manageInvalidClient(player, "Client Infraction detected");
        }
    }

    public static void onMissingHandshake(ServerPlayer player)
    {
        PendingHandshakeTracker.unmark(player.getUUID());
        BedrockHandshake.LOGGER.info("Bedrock Handshake - Server verifying player " + player.getName().getString() + " : no handshake response, kicked.");
        BedrockHandshakeVerifier.manageInvalidClient(player, "Kicked for not receiving mod list. Contact server administrator.");
    }

    public static void manageInvalidClient(ServerPlayer player, String message)
    {
        String disconnectMessage = message;
        if (!player.hasDisconnected())
        {
            BedrockHandshakeHelper.increaseInfractionCount(player);
            int infractionCount = BedrockHandshakeHelper.getInfractionCount(player);
            MinecraftServer server = player.level().getServer();
            if (infractionCount > BedrockHandshake.TOLERATED_INFRACTION_COUNT && server != null)
            {
                disconnectMessage = "You have been banned for multiple infractions.";
                server.getPlayerList().getBans().add(new UserBanListEntry(new GameProfile(player.getUUID(), player.getName().getString()), null, BedrockHandshake.MOD_ID, null, disconnectMessage));
            }
            player.connection.disconnect(Component.literal(disconnectMessage));
        }
    }

    public static Response verify(Player player, List<ModInfo> loadedMods, List<ResourcePackInfo> loadedPacks)
    {
        if (BedrockHandshake.PLAYERS_WHITELIST.contains(player.getName().getString())) {return new Response("skipped player verification because he is bedrock_handshake-whitelisted", InfractionType.NONE);}
        else
        {
            InfractionType infractionType = InfractionType.NONE;
            String list = "invalid mods : ";
            for (ModInfo mod : loadedMods) {if (!BedrockHandshake.MODS_WHITELIST.contains(mod.modId()))
            {
                infractionType = InfractionType.MOD;
                list += mod + ", ";
            }}
            if (list.endsWith(" : ")) {list += "none";}
            else if (list.endsWith(", ")) {list = list.substring(0, list.length() - 2);}

            list += ", invalid packs : ";
            for (ResourcePackInfo packInfo : loadedPacks) {if (!BedrockHandshake.PACKS_WHITELIST.contains(packInfo.packSignature()))
            {
                infractionType = (infractionType == InfractionType.MOD) ? InfractionType.MOD_AND_PACK : InfractionType.PACK;
                list += packInfo + ", ";
            }}
            if (list.endsWith(" : ")) {list += "none";}
            else if (list.endsWith(", ")) {list = list.substring(0, list.length() - 2);}

            if (list.equals("invalid mods : none, invalid packs : none")) {list = "no infraction, player can connect";}

            return new Response(list, infractionType);
        }
    }
}
