package fr.factionbedrock.bedrockhandshake.registry;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class BedrockHandshakeTrackedData
{
    public static final TrackedData<Integer> INFRACTION_COUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static void load() {}
}
