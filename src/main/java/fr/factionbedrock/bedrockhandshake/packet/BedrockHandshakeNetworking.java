package fr.factionbedrock.bedrockhandshake.packet;

import fr.factionbedrock.bedrockhandshake.util.PendingHandshakeTracker;
import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BedrockHandshakeNetworking
{
    public static final CustomData HANDSHAKE_PACKET = new CustomData("handshake", 0); //C2S
    public static final CustomData CONFIRMATION_PACKET = new CustomData("confirmation", 0); //S2C

    public static void registerData()
    {
        PayloadTypeRegistry.playC2S().register(CustomData.ID, CustomData.CODEC);
        PayloadTypeRegistry.playS2C().register(CustomData.ID, CustomData.CODEC);
    }

    public static void sendPacketFromServer(ServerPlayerEntity serverPlayer, CustomData payload)
    {
        ServerPlayNetworking.send(serverPlayer, payload);
    }

    public static void registerServerReceiver()
    {
        ServerPlayNetworking.registerGlobalReceiver(CustomData.ID, (payload, context) ->
        {
            if (payload.name().equals(HANDSHAKE_PACKET.name()))
            {
                context.player().sendMessage(Text.literal("Server Received Handshake Packet"), false);
                PendingHandshakeTracker.unmark(context.player().getUuid());
                BedrockHandshakeHelper.messageLoadedModsToPlayer(context.player());
                sendPacketFromServer(context.player(), CONFIRMATION_PACKET);
            }
        });
    }
}
