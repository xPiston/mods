package fr.craftechmc.needs.common.properties;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by arisu on 11/07/2016.
 */
public abstract class TimedProperty extends Property
{
    private int remainingTicks = 0;

    protected TimedProperty(final EntityPlayer player)
    {
        super(player);
    }

    public boolean isActive()
    {
        return this.remainingTicks != 0;
    }

    public void tick()
    {
        if (this.remainingTicks > 0)
            this.remainingTicks--;
        this.update();
    }

    abstract void update();

    @Override
    public void save(final NBTTagCompound compound, final String tagName)
    {
        compound.setInteger(tagName + "RemainingTicks", this.remainingTicks);
    }

    @Override
    public void load(final NBTTagCompound compound, final String tagName)
    {
        this.remainingTicks = compound.getInteger(tagName + "RemainingTicks");
    }

    public int getRemainingTicks()
    {
        return this.remainingTicks;
    }

    public void setRemainingTicks(final int remainingTicks)
    {
        this.remainingTicks = remainingTicks;
    }
}