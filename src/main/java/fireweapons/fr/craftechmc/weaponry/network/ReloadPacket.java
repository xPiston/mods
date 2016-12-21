package fr.craftechmc.weaponry.network;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.craftechmc.weaponry.CraftechWeaponry;
import fr.craftechmc.weaponry.items.WeaponDescribed;
import fr.craftechmc.weaponry.server.WeaponFireSystem;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class ReloadPacket extends AbstractPacket
{

    @Override
    public void encodeInto(final ChannelHandlerContext ctx, final ByteBuf buffer)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void decodeInto(final ChannelHandlerContext ctx, final ByteBuf buffer)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleClientSide(final EntityPlayer player)
    {
        Logger.getLogger(CraftechWeaponry.MODNAME).log(Level.SEVERE,
                "The packet ReloadPacket was never supposed to go on the client side !");

    }

    @Override
    public void handleServerSide(final EntityPlayer player)
    {
        WeaponFireSystem.setReload(player.getDisplayName());
        if (player.getCurrentEquippedItem() != null
                && player.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            for (int i = 0; i < player.inventory.getSizeInventory()
                    && player.getCurrentEquippedItem().getMetadata() > 0; i++)
                if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i)
                        .getItem() == ((WeaponDescribed) player.getCurrentEquippedItem().getItem()).getLoader())
                    if (player.inventory.getStackInSlot(i).getMaxDurability()
                            - player.inventory.getStackInSlot(i).getMetadata() < player.getCurrentEquippedItem()
                                    .getMetadata())
                    {
                        player.getCurrentEquippedItem()
                                .setMetadata(player.getCurrentEquippedItem().getMetadata()
                                        - (player.inventory.getStackInSlot(i).getMaxDurability()
                                                - player.inventory.getStackInSlot(i).getMetadata()));
                        player.inventory.setInventorySlotContents(i, null);
                    }
                    else if (player.inventory.getStackInSlot(i).getMaxDurability()
                            - player.inventory.getStackInSlot(i).getMetadata() == player.getCurrentEquippedItem()
                                    .getMetadata())
                    {
                        player.inventory.setInventorySlotContents(i, null);
                        player.getCurrentEquippedItem().setMetadata(0);
                        break;
                    }
                    else if (player.inventory.getStackInSlot(i).getMaxDurability()
                            - player.inventory.getStackInSlot(i).getMetadata() > player.getCurrentEquippedItem()
                                    .getMetadata())
                    {
                        player.inventory.getStackInSlot(i).damageItem(player.getCurrentEquippedItem().getMetadata(),
                                player);
                        player.getCurrentEquippedItem().setMetadata(0);
                        break;
                    }
    }

}
