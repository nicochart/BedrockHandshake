package fr.factionbedrock.bedrockhandshake.packet;

import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeVerifier;
import fr.factionbedrock.bedrockhandshake.util.PendingHandshakeTracker;
import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BedrockHandshakeNetworking
{
    public static HandshakeData createHandshakePacket(ResourcePackManager resourcePackManager) //C2S
    {
        return new HandshakeData(BedrockHandshakeHelper.getLoadedModsList(), BedrockHandshakeHelper.getLoadedResourcePacksList(resourcePackManager));
    }

    public static final CustomData CONFIRMATION_PACKET = new CustomData("confirmation", 0); //S2C

    public static void registerData()
    {
        PayloadTypeRegistry.playC2S().register(HandshakeData.ID, HandshakeData.CODEC);
        PayloadTypeRegistry.playS2C().register(CustomData.ID, CustomData.CODEC);
    }

    public static void sendPacketFromServer(ServerPlayerEntity serverPlayer, CustomData payload)
    {
        ServerPlayNetworking.send(serverPlayer, payload);
    }

    public static void registerServerReceiver()
    {
        ServerPlayNetworking.registerGlobalReceiver(HandshakeData.ID, (payload, context) ->
        {
            context.player().sendMessage(Text.literal("Server Received Handshake Packet"), false);
            PendingHandshakeTracker.unmark(context.player().getUuid());
            BedrockHandshakeHelper.messageLoadedThingsToPlayer(context.player(), payload.mods(), payload.packs());
            sendPacketFromServer(context.player(), CONFIRMATION_PACKET);
            if (!BedrockHandshakeVerifier.verify(context.player(), payload.mods(), payload.packs()))
            {
                BedrockHandshakeHelper.kickPlayerForInfraction(context.player(), "Client Infraction detected");
            }
        });
    }
}
