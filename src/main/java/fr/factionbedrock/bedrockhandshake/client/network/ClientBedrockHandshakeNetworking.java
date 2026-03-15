package fr.factionbedrock.bedrockhandshake.client.network;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.network.payload.ServerResponseData;
import fr.factionbedrock.bedrockhandshake.network.payload.HandshakeData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientBedrockHandshakeNetworking
{
    public static void sendPacketFromClient(HandshakeData payload)
    {
        ClientPlayNetworking.send(payload);
    }

    public static void registerClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(ServerResponseData.ID, (payload, context) ->
        {
            String serverResponse = payload.response();
            BedrockHandshake.LOGGER.info("Server response : " + serverResponse);
        });
    }
}
