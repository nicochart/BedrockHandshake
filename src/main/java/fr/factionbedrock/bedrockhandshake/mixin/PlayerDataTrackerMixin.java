package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerDataTrackerMixin
{
	@Inject(at = @At("RETURN"), method = "defineSynchedData")
	private void init(SynchedEntityData.Builder builder, CallbackInfo info)
	{
		builder.define(BedrockHandshakeTrackedData.INFRACTION_COUNT, 0);
	}
}