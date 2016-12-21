package fr.craftechmc.contentmod.common.multiblocks;

import fr.craftechmc.core.common.tiles.TileCraftechBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TileModelMultiBlockGag extends TileCraftechBase implements IMultiBlockTileGag
{
    private String        unlocalizedCoreName = " ";
    private final boolean needSelectionHandling;
    private int           xCore, yCore, zCore;

    public TileModelMultiBlockGag()
    {
        this.needSelectionHandling = true;
        this.xCore = -1;
        this.yCore = -1;
        this.zCore = -1;
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("xCore", this.xCore);
        tag.setInteger("yCore", this.yCore);
        tag.setInteger("zCore", this.zCore);

        tag.setString("unlocalizedCoreName", this.unlocalizedCoreName);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.xCore = tag.getInteger("xCore");
        this.yCore = tag.getInteger("yCore");
        this.zCore = tag.getInteger("zCore");

        this.unlocalizedCoreName = tag.getString("unlocalizedCoreName");
    }

    @Override
    public void destroyCore()
    {
        this.worldObj.setBlockToAir(this.xCore, this.yCore, this.zCore);
        this.worldObj.removeTileEntity(this.xCore, this.yCore, this.zCore);
    }

    @Override
    public boolean isCorePresent()
    {
        if (this.worldObj == null)
            return false;
        final IMultiBlockTile tile = (IMultiBlockTile) this.worldObj.getTileEntity(this.xCore, this.yCore, this.zCore);

        if (tile != null && tile.isCore())
            return true;
        return false;
    }

    public int getxCore()
    {
        return this.xCore;
    }

    @Override
    public void setxCore(int xCore)
    {
        this.xCore = xCore;
    }

    public int getyCore()
    {
        return this.yCore;
    }

    @Override
    public void setyCore(int yCore)
    {
        this.yCore = yCore;
    }

    public int getzCore()
    {
        return this.zCore;
    }

    @Override
    public void setzCore(int zCore)
    {
        this.zCore = zCore;
    }

    @Override
    public AxisAlignedBB getSelectionBox()
    {
        if (this.isCorePresent())
            return ((IMultiBlockTile) this.worldObj.getTileEntity(this.xCore, this.yCore, this.zCore))
                    .getSelectionBox();
        else
            return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1,
                    this.zCoord + 1);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.isCorePresent())
            return ((IMultiBlockTile) this.worldObj.getTileEntity(this.xCore, this.yCore, this.zCore))
                    .getSelectionBox();
        else
            return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1,
                    this.zCoord + 1);
    }

    @Override
    public boolean isCore()
    {
        return false;
    }

    @Override
    public boolean needSelectionHandling()
    {
        return this.needSelectionHandling;
    }

    public String getUnlocalizedCoreName()
    {
        return this.unlocalizedCoreName;
    }

    @Override
    public void setUnlocalizedCoreName(String unlocalizedCoreName)
    {
        this.unlocalizedCoreName = unlocalizedCoreName;
    }

    @Override
    public TileModelMultiBlockCore getCore()
    {
        return (TileModelMultiBlockCore) this.worldObj.getTileEntity(this.xCore, this.yCore, this.zCore);
    }
}