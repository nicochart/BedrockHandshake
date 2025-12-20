package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerNbtMixin
{
    private static String infraction_count = "infraction_count";

    @Inject(at = @At("RETURN"), method = "readCustomData")
    private void read(ReadView view, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (view.contains(infraction_count))
        {
            player.getDataTracker().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, view.getInt(infraction_count, 0));
        }
    }

    @Inject(at = @At("RETURN"), method = "writeCustomData")
    private void write(WriteView view, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        view.putInt(infraction_count, player.getDataTracker().get(BedrockHandshakeTrackedData.INFRACTION_COUNT));
    }
}
