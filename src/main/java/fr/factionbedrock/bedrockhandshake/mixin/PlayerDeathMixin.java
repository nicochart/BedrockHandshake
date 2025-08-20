package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin
{
    @Inject(at = @At("HEAD"), method = "copyFrom")
    private void applyOnCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info)
    {
        ServerPlayerEntity newPlayer = (ServerPlayerEntity) (Object) this;

        int count = oldPlayer.getDataTracker().get(BedrockHandshakeTrackedData.INFRACTION_COUNT);
        newPlayer.getDataTracker().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, count);
    }
}
