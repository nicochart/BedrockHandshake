package fr.factionbedrock.bedrockhandshake.events;

import fr.factionbedrock.bedrockhandshake.util.HandshakeTimeoutScheduler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class BedrockHandshakeServerEvents
{
    public static void registerServerEvents()
    {
        ServerLifecycleEvents.SERVER_STARTING.register(server ->
        {
            HandshakeTimeoutScheduler.startScheduler();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server ->
        {
            HandshakeTimeoutScheduler.shutdownScheduler();
        });
    }
}
