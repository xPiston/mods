package fr.craftechmc.contentmod.common.blocks.customs;

import net.minecraft.block.Block;

/**
 *
 * @author Ourten
 *
 */
public class BlockObject
{
    private String          blockName;
    private transient Block block;
    private int             meta;

    public BlockObject(final String blockName, final Block block, final int meta)
    {
        super();
        this.blockName = blockName;
        this.block = block;
        this.meta = meta;
    }

    public BlockObject(final String blockName, final int meta)
    {
        this.blockName = blockName;
        this.meta = meta;
    }

    public Block getBlock()
    {
        if (this.block == null)
            return this.block = Block.getBlockFromName(this.blockName);
        return this.block;
    }

    public String getBlockName()
    {
        return this.blockName;
    }

    public void setBlockName(final String blockName)
    {
        this.blockName = blockName;
    }

    public int getMeta()
    {
        return this.meta;
    }

    public void setMeta(final int meta)
    {
        this.meta = meta;
    }

    public void setBlock(final Block block)
    {
        this.block = block;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.blockName == null ? 0 : this.blockName.hashCode());
        result = prime * result + this.meta;
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final BlockObject other = (BlockObject) obj;
        if (this.blockName == null)
        {
            if (other.blockName != null)
                return false;
        }
        else if (!this.blockName.equals(other.blockName))
            return false;
        if (this.meta != other.meta)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "BlockObject [blockName=" + this.blockName + ", meta=" + this.meta + "]";
    }
}