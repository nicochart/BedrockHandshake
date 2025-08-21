package fr.factionbedrock.bedrockhandshake;

import fr.factionbedrock.bedrockhandshake.client.events.BedrockHandshakeClientEvents;
import fr.factionbedrock.bedrockhandshake.client.packet.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.config.BedrockHandshakeConfig;
import fr.factionbedrock.bedrockhandshake.config.BedrockHandshakeConfigLoader;
import fr.factionbedrock.bedrockhandshake.events.BedrockHandshakePlayerEvents;
import fr.factionbedrock.bedrockhandshake.events.BedrockHandshakeServerEvents;
import fr.factionbedrock.bedrockhandshake.packet.BedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshaketems;
import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BedrockHandshake implements ModInitializer, ClientModInitializer
{
	public static final String MOD_ID = "bedrock_handshake";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static List<String> MODS_WHITELIST;
	public static List<String> PACKS_WHITELIST;
	public static List<String> PLAYERS_WHITELIST;

	@Override public void onInitialize()
	{
		BedrockHandshakeConfig config = BedrockHandshakeConfigLoader.loadConfig();
		MODS_WHITELIST = config.modsWhitelist();
		PACKS_WHITELIST = config.packsWhitelist();
		PLAYERS_WHITELIST = config.playersWhitelist();

		BedrockHandshaketems.load();
		BedrockHandshakeTrackedData.load();
		BedrockHandshakeNetworking.registerData();
		BedrockHandshakeNetworking.registerServerReceiver();
		BedrockHandshakePlayerEvents.registerPlayerEvents();
		BedrockHandshakeServerEvents.registerServerEvents();
	}

	@Override public void onInitializeClient()
	{
		ClientBedrockHandshakeNetworking.registerClientReceiver();
		BedrockHandshakeClientEvents.registerClientPlayEvents();
	}

	public static Identifier id(String path) {return Identifier.of(MOD_ID, path);}
}