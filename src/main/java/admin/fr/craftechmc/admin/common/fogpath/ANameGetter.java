package fr.craftechmc.admin.common.fogpath;

/**
 * Used to generify input selectable elements Created by arisu on 24/07/2016.
 */
public abstract class ANameGetter
{
    private String name;

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }
}