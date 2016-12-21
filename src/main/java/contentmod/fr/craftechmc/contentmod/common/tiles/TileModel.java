package fr.craftechmc.contentmod.common.tiles;

import fr.craftechmc.core.common.tiles.TileCraftechBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileModel extends TileCraftechBase
{
    private ForgeDirection orientation;

    public TileModel()
    {
        this.orientation = ForgeDirection.UNKNOWN;
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setString("orientation", this.orientation.name());
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.orientation = ForgeDirection.valueOf(tag.getString("orientation"));
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public ForgeDirection getOrientation()
    {
        return this.orientation;
    }

    public void setOrientation(final ForgeDirection orientation)
    {
        this.orientation = orientation;
    }
}