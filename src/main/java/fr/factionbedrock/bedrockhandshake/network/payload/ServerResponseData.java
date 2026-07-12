package fr.factionbedrock.bedrockhandshake.network.payload;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ServerResponseData(String response) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ServerResponseData> ID = new CustomPacketPayload.Type<>(BedrockHandshake.id("data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerResponseData> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ServerResponseData::response,
            ServerResponseData::new);

    @Override public Type<? extends CustomPacketPayload> type() {return ID;}
}