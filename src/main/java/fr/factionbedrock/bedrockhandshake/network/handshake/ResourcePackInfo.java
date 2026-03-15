package fr.factionbedrock.bedrockhandshake.network.handshake;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ResourcePackInfo(String packSignature, String fileName, String description)
{
    public static final PacketCodec<RegistryByteBuf, ResourcePackInfo> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ResourcePackInfo::packSignature, PacketCodecs.STRING, ResourcePackInfo::fileName, PacketCodecs.STRING, ResourcePackInfo::description, ResourcePackInfo::new);

    @Override public String toString() {return "\"" + this.fileName + "\" : " + this.description + " (" + this.packSignature+ ")";}
}
