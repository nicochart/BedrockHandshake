package fr.factionbedrock.bedrockhandshake.network.handshake;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public record ModInfo(String modId, String name, String version)
{
    public static final PacketCodec<RegistryByteBuf, ModInfo> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ModInfo::modId, PacketCodecs.STRING, ModInfo::name, PacketCodecs.STRING, ModInfo::version, ModInfo::new);

    @Override public String toString() {return "\"" + this.name + "\" " + this.version + " (" + this.modId + ")";}

    public MutableText toText()
    {
        return this.toText(Formatting.WHITE, Text.literal(""));
    }

    public MutableText toText(Formatting formatting, Text customPrefix)
    {
        MutableText modInfo = Text.literal(customPrefix.getString()).styled(style -> style.withColor(formatting));

        Text modIdText = Text.literal(modId).styled(style -> style
                .withClickEvent(new ClickEvent.CopyToClipboard(modId))
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                .withUnderline(true));

        return modInfo.append("\"").append(name).append("\" : ").append(modIdText).append(" version ").append(version);
    }
}
