package fr.factionbedrock.bedrockhandshake.client.registry;

import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.client.packet.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.packet.BedrockHandshakeNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BedrockHandshakeKeyBinds
{
    public static final KeyBinding TEMPORARY_HANDSHAKE_KEY = new KeyBinding(
            "key."+ BedrockHandshake.MOD_ID+".temporary_handshake_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category."+ BedrockHandshake.MOD_ID
    );

    public static void registerKeybinds()
    {
        KeyBindingHelper.registerKeyBinding(TEMPORARY_HANDSHAKE_KEY);
    }

    public static void registerPressedInteractions()
    {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TEMPORARY_HANDSHAKE_KEY.wasPressed()) {
                if (client.player != null)
                {
                    ClientBedrockHandshakeNetworking.sendPacketFromClient(BedrockHandshakeNetworking.HANDSHAKE_PACKET);
                }
            }
        });
    }
}
