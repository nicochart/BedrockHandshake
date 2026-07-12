package fr.factionbedrock.bedrockhandshake.network.payload;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.network.handshake.ModInfo;
import fr.factionbedrock.bedrockhandshake.network.handshake.ResourcePackInfo;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HandshakeData(boolean fromAdminTool, List<ModInfo> mods, List<ResourcePackInfo> packs) implements CustomPacketPayload
{
    public static final Type<HandshakeData> ID = new Type<>(BedrockHandshake.id("handshake_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, HandshakeData> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, HandshakeData::fromAdminTool,
            ByteBufCodecs.collection(ArrayList::new, ModInfo.CODEC), HandshakeData::mods,
            ByteBufCodecs.collection(ArrayList::new, ResourcePackInfo.CODEC), HandshakeData::packs,
            HandshakeData::new);

    @Override public Type<? extends CustomPacketPayload> type() {return ID;}
}