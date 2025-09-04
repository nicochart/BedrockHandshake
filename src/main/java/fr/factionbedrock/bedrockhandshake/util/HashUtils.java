package fr.factionbedrock.bedrockhandshake.util;

import net.minecraft.resource.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashUtils
{
    public static void listActivePacks(ResourcePackManager resourcePackManager, String resourcePackDirectoryPath)
    {
        List<String> packNames = BedrockHandshakeHelper.getLoadedResourcePacksList(resourcePackManager);

        for (String name : packNames)
        {
            String pathToResourcePack = resourcePackDirectoryPath + File.separator + name;
            System.out.println("Resource pack : " + name);
            System.out.println("Path : " + pathToResourcePack);
            printPackSHA(pathToResourcePack);
        }
    }

    public static void printPackSHA(String chemin)
    {
        File file = new File(chemin);
        String hash = getSHA256(file);
        System.out.println("SHA-256: " + hash);
    }

    public static String getSHA256(File file)
    {
        MessageDigest digest;
        try {digest = MessageDigest.getInstance("SHA-256");}
        catch (NoSuchAlgorithmException e) {return "Error - SHA-256 Algorithm not found (not available in this JVM)";}

        FileInputStream fileReader;
        try {fileReader = new FileInputStream(file);}
        catch (FileNotFoundException e) {return "Error - Pack File not found";}

        byte[] byteArray = new byte[1024];
        int bytesCount;

        try {while ((bytesCount = fileReader.read(byteArray)) != -1) {digest.update(byteArray, 0, bytesCount);} fileReader.close();}
        catch (IOException e) {return "Error - Unable to read Pack File: ";}

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {sb.append(String.format("%02x", b));}

        return sb.toString();
    }
}
