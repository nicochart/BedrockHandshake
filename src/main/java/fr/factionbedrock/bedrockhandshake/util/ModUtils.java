package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.network.handshake.ModInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.List;

public class ModUtils
{
    public static List<String> getLoadedModIds()
    {
        List<String> modIds = new ArrayList<>();
        for (ModInfo mod : getLoadedMods())
        {
            modIds.add(mod.modId());
        }
        return modIds;
    }

    public static List<ModInfo> getLoadedMods()
    {
        List<ModInfo> loadedModIds = new ArrayList<>();
        for (ModContainer mod : getLoadedModsList())
        {
            loadedModIds.add(new ModInfo(mod.getMetadata().getId(), mod.getMetadata().getName(), mod.getMetadata().getVersion().getFriendlyString()));
        }
        return loadedModIds;
    }

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
}
