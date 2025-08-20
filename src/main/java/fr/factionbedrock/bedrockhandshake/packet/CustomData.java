package fr.factionbedrock.bedrockhandshake.packet;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record CustomData(String name, int age) implements CustomPayload
{
    public static final CustomPayload.Id<CustomData> ID = new CustomPayload.Id<>(BedrockHandshake.id("data"));

    public static final PacketCodec<RegistryByteBuf, CustomData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, CustomData::name,
            PacketCodecs.VAR_INT, CustomData::age,
            CustomData::new);

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}