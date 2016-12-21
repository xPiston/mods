package fr.craftechmc.loots.common.objects;

import java.util.ArrayList;

import fr.craftechmc.core.common.objects.BlockPos;

public class LootZone
{
    private String              zoneName;
    private ArrayList<BlockPos> blocks;

    public String getZoneName()
    {
        return this.zoneName;
    }

    public void setZoneName(final String zoneName)
    {
        this.zoneName = zoneName;
    }

    public ArrayList<BlockPos> getBlocks()
    {
        return this.blocks;
    }

    public void setBlocks(final ArrayList<BlockPos> blocks)
    {
        this.blocks = blocks;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.blocks == null ? 0 : this.blocks.hashCode());
        result = prime * result + (this.zoneName == null ? 0 : this.zoneName.hashCode());
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
        final LootZone other = (LootZone) obj;
        if (this.blocks == null)
        {
            if (other.blocks != null)
                return false;
        }
        else if (!this.blocks.equals(other.blocks))
            return false;
        if (this.zoneName == null)
        {
            if (other.zoneName != null)
                return false;
        }
        else if (!this.zoneName.equals(other.zoneName))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "LootZone [zoneName=" + this.zoneName + ", blocks=" + this.blocks + "]";
    }
}