package fr.factionbedrock.bedrockhandshake.network.handshake;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ModInfo(String modId, String name, String version)
{
    public static final PacketCodec<RegistryByteBuf, ModInfo> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ModInfo::modId, PacketCodecs.STRING, ModInfo::name, PacketCodecs.STRING, ModInfo::version, ModInfo::new);

    @Override public String toString() {return "\"" + this.name + "\" " + this.version + " (" + this.modId + ")";}
}
