package fr.factionbedrock.bedrockhandshake.item;

import fr.factionbedrock.bedrockhandshake.client.network.ClientBedrockHandshakeNetworking;
import fr.factionbedrock.bedrockhandshake.client.util.ClientHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item
{
    public DebugItem(Properties settings) {super(settings);}

    @Override public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand)
    {
        if (world.isClientSide())
        {
            ClientBedrockHandshakeNetworking.sendPacketFromClient(ClientHelper.createAdminHandshakePacket());
        }

        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}