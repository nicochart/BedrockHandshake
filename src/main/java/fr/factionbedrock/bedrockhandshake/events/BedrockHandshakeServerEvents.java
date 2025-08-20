package fr.factionbedrock.bedrockhandshake.events;

import fr.factionbedrock.bedrockhandshake.util.HandshakeTimeoutScheduler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class BedrockHandshakeServerEvents
{
    public static void registerPlayerEvents()
    {
        ServerLifecycleEvents.SERVER_STOPPING.register(server ->
        {
            HandshakeTimeoutScheduler.shutdownScheduler();
        });
    }
}
