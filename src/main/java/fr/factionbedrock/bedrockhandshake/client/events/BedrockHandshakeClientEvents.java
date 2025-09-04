package fr.factionbedrock.bedrockhandshake.client.events;

import fr.factionbedrock.bedrockhandshake.client.packet.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.client.util.ClientHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class BedrockHandshakeClientEvents
{
    public static void registerClientPlayEvents()
    {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
        {
            ClientBedrockHandshakeNetworking.sendPacketFromClient(ClientHelper.createHandshakePacket());
        });
    }
}
