package fr.factionbedrock.bedrockhandshake.packet;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ServerResponseData(String response) implements CustomPayload
{
    public static final CustomPayload.Id<ServerResponseData> ID = new CustomPayload.Id<>(BedrockHandshake.id("data"));

    public static final PacketCodec<RegistryByteBuf, ServerResponseData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ServerResponseData::response,
            ServerResponseData::new);

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}