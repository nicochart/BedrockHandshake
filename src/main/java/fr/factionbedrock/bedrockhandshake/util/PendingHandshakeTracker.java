package fr.factionbedrock.bedrockhandshake.util;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PendingHandshakeTracker
{
    private static final Set<UUID> waitingPlayers = ConcurrentHashMap.newKeySet();

    public static void mark(UUID uuid) {waitingPlayers.add(uuid);}

    public static void unmark(UUID uuid) {waitingPlayers.remove(uuid);}

    public static boolean isStillWaiting(UUID uuid) {return waitingPlayers.contains(uuid);}
}
