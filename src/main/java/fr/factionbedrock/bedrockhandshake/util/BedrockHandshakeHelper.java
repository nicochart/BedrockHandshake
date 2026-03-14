package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.registry.BedrockHandshakeTrackedData;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BedrockHandshakeHelper
{
    public static List<ModContainer> getLoadedModsList()
    {
        List<ModContainer> list = new ArrayList<>();

        for (ModContainer mod : FabricLoader.getInstance().getAllMods())
        {
            String modId = mod.getMetadata().getId();
            if (!modId.contains("fabric") && !modId.equals("java") && !modId.equals("minecraft") && !modId.equals("mixinextras")) {list.add(mod);}
        }
        return list;
    }

    public static List<String> getLoadedModsIds()
    {
        List<String> loadedModIds = new ArrayList<>();
        for (ModContainer mod : getLoadedModsList())
        {
            loadedModIds.add(mod.getMetadata().getId());
        }
        return loadedModIds;
    }

    public static List<ResourcePackProfile> getLoadedResourcePacksList(ResourcePackManager resourcePackManager)
    {
        //temporary solution to avoid mod list to appear in loaded packs
        List<String> loadedModIds = getLoadedModsIds();
        List<ResourcePackProfile> loadedResourcePacks = new ArrayList<>();

        Collection<ResourcePackProfile> enabledPacks = resourcePackManager.getEnabledProfiles();
        if (enabledPacks.isEmpty()) {return loadedResourcePacks;}
        for (ResourcePackProfile profile : enabledPacks)
        {
            if (!profile.getId().contains("fabric") && !loadedModIds.contains(profile.getId())) {loadedResourcePacks.add(profile);}
        }
        return loadedResourcePacks;
    }

    public static void messageLoadedModsToPlayer(PlayerEntity player)
    {
        List<ModContainer> loadedMods = getLoadedModsList();
        int modCount = loadedMods.size();

        String numberOfMods = modCount + " loaded mods" + (modCount != 0 ? " :" : "");
        player.sendMessage(Text.literal(numberOfMods).styled(style -> style.withBold(true)), false);

        int modNumber = 0;

        for (ModContainer mod : loadedMods)
        {
            modNumber++;

            String modId = mod.getMetadata().getId();
            String name = mod.getMetadata().getName();
            String version = mod.getMetadata().getVersion().getFriendlyString();

            MutableText modInfo = Text.literal("Mod " + modNumber + " : ");

            Text modIdText = Text.literal(modId).styled(style -> style
                    .withClickEvent(new ClickEvent.CopyToClipboard(modId))
                    .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                    .withUnderline(true));

            modInfo.append("\"").append(name).append("\" : ").append(modIdText).append(" version ").append(version);

            player.sendMessage(modInfo, false);
        }
    }

    public static void messageLoadedResourcePacksToPlayer(ResourcePackManager resourcePackManager, PlayerEntity player)
    {

        List<ResourcePackProfile> enabledPacks = BedrockHandshakeHelper.getLoadedResourcePacksList(resourcePackManager);
        int loadedPacksCount = enabledPacks.size();

        String numberOfLoadedPacks = loadedPacksCount + " loaded packs" + (loadedPacksCount != 0 ? " : " : "");
        player.sendMessage(Text.literal(numberOfLoadedPacks).styled(style -> style.withBold(true)), false);

        int packNumber = 0;
        for (ResourcePackProfile profile : enabledPacks)
        {
            packNumber++;
            MutableText packInfo = Text.literal("Pack "+packNumber+" : ");
            String packSignature = ResourcePackUtils.getResourcePackInfo(profile.getId(), MinecraftClient.getInstance().getResourcePackDir().toFile().getAbsolutePath()).packSignature();

            Text hashText = Text.literal(packSignature).styled(style -> style
                                .withClickEvent(new ClickEvent.CopyToClipboard(packSignature))
                                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy")))
                                .withUnderline(true));

            packInfo.append("\"").append(profile.getDisplayName()).append("\" : ").append(profile.getDescription()).append(", ").append(hashText);

            player.sendMessage(packInfo, false);
        }
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
