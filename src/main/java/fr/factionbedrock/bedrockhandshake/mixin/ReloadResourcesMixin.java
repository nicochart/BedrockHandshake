package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.client.network.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.client.util.ClientHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class ReloadResourcesMixin
{
    @Inject(method = "reloadResourcePacks", at = @At("RETURN"))
    private void onReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> ci)
    {
        Minecraft client = Minecraft.getInstance();
        if (client.getConnection() != null && client.player != null)
        {
            ClientBedrockHandshakeNetworking.sendPacketFromClient(ClientHelper.createHandshakePacket());
        }
    }
}
