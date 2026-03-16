package fr.factionbedrock.bedrockhandshake.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BedrockHandshakeConfigLoader
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(BedrockHandshake.MOD_ID);
    private static final Path CONFIG_PATH = CONFIG_FOLDER.resolve("config.json");

    public static BedrockHandshakeConfig loadConfig()
    {
        if (!Files.exists(CONFIG_PATH))
        {
            BedrockHandshakeConfig defaultConfig = defaultConfig();
            saveConfig(defaultConfig);
            return defaultConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {return GSON.fromJson(reader, BedrockHandshakeConfig.class);}
        catch (IOException | JsonParseException e)
        {
            e.printStackTrace();
            BedrockHandshake.LOGGER.error("Failed to load Bedrock Handshake config file, using default configuration instead");
            return defaultConfig();
        }
    }

    public static BedrockHandshakeConfig defaultConfig()
    {
        ArrayList<String> modsWhitelist = new ArrayList<>();
        modsWhitelist.add("bedrock_handshake");
        ArrayList<String> packsWhitelist = new ArrayList<>();
        packsWhitelist.add("vanilla");
        packsWhitelist.add("high_contrast");
        packsWhitelist.add("programmer_art");
        ArrayList<String> playerWhitelist = new ArrayList<>();
        return new BedrockHandshakeConfig(false, 3, modsWhitelist, packsWhitelist, playerWhitelist);
    }

    public static void updateDoHandshakeOnIntegratedServer(boolean doHandshakeOnIntegratedServer)
    {
        updateConfig(doHandshakeOnIntegratedServer, BedrockHandshake.TOLERATED_INFRACTION_COUNT, BedrockHandshake.MODS_WHITELIST, BedrockHandshake.PACKS_WHITELIST, BedrockHandshake.PLAYERS_WHITELIST);
    }

    public static void updateToleratedInfractionCount(int toleratedInfractionCount)
    {
        updateConfig(BedrockHandshake.DO_HANDSHAKE_ON_INTEGRATED_SERVER, toleratedInfractionCount, BedrockHandshake.MODS_WHITELIST, BedrockHandshake.PACKS_WHITELIST, BedrockHandshake.PLAYERS_WHITELIST);
    }

    public static void updateModsWhitelist(List<String> modsWhitelist)
    {
        updateConfig(BedrockHandshake.DO_HANDSHAKE_ON_INTEGRATED_SERVER, BedrockHandshake.TOLERATED_INFRACTION_COUNT, modsWhitelist, BedrockHandshake.PACKS_WHITELIST, BedrockHandshake.PLAYERS_WHITELIST);
    }

    public static void updatePacksWhitelist(List<String> packsWhitelist)
    {
        updateConfig(BedrockHandshake.DO_HANDSHAKE_ON_INTEGRATED_SERVER, BedrockHandshake.TOLERATED_INFRACTION_COUNT, BedrockHandshake.MODS_WHITELIST, packsWhitelist, BedrockHandshake.PLAYERS_WHITELIST);
    }

    public static void updatePlayersWhitelist(List<String> playersWhitelist)
    {
        updateConfig(BedrockHandshake.DO_HANDSHAKE_ON_INTEGRATED_SERVER, BedrockHandshake.TOLERATED_INFRACTION_COUNT, BedrockHandshake.MODS_WHITELIST, BedrockHandshake.PACKS_WHITELIST, playersWhitelist);
    }

    public static void updateConfig(boolean doHandshakeOnIntegratedServer, int toleratedInfractionCount, List<String> modsWhitelist, List<String> packsWhitelist, List<String> playersWhitelist)
    {
        BedrockHandshakeConfig config = new BedrockHandshakeConfig(doHandshakeOnIntegratedServer, toleratedInfractionCount, modsWhitelist, packsWhitelist, playersWhitelist);
        saveConfig(config);
        BedrockHandshake.initializeConfig();
    }

    public static void saveConfig(BedrockHandshakeConfig config)
    {
        try
        {
            Files.createDirectories(CONFIG_FOLDER);
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {GSON.toJson(config, writer);}
        }
        catch (IOException e) {e.printStackTrace();}
    }
}
