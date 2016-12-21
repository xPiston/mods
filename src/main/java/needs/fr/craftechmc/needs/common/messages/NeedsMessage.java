package fr.craftechmc.needs.common.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import fr.craftechmc.needs.common.properties.NeedsProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by arisu on 23/06/2016.
 * <p>
 * This is a Packet : it's used to synchronize the player's NBTs from the server to the client
 */
public class NeedsMessage implements IMessage
{
    protected NBTTagCompound data;

    /**
     * This constructor is required for the SimpleNetworkWrapper - don't remove it.
     */
    public NeedsMessage()
    {
    }

    public NeedsMessage(EntityPlayer player)
    {
        data = new NBTTagCompound();
        NeedsProperties.get(player).saveNBTData(data);
        System.out.println(data.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, data);
    }

}
