package fr.factionbedrock.bedrockhandshake.config;

import java.util.List;

public record BedrockHandshakeConfig(boolean doHandshakeInIntegratedServer, int toleratedInfractionCount, List<String> modsWhitelist, List<String> packsWhitelist, List<String> playersWhitelist) {}