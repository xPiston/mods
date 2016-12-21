package fr.craftechmc.needs.common.properties;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by arisu on 28/06/2016.
 */
public abstract class Property
{
    protected final EntityPlayer player;

    protected Property(EntityPlayer player)
    {
        this.player = player;
    }

    public abstract void save(NBTTagCompound compound, String tagName);

    public abstract void load(NBTTagCompound compound, String tagName);
}
