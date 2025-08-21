package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.client.packet.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.packet.BedrockHandshakeNetworking;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public class ReloadResourcesMixin
{
    @Inject(method = "reloadResources", at = @At("RETURN"))
    private void onReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> ci)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null && client.player != null)
        {
            ClientBedrockHandshakeNetworking.sendPacketFromClient(BedrockHandshakeNetworking.createHandshakePacket(client.getResourcePackManager()));
        }
    }
}
