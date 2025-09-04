package fr.factionbedrock.bedrockhandshake.util;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import net.minecraft.resource.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class HashUtils
{
    public static List<String> getActivePacksHashList(ResourcePackManager resourcePackManager, String resourcePackDirectoryPath)
    {
        List<String> packIds = BedrockHandshakeHelper.getLoadedResourcePacksIdList(resourcePackManager);
        List<String> hashList = new ArrayList<>();

        for (String id : packIds)
        {
            if (id.equals("vanilla") || id.equals("high_contrast") || id.equals("programmer_art")) {hashList.add(id);} //default vanilla packs, can't get hash
            else
            {
                String fileName = id;
                if (id.startsWith("file/")) {fileName = id.substring("file/".length());}
                else if (id.contains("/"))
                {
                    BedrockHandshake.LOGGER.info("Bedrock Handshake HashUtils error - strange resource pack id : "+id);
                }

                String pathToResourcePack = resourcePackDirectoryPath + File.separator + fileName;
                File resourcePackFile = new File(pathToResourcePack);
                if (Files.isDirectory(resourcePackFile.toPath())) {hashList.add("folder "+fileName);} //can't get a folder hash
                else {hashList.add(getSHA256(resourcePackFile));}
            }
        }
        return hashList;
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
