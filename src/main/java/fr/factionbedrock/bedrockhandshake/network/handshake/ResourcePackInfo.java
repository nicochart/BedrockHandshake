package fr.factionbedrock.bedrockhandshake.network.handshake;

import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ResourcePackInfo(String packSignature, String fileName, String description)
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourcePackInfo> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ResourcePackInfo::packSignature, ByteBufCodecs.STRING_UTF8, ResourcePackInfo::fileName, ByteBufCodecs.STRING_UTF8, ResourcePackInfo::description, ResourcePackInfo::new);

    @Override public String toString() {return "\"" + this.fileName + "\" : " + this.description + " (" + this.packSignature+ ")";}

    public MutableComponent toText()
    {
        return this.toText(ChatFormatting.WHITE, Component.literal(""));
    }

    public MutableComponent toText(ChatFormatting formatting, Component customPrefix)
    {
        MutableComponent packInfo = Component.literal(customPrefix.getString()).withStyle(style -> style.withColor(formatting));
        Component hashText = BedrockHandshakeHelper.textCopyable(packSignature);

        return packInfo.append("\"").append(fileName).append("\" : ").append(description).append(", ").append(hashText);
    }
}
