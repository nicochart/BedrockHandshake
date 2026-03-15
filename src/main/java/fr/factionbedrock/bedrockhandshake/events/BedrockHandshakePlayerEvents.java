package fr.factionbedrock.bedrockhandshake.events;

import fr.factionbedrock.bedrockhandshake.network.handshake.server.HandshakeTimeoutScheduler;
import fr.factionbedrock.bedrockhandshake.network.handshake.server.PendingHandshakeTracker;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.util.UUID;

public class BedrockHandshakePlayerEvents
{
    public static void registerPlayerEvents()
    {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            UUID playerId = handler.player.getUuid();

            PendingHandshakeTracker.mark(playerId);

            HandshakeTimeoutScheduler.schedule(server, playerId, handler.player);
        });
    }
}
