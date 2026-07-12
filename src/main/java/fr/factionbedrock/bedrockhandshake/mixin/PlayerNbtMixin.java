package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerNbtMixin
{
    private static String infraction_count = "infraction_count";

    @Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
    private void read(ValueInput view, CallbackInfo info)
    {
        Player player = (Player) (Object) this;
        if (view.contains(infraction_count))
        {
            player.getEntityData().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, view.getIntOr(infraction_count, 0));
        }
    }

    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    private void write(ValueOutput view, CallbackInfo info)
    {
        Player player = (Player) (Object) this;
        view.putInt(infraction_count, player.getEntityData().get(BedrockHandshakeTrackedData.INFRACTION_COUNT));
    }
}
