package fr.craftechmc.contentmod.common.objects;

import java.util.ArrayList;

import fr.craftechmc.contentmod.common.objects.BlockDescriptors.BasicMultiBlockDescriptor;

public class BasicMultiBlockGroupDescriptor
{
    private ArrayList<BasicMultiBlockDescriptor> descriptors = new ArrayList<>();
    private String                               name;
    private String                               material;

    public BasicMultiBlockGroupDescriptor()
    {
        super();
    }

    public ArrayList<BasicMultiBlockDescriptor> getDescriptors()
    {
        return this.descriptors;
    }

    public void setDescriptors(final ArrayList<BasicMultiBlockDescriptor> descriptors)
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

    public String getMaterial()
    {
        return this.material;
    }

    public void setMaterial(final String material)
    {
        this.material = material;
    }

    @Override
    public String toString()
    {
        return "BasicMultiBlockGroupDescriptor [descriptors=" + this.descriptors + ", name=" + this.name + ", material="
                + this.material + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.descriptors == null ? 0 : this.descriptors.hashCode());
        result = prime * result + (this.material == null ? 0 : this.material.hashCode());
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
        final BasicMultiBlockGroupDescriptor other = (BasicMultiBlockGroupDescriptor) obj;
        if (this.descriptors == null)
        {
            if (other.descriptors != null)
                return false;
        }
        else if (!this.descriptors.equals(other.descriptors))
            return false;
        if (this.material == null)
        {
            if (other.material != null)
                return false;
        }
        else if (!this.material.equals(other.material))
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
}