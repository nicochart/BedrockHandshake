package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BedrockHandshakeHelper
{
    public static List<String> getLoadedModsList()
    {
        List<String> list = new ArrayList<>();

        for (ModContainer mod : FabricLoader.getInstance().getAllMods())
        {
            String modId = mod.getMetadata().getId();
            if (!modId.contains("fabric") && !modId.equals("java") && !modId.equals("minecraft") && !modId.equals("mixinextras")) {list.add(modId);}
        }
        return list;
    }

    public static List<String> getLoadedResourcePacksNameList(ResourcePackManager resourcePackManager)
    {
        //temporary solution to avoid mod list to appear in loaded packs
        List<String> loadedModIds = getLoadedModsList();
        List<String> loadedResourcePacks = new ArrayList<>();

        Collection<ResourcePackProfile> enabledPacks = resourcePackManager.getEnabledProfiles();
        if (enabledPacks.isEmpty()) {return loadedResourcePacks;}
        for (ResourcePackProfile profile : enabledPacks)
        {
            if (!profile.getId().contains("fabric") && !loadedModIds.contains(profile.getId())) {loadedResourcePacks.add(profile.getDisplayName().getString());}
        }
        return loadedResourcePacks;
    }

    public static List<String> getLoadedResourcePacksIdList(ResourcePackManager resourcePackManager)
    {
        //temporary solution to avoid mod list to appear in loaded packs
        List<String> loadedModIds = getLoadedModsList();
        List<String> loadedResourcePacks = new ArrayList<>();

        Collection<ResourcePackProfile> enabledPacks = resourcePackManager.getEnabledProfiles();
        if (enabledPacks.isEmpty()) {return loadedResourcePacks;}
        for (ResourcePackProfile profile : enabledPacks)
        {
            if (!profile.getId().contains("fabric") && !loadedModIds.contains(profile.getId())) {loadedResourcePacks.add(profile.getId());}
        }
        return loadedResourcePacks;
    }

    //temporary - needs to be logged instead
    public static void messageLoadedThingsToPlayer(PlayerEntity player, List<String> modsList, List<String> packsList)
    {
        messageLoadedModsFromList(player, modsList);
        messageLoadedPacksFromList(player, packsList);
    }

    public static void messageLoadedModsFromList(PlayerEntity player, List<String> list)
    {
        messageListToPlayer("mods", player, list);
    }

    public static void messageLoadedPacksFromList(PlayerEntity player, List<String> list)
    {
        messageListToPlayer("packs", player, list);
    }

    public static void messageListToPlayer(String category, PlayerEntity player, List<String> list)
    {
        String stringToSend = "";
        if (list.isEmpty()) {stringToSend = "0 loaded " + category;}
        else
        {
            int numberOfNotFabricMobs = list.size();
            for (String modId : list)
            {
                stringToSend += "\""+modId+"\", ";
            }
            stringToSend = numberOfNotFabricMobs + " loaded " + category + " : " + stringToSend;
        }

        if (stringToSend.endsWith(", ")) {stringToSend = stringToSend.substring(0, stringToSend.length() - 2);}
        player.sendMessage(Text.literal(stringToSend), false);
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
        List<String> loadedModIds = getLoadedModsList();

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

    public static int getInfractionCount(PlayerEntity player)
    {
        return player.getDataTracker().get(BedrockHandshakeTrackedData.INFRACTION_COUNT);
    }

    public static void increaseInfractionCount(PlayerEntity player)
    {
        player.getDataTracker().set(BedrockHandshakeTrackedData.INFRACTION_COUNT, getInfractionCount(player) + 1);
    }

    public static boolean isDedicated(MinecraftServer server)
    {
        return server instanceof DedicatedServer;
    }
}
