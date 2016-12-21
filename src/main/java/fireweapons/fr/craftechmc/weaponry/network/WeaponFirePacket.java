package fr.craftechmc.weaponry.network;

import fr.craftechmc.weaponry.CraftechWeaponry;
import fr.craftechmc.weaponry.items.WeaponDescribed;
import fr.craftechmc.weaponry.server.WeaponFireSystem;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WeaponFirePacket extends AbstractPacket
{
    private boolean fire;

    public WeaponFirePacket()
    {

    }

    public WeaponFirePacket(boolean fire)
    {
        this.fire = fire;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeBoolean(this.fire);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.fire = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        Logger.getLogger(CraftechWeaponry.MODNAME).log(Level.SEVERE,
                "The packet WeaponFirePacket was never supposed to go on the client side !");
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        if (this.fire)
        {
            if (player.getCurrentEquippedItem() != null
                    && player.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            {
                if (!WeaponFireSystem.isFiring(player.getDisplayName()))
                    WeaponFireSystem.setFiring(player.getDisplayName());
            }
        }
        else
        {
            if (player.getCurrentEquippedItem() != null
                    && player.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            {
                if (WeaponFireSystem.isFiring(player.getDisplayName()))
                    WeaponFireSystem.removeFiring(player.getDisplayName());
            }
        }
    }
}