package fr.craftechmc.core.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileCraftechBase extends TileEntity
{
    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    public NBTTagCompound getTag()
    {
        final NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return tag;
    }
}