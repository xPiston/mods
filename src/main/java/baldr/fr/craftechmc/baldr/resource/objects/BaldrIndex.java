package fr.craftechmc.baldr.resource.objects;

import java.util.ArrayList;

public class BaldrIndex
{
    private final ArrayList<String> contentDescriptors;

    public BaldrIndex()
    {
        super();
        this.contentDescriptors = new ArrayList<>();
    }

    public ArrayList<String> getContentDescriptors()
    {
        return this.contentDescriptors;
    }

    @Override
    public String toString()
    {
        return "BaldrIndex [contentDescriptors=" + this.contentDescriptors + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.contentDescriptors == null ? 0 : this.contentDescriptors.hashCode());
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
        final BaldrIndex other = (BaldrIndex) obj;
        if (this.contentDescriptors == null)
        {
            if (other.contentDescriptors != null)
                return false;
        }
        else if (!this.contentDescriptors.equals(other.contentDescriptors))
            return false;
        return true;
    }
}