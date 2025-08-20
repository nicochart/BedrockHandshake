package fr.factionbedrock.bedrockhandshake.client.packet;

import fr.factionbedrock.bedrockhandshake.packet.CustomData;
import fr.factionbedrock.bedrockhandshake.packet.BedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.packet.HandshakeData;
import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ClientBedrockHandshakeNetworking
{
    public static void sendPacketFromClient(HandshakeData payload)
    {
        ClientPlayNetworking.send(payload);
    }

    public static void registerClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(CustomData.ID, (payload, context) ->
        {
            if (payload.name().equals(BedrockHandshakeNetworking.CONFIRMATION_PACKET.name()))
            {
                context.player().sendMessage(Text.literal("Client is authorized to join !"), false);
                BedrockHandshakeHelper.messageLoadedModsToPlayer(context.player());
                BedrockHandshakeHelper.messageLoadedResourcePacksToPlayer(MinecraftClient.getInstance().getResourcePackManager(), context.player());
            }
        });
    }
}
