package fr.factionbedrock.bedrockhandshake.network.handshake;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public record ResourcePackInfo(String packSignature, String fileName, String description)
{
    public static final PacketCodec<RegistryByteBuf, ResourcePackInfo> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ResourcePackInfo::packSignature, PacketCodecs.STRING, ResourcePackInfo::fileName, PacketCodecs.STRING, ResourcePackInfo::description, ResourcePackInfo::new);

    @Override public String toString() {return "\"" + this.fileName + "\" : " + this.description + " (" + this.packSignature+ ")";}

    public MutableText toText()
    {
        return this.toText(Formatting.WHITE, Text.literal(""));
    }

    public MutableText toText(Formatting formatting, Text customPrefix)
    {
        MutableText packInfo = Text.literal(customPrefix.getString()).styled(style -> style.withColor(formatting));

        Text hashText = Text.literal(packSignature).styled(style -> style
                .withClickEvent(new ClickEvent.CopyToClipboard(packSignature))
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                .withUnderline(true));

        return packInfo.append("\"").append(fileName).append("\" : ").append(description).append(", ").append(hashText);
    }
}
