package fr.factionbedrock.bedrockhandshake.item;

import fr.factionbedrock.bedrockhandshake.client.network.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.client.util.ClientHelper;
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
            ClientBedrockHandshakeNetworking.sendPacketFromClient(ClientHelper.createHandshakePacket());
        }

        return ActionResult.SUCCESS;
    }
}