package fr.factionbedrock.bedrockhandshake.client.events;

import fr.factionbedrock.bedrockhandshake.client.packet.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.packet.BedrockHandshakeNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

public class BedrockHandshakeClientEvents
{
    public static void registerClientPlayEvents()
    {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
        {
            ClientBedrockHandshakeNetworking.sendPacketFromClient(BedrockHandshakeNetworking.createHandshakePacket(MinecraftClient.getInstance().getResourcePackManager()));
        });
    }
}
