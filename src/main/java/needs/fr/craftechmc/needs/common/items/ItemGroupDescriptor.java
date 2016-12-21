package fr.craftechmc.needs.common.items;

import java.util.ArrayList;

/**
 * Created by arisu on 25/06/2016.
 */
public class ItemGroupDescriptor
{
    private ArrayList<ItemDescriptor> descriptors = new ArrayList<>();
    private String                    name;
    private ENeedsItemType            type;

    public ItemGroupDescriptor()
    {
        super();
    }

    public ArrayList<ItemDescriptor> getDescriptors()
    {
        return this.descriptors;
    }

    public void setDescriptors(final ArrayList<ItemDescriptor> descriptors)
    {
        this.descriptors = descriptors;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public ENeedsItemType getType()
    {
        return this.type;
    }

    public void setType(final ENeedsItemType type)
    {
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.descriptors == null ? 0 : this.descriptors.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + (this.type == null ? 0 : this.type.hashCode());
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
        final ItemGroupDescriptor other = (ItemGroupDescriptor) obj;
        if (this.descriptors == null)
        {
            if (other.descriptors != null)
                return false;
        }
        else if (!this.descriptors.equals(other.descriptors))
            return false;
        if (this.name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "ItemGroupDescriptor [descriptors=" + this.descriptors + ", name=" + this.name + ", type=" + this.type
                + "]";
    }
}