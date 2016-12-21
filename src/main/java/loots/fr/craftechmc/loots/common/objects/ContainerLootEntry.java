package fr.craftechmc.loots.common.objects;

import fr.craftechmc.loots.common.storage.LootItem;

public class ContainerLootEntry
{
    private LootItem item;
    private int      quantityMin;
    private int      quantityMax;
    private double   chance;

    public LootItem getItem()
    {
        return this.item;
    }

    public void setItem(final LootItem item)
    {
        this.item = item;
    }

    public int getQuantityMin()
    {
        return this.quantityMin;
    }

    public void setQuantityMin(final int quantityMin)
    {
        this.quantityMin = quantityMin;
    }

    public int getQuantityMax()
    {
        return this.quantityMax;
    }

    public void setQuantityMax(final int quantityMax)
    {
        this.quantityMax = quantityMax;
    }

    public double getChance()
    {
        return this.chance;
    }

    public void setChance(final double chance)
    {
        this.chance = chance;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.chance);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (this.item == null ? 0 : this.item.hashCode());
        result = prime * result + this.quantityMax;
        result = prime * result + this.quantityMin;
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
        final ContainerLootEntry other = (ContainerLootEntry) obj;
        if (Double.doubleToLongBits(this.chance) != Double.doubleToLongBits(other.chance))
            return false;
        if (this.item == null)
        {
            if (other.item != null)
                return false;
        }
        else if (!this.item.equals(other.item))
            return false;
        if (this.quantityMax != other.quantityMax)
            return false;
        if (this.quantityMin != other.quantityMin)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "ContainerLootEntry [item=" + this.item + ", quantityMin=" + this.quantityMin + ", quantityMax="
                + this.quantityMax + ", chance=" + this.chance + "]";
    }
}