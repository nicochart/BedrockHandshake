package fr.factionbedrock.bedrockhandshake.registry;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public class BedrockHandshakeTrackedData
{
    public static final EntityDataAccessor<Integer> INFRACTION_COUNT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    public static void load() {}
}
