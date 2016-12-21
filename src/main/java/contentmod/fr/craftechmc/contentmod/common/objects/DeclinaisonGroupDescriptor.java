package fr.craftechmc.contentmod.common.objects;

import java.util.ArrayList;

import fr.craftechmc.contentmod.common.objects.BlockDescriptors.DeclinaisonDescriptor;

public class DeclinaisonGroupDescriptor
{
    private String                           name;
    private ArrayList<DeclinaisonDescriptor> descriptors = new ArrayList<>();

    public DeclinaisonGroupDescriptor()
    {
        super();
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public ArrayList<DeclinaisonDescriptor> getDescriptors()
    {
        return this.descriptors;
    }

    public void setDescriptors(final ArrayList<DeclinaisonDescriptor> descriptors)
    {
        this.descriptors = descriptors;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.descriptors == null ? 0 : this.descriptors.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
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
        final DeclinaisonGroupDescriptor other = (DeclinaisonGroupDescriptor) obj;
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
        return true;
    }

    @Override
    public String toString()
    {
        return "DeclinaisonGroupDescriptor [name=" + this.name + ", descriptors=" + this.descriptors + "]";
    }
}