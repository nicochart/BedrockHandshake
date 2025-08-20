package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BedrockHandshakeHelper
{
    public static void runFunction(ServerPlayerEntity user, String functionName)
    {
        MinecraftServer server = user.server;
        ServerWorld world = user.getServerWorld();
        Identifier functionId = BedrockHandshake.id(functionName);

        Optional<CommandFunction<ServerCommandSource>> mcFunction = server.getCommandFunctionManager().getFunction(functionId);

        mcFunction.ifPresent(function -> {
            ServerCommandSource source = server.getCommandSource().withEntity(user).withWorld(world).withPosition(user.getPos()).withSilent();
            server.getCommandFunctionManager().execute(function, source);
        });
    }

    public static void messageLoadedModsToPlayer(PlayerEntity player)
    {
        int numberOfMods = FabricLoader.getInstance().getAllMods().size();
        int numberOfNotFabricMobs = 0;
        String loadedMods = "";
        for (ModContainer mod : FabricLoader.getInstance().getAllMods())
        {
            String modId = mod.getMetadata().getId();
            String name = mod.getMetadata().getName();
            String version = mod.getMetadata().getVersion().getFriendlyString();

            if (!modId.contains("fabric") && !modId.equals("java") && !modId.equals("minecraft") && !modId.equals("mixinextras"))
            {
                numberOfNotFabricMobs++;
                loadedMods += "\""+name+"\" : "+modId+" version "+version+" | ";
            }
        }

        loadedMods = ((numberOfNotFabricMobs == 0) ? "0 loaded mods" : numberOfNotFabricMobs + " loaded mods : ") + loadedMods;
        if (loadedMods.endsWith(" | ")) {loadedMods = loadedMods.substring(0, loadedMods.length() - 3);}
        player.sendMessage(Text.literal(loadedMods), false);
    }

    public static void messageLoadedResourcePacksToPlayer(ResourcePackManager resourcePackManager, PlayerEntity player)
    {
        //temporary solution to avoid mod list to appear in loaded packs
        List<String> loadedModIds = new ArrayList<>();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods())
        {
            String modId = mod.getMetadata().getId();
            if (!modId.contains("fabric") && !modId.equals("java") && !modId.equals("minecraft") && !modId.equals("mixinextras"))
            {
                loadedModIds.add(modId);
            }
        }

        String loadedPacks = "";
        int numberOfLoadedPacks = 0;
        Collection<ResourcePackProfile> enabledPacks = resourcePackManager.getEnabledProfiles();
        if (enabledPacks.isEmpty()) {loadedPacks = "0 loaded packs";}
        for (ResourcePackProfile profile : enabledPacks)
        {
            if (!profile.getId().contains("fabric") && !loadedModIds.contains(profile.getId()))
            {
                numberOfLoadedPacks++;

                loadedPacks += "\""+profile.getDisplayName().getString()+"\" : " + profile.getDescription().getString() + " | ";
            }
        }
        loadedPacks = ((numberOfLoadedPacks == 0) ? "0 loaded packs" : numberOfLoadedPacks + " loaded packs : ") + loadedPacks;
        if (loadedPacks.endsWith(" | ")) {loadedPacks = loadedPacks.substring(0, loadedPacks.length() - 3);}
        player.sendMessage(Text.literal(loadedPacks), false);
    }

    public static void increaseInfractionCount(PlayerEntity player)
    {
        int oldCount = player.getDataTracker().get(BedrockHandshakeTrackedData.INFRACTION_COUNT);
        player.getDataTracker().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, oldCount + 1);
    }
}
