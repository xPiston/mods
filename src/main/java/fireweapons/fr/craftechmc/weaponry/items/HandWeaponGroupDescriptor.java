package fr.craftechmc.weaponry.items;

import java.util.ArrayList;

/**
 * Created by arisu on 04/09/2016.
 */
public class HandWeaponGroupDescriptor
{
    private ArrayList<HandWeaponDescriptor> descriptors = new ArrayList<>();
    private String                          name;

    public HandWeaponGroupDescriptor()
    {
        super();
    }

    public ArrayList<HandWeaponDescriptor> getDescriptors()
    {
        return this.descriptors;
    }

    public void setDescriptors(final ArrayList<HandWeaponDescriptor> descriptors)
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
        final HandWeaponGroupDescriptor other = (HandWeaponGroupDescriptor) obj;
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
        return "HandWeaponGroupDescriptor [descriptors=" + this.descriptors + ", name=" + this.name + "]";
    }
}