package fr.factionbedrock.bedrockhandshake.util;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ResourcePackInfo(String packSignature, String fileName)
{
    public static final PacketCodec<RegistryByteBuf, ResourcePackInfo> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ResourcePackInfo::packSignature, PacketCodecs.STRING, ResourcePackInfo::fileName, ResourcePackInfo::new);

    @Override public String toString() {return this.fileName + " (" + this.packSignature() + ")";}
}
