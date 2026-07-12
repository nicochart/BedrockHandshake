package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerDeathMixin
{
    @Inject(at = @At("HEAD"), method = "restoreFrom")
    private void applyOnCopyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo info)
    {
        ServerPlayer newPlayer = (ServerPlayer) (Object) this;

        int count = oldPlayer.getEntityData().get(BedrockHandshakeTrackedData.INFRACTION_COUNT);
        newPlayer.getEntityData().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, count);
    }
}
