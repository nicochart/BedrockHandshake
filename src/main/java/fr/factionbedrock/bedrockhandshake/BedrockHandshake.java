package fr.factionbedrock.bedrockhandshake;

import fr.factionbedrock.bedrockhandshake.client.packet.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.client.registry.BedrockHandshakeKeyBinds;
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

public class BedrockHandshake implements ModInitializer, ClientModInitializer
{
	public static final String MOD_ID = "bedrock_handshake";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override public void onInitialize()
	{
		BedrockHandshaketems.load();
		BedrockHandshakeTrackedData.load();
		BedrockHandshakeNetworking.registerData();
		BedrockHandshakeNetworking.registerServerReceiver();
		BedrockHandshakePlayerEvents.registerPlayerEvents();
		BedrockHandshakeServerEvents.registerPlayerEvents();
	}

	@Override public void onInitializeClient()
	{
		BedrockHandshakeKeyBinds.registerKeybinds();
		BedrockHandshakeKeyBinds.registerPressedInteractions();
		ClientBedrockHandshakeNetworking.registerClientReceiver();
	}

	public static Identifier id(String path) {return Identifier.of(MOD_ID, path);}
}