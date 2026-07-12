package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.network.handshake.ResourcePackInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

public class ResourcePackUtils
{
    public static List<ResourcePackInfo> getActivePacksInfo(PackRepository resourcePackManager, String resourcePackDirectoryPath)
    {
        List<Pack> loadedPackProfiles = getLoadedResourcePacksList(resourcePackManager);
        List<ResourcePackInfo> hashList = new ArrayList<>();

        for (Pack profile : loadedPackProfiles) {hashList.add(getResourcePackInfo(profile, resourcePackDirectoryPath));}
        return hashList;
    }

    public static List<Pack> getLoadedResourcePacksList(PackRepository resourcePackManager)
    {
        //temporary solution to avoid mod list to appear in loaded packs
        List<String> loadedModIds = ModUtils.getLoadedModIds();
        List<Pack> loadedResourcePacks = new ArrayList<>();

        Collection<Pack> enabledPacks = resourcePackManager.getSelectedPacks();
        if (enabledPacks.isEmpty()) {return loadedResourcePacks;}
        for (Pack profile : enabledPacks)
        {
            if (!profile.getId().contains("fabric") && !loadedModIds.contains(profile.getId())) {loadedResourcePacks.add(profile);}
        }
        return loadedResourcePacks;
    }

    public static ResourcePackInfo getResourcePackInfo(Pack profile, String resourcePackDirectoryPath)
    {
        return new ResourcePackInfo(getPackSignature(profile.getId(), resourcePackDirectoryPath), profile.getId(), profile.getDescription().getString());
    }

    public static String getPackSignature(String packId, String resourcePackDirectoryPath)
    {
        if (packId.equals("vanilla") || packId.equals("high_contrast") || packId.equals("programmer_art")) {return packId;} //default vanilla packs, can't get hash
        else
        {
            String fileName = packId;
            if (packId.startsWith("file/")) {fileName = packId.substring("file/".length());}
            else if (packId.contains("/"))
            {
                BedrockHandshake.LOGGER.info("Bedrock Handshake HashUtils error - strange resource pack fileName : "+packId);
            }

            String pathToResourcePack = resourcePackDirectoryPath + File.separator + fileName;
            File resourcePackFile = new File(pathToResourcePack);
            return Files.isDirectory(resourcePackFile.toPath()) ? "folder " + fileName : getSHA256(resourcePackFile);
            //if is directory : pack signature will be "folder" + folder name, because can't get a folder hash.
        }
    }

    public static String getSHA256(File file)
    {
        MessageDigest digest;
        try {digest = MessageDigest.getInstance("SHA-256");}
        catch (NoSuchAlgorithmException e) {return "Error - SHA-256 Algorithm not found (not available in this JVM)";}

        FileInputStream fileReader;
        try {fileReader = new FileInputStream(file);}
        catch (FileNotFoundException e) {return "Error - Pack File not found : "+file.getAbsolutePath();}

        byte[] byteArray = new byte[1024];
        int bytesCount;

        try {while ((bytesCount = fileReader.read(byteArray)) != -1) {digest.update(byteArray, 0, bytesCount);} fileReader.close();}
        catch (IOException e) {return "Error - Unable to read Pack File : "+file.getAbsolutePath();}

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {sb.append(String.format("%02x", b));}

        return sb.toString();
    }
}
