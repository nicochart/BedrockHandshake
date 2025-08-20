package fr.factionbedrock.bedrockhandshake.mixin;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerNbtMixin
{
    private static String infraction_count = "infraction_count";

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    private void read(NbtCompound nbt, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (nbt.contains(infraction_count, NbtElement.INT_TYPE))
        {
            player.getDataTracker().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, nbt.getInt(infraction_count));
        }
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    private void write(NbtCompound nbt, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        nbt.putInt(infraction_count, player.getDataTracker().get(BedrockHandshakeTrackedData.INFRACTION_COUNT));
    }
}
