package fr.craftechmc.contentmod.common.multiblocks;

import net.minecraft.util.AxisAlignedBB;

public interface IMultiBlockTile
{
    public abstract boolean isCore();

    public abstract AxisAlignedBB getSelectionBox();

    public abstract boolean needSelectionHandling();
}