package fr.craftechmc.loots.common.objects;

import java.util.ArrayList;

import com.google.common.collect.Lists;

public class ContainerLootTable
{
    private String                             lootTableName;
    private final ArrayList<ContainerLootList> lootLists = Lists.newArrayList();

    public String getLootTableName()
    {
        return this.lootTableName;
    }

    public void setLootTableName(final String lootTableName)
    {
        this.lootTableName = lootTableName;
    }

    public ArrayList<ContainerLootList> getLootLists()
    {
        return this.lootLists;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.lootLists == null ? 0 : this.lootLists.hashCode());
        result = prime * result + (this.lootTableName == null ? 0 : this.lootTableName.hashCode());
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
        final ContainerLootTable other = (ContainerLootTable) obj;
        if (this.lootLists == null)
        {
            if (other.lootLists != null)
                return false;
        }
        else if (!this.lootLists.equals(other.lootLists))
            return false;
        if (this.lootTableName == null)
        {
            if (other.lootTableName != null)
                return false;
        }
        else if (!this.lootTableName.equals(other.lootTableName))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "ContainerLootTable [lootTableName=" + this.lootTableName + ", lootLists=" + this.lootLists + "]";
    }

}