package fr.factionbedrock.bedrockhandshake.packet;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.util.ResourcePackInfo;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.ArrayList;
import java.util.List;

public record HandshakeData(List<String> mods, List<ResourcePackInfo> packs) implements CustomPayload
{
    public static final Id<HandshakeData> ID = new Id<>(BedrockHandshake.id("handshake_data"));

    public static final PacketCodec<RegistryByteBuf, HandshakeData> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING), HandshakeData::mods,
            PacketCodecs.collection(ArrayList::new, ResourcePackInfo.CODEC), HandshakeData::packs,
            HandshakeData::new);

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}