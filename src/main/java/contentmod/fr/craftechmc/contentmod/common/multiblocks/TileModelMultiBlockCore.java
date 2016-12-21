package fr.craftechmc.contentmod.common.multiblocks;

import fr.craftechmc.core.common.tiles.TileCraftechBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileModelMultiBlockCore extends TileCraftechBase implements IMultiBlockTile
{
    private String        index = "";
    private final boolean needSelectionHandling;
    private AxisAlignedBB box;
    private Vec3          offset;

    public TileModelMultiBlockCore()
    {
        this.needSelectionHandling = true;
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
        tag.setString("name", this.index);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.index = tag.getString("name");
    }

    public AxisAlignedBB getBox()
    {
        if (this.box != null)
            return this.box;
        else
        {
            if (this.getBlockMetadata() == 0 || this.getBlockMetadata() == 2)
            {
                return (this.box = AxisAlignedBB.getBoundingBox(
                        this.xCoord + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getOffsetZ(),
                        this.yCoord + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getOffsetY(),
                        this.zCoord + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getOffsetX(),
                        this.xCoord
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getLength()
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getOffsetZ(),
                        this.yCoord
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getHeight()
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getOffsetY(),
                        this.zCoord
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getWidth()
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getOffsetX()));
            }
            else
            {
                return (this.box = AxisAlignedBB.getBoundingBox(
                        this.xCoord + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getOffsetX(),
                        this.yCoord + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getOffsetY(),
                        this.zCoord + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getOffsetZ(),
                        this.xCoord
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getWidth()
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getOffsetX(),
                        this.yCoord
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getHeight()
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getOffsetY(),
                        this.zCoord
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getLength()
                                + ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                        .getOffsetZ()));
            }
        }
    }

    @Override
    public AxisAlignedBB getSelectionBox()
    {
        return this.getBox();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return this.getBox();
    }

    @Override
    public boolean isCore()
    {
        return true;
    }

    @Override
    public boolean needSelectionHandling()
    {
        return this.needSelectionHandling;
    }

    public String getIndex()
    {
        return this.index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public Vec3 getRenderOffset()
    {
        if (this.offset != null)
            return this.offset;
        else
        {
            if (this.getBlockMetadata() == 0)
                return (this.offset = Vec3.createVectorHelper(
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetZ(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetY(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetX()));
            else if (this.getBlockMetadata() == 2)
                return (this.offset = Vec3.createVectorHelper(
                        -((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetZ(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetY(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetX()));
            else if (this.getBlockMetadata() == 1)
                return (this.offset = Vec3.createVectorHelper(
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetX(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetY(),
                        -((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetZ()));
            else
                return (this.offset = Vec3.createVectorHelper(
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetX(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetY(),
                        ((BlockModelMultiBlockDescribed) this.getBlockType()).getModels().get(this.index)
                                .getRenderOffsetZ()));
        }
    }
}