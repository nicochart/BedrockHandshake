package fr.factionbedrock.bedrockhandshake.client.util;

import fr.factionbedrock.bedrockhandshake.network.BedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.network.payload.HandshakeData;
import net.minecraft.client.Minecraft;

public class ClientHelper
{
    public static HandshakeData createHandshakePacket()
    {
        return BedrockHandshakeNetworking.createHandshakePacket(Minecraft.getInstance().getResourcePackRepository(), Minecraft.getInstance().getResourcePackDirectory().toFile().getAbsolutePath());
    }

    public static HandshakeData createAdminHandshakePacket()
    {
        return BedrockHandshakeNetworking.createHandshakePacket(true, Minecraft.getInstance().getResourcePackRepository(), Minecraft.getInstance().getResourcePackDirectory().toFile().getAbsolutePath());
    }
}
