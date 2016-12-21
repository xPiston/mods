package fr.craftechmc.core.client.render;

import net.minecraft.block.Block;

public class TESRIndex
{
    Block   block;
    int     metadata;
    boolean advancedRender;

    public TESRIndex(Block block, int metadata, boolean advancedRender)
    {
        this.block = block;
        this.metadata = metadata;
        this.advancedRender = advancedRender;
    }

    @Override
    public int hashCode()
    {
        return this.block.hashCode() + this.metadata;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TESRIndex))
            return false;

        final TESRIndex tesr = (TESRIndex) o;

        return tesr.block == this.block && tesr.metadata == this.metadata;
    }

    public boolean useAdvancedRender()
    {
        return this.advancedRender;
    }

    public void setAdvancedRender(boolean advancedRender)
    {
        this.advancedRender = advancedRender;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public void setBlock(Block block)
    {
        this.block = block;
    }
}