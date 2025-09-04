package fr.factionbedrock.bedrockhandshake.packet;

import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeVerifier;
import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import fr.factionbedrock.bedrockhandshake.util.HashUtils;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class BedrockHandshakeNetworking
{
    public static HandshakeData createHandshakePacket(ResourcePackManager resourcePackManager, String resourcePackDirectoryPath) //C2S
    {
        HashUtils.listActivePacks(resourcePackManager, resourcePackDirectoryPath);
        return new HandshakeData(BedrockHandshakeHelper.getLoadedModsList(), BedrockHandshakeHelper.getLoadedResourcePacksList(resourcePackManager));
    }

    public static ServerResponseData createServerResponsePacket(BedrockHandshakeVerifier.InfractionType infractionType, String infractionList) //S2C
    {
        return switch (infractionType)
        {
            case BedrockHandshakeVerifier.InfractionType.NONE -> new ServerResponseData("Client is authorized to join !");
            case BedrockHandshakeVerifier.InfractionType.MOD -> new ServerResponseData("Client will be kicked. Mod infraction detected. "+infractionList);
            case BedrockHandshakeVerifier.InfractionType.PACK -> new ServerResponseData("Client will be kicked. Pack infraction detected. "+infractionList);
            case BedrockHandshakeVerifier.InfractionType.MOD_AND_PACK -> new ServerResponseData("Client will be kicked. Mod and pack infraction detected."+infractionList);
            case BedrockHandshakeVerifier.InfractionType.NO_HANDSHAKE -> new ServerResponseData("Client will be kicked because no handshake."); //should never happen because response is sent only if server received list
        };
    }

    public static void registerData()
    {
        PayloadTypeRegistry.playC2S().register(HandshakeData.ID, HandshakeData.CODEC);
        PayloadTypeRegistry.playS2C().register(ServerResponseData.ID, ServerResponseData.CODEC);
    }

    public static void sendPacketFromServer(ServerPlayerEntity serverPlayer, ServerResponseData payload)
    {
        ServerPlayNetworking.send(serverPlayer, payload);
    }

    public static void registerServerReceiver()
    {
        ServerPlayNetworking.registerGlobalReceiver(HandshakeData.ID, (payload, context) ->
        {
            BedrockHandshakeVerifier.onHandshake(context.player(), payload);
        });
    }
}
