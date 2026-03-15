package fr.factionbedrock.bedrockhandshake.client.util;

import fr.factionbedrock.bedrockhandshake.network.BedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.network.payload.HandshakeData;
import net.minecraft.client.MinecraftClient;

public class ClientHelper
{
    public static HandshakeData createHandshakePacket()
    {
        return BedrockHandshakeNetworking.createHandshakePacket(MinecraftClient.getInstance().getResourcePackManager(), MinecraftClient.getInstance().getResourcePackDir().toFile().getAbsolutePath());
    }
}
