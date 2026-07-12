package fr.factionbedrock.bedrockhandshake.network.handshake;

import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ModInfo(String modId, String name, String version)
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ModInfo> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ModInfo::modId, ByteBufCodecs.STRING_UTF8, ModInfo::name, ByteBufCodecs.STRING_UTF8, ModInfo::version, ModInfo::new);

    @Override public String toString() {return "\"" + this.name + "\" " + this.version + " (" + this.modId + ")";}

    public MutableComponent toText()
    {
        return this.toText(ChatFormatting.WHITE, Component.literal(""));
    }

    public MutableComponent toText(ChatFormatting formatting, Component customPrefix)
    {
        MutableComponent modInfo = Component.literal(customPrefix.getString()).withStyle(style -> style.withColor(formatting));
        Component modIdText = BedrockHandshakeHelper.textCopyable(modId);

        return modInfo.append("\"").append(name).append("\" : ").append(modIdText).append(" version ").append(version);
    }
}
