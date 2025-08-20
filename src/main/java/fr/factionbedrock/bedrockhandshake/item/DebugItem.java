package fr.factionbedrock.bedrockhandshake.item;

import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DebugItem extends Item
{
    public DebugItem(Settings settings) {super(settings);}

    @Override public ActionResult use(World world, PlayerEntity user, Hand hand)
    {
        if (world.isClient())
        {
            BedrockHandshakeHelper.messageLoadedModsToPlayer(user);
            BedrockHandshakeHelper.messageLoadedResourcePacksToPlayer(MinecraftClient.getInstance().getResourcePackManager(), user);
        }

        return ActionResult.SUCCESS;
    }
}