package fr.craftechmc.needs.common.properties;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

/**
 * Created by arisu on 24/06/2016.
 */
public class SaturatedProperty extends LeveledProperty
{
    private double       saturation;
    private final double maxSaturation;

    public SaturatedProperty(final EntityPlayer player, final DamageSource damageSource, final double maxSaturation,
            final double maxLevel, final double decayAmount, final int hurtInterval)
    {
        super(player, damageSource, maxLevel, decayAmount, hurtInterval);
        this.saturation = maxSaturation;
        this.maxSaturation = maxSaturation;
    }

    /**
     * Decreases properly saturation, then level itself and finally hurts the
     * player
     *
     * @param additionalAmount
     */
    @Override
    public void decay(final double additionalAmount)
    {
        double amount = this.decayAmount + additionalAmount;

        if (this.saturation >= amount)
        {
            this.saturation -= amount;
            super.decay(-this.decayAmount); // No decay, just triggers the
                                            // damage reset
        }
        else if (this.level >= amount && this.saturation > 0)
        {
            amount -= this.saturation;
            this.saturation = 0d;
            super.decay(amount - this.decayAmount);
        }
        else
            super.decay(additionalAmount);
    }

    public void replenish(final double saturation, final double level)
    {
        super.replenish(level);

        this.saturation += saturation;
        if (this.saturation > this.maxSaturation)
            this.saturation = this.maxSaturation;
    }

    @Override
    public void save(final NBTTagCompound compound, final String tagName)
    {
        super.save(compound, tagName);
        compound.setDouble(tagName + "Saturation", this.saturation);
    }

    @Override
    public void load(final NBTTagCompound compound, final String tagName)
    {
        super.load(compound, tagName);
        this.setSaturation(compound.getDouble(tagName + "Saturation"));
    }

    public double getMaxSaturation()
    {
        return this.maxSaturation;
    }

    public double getSaturation()
    {
        return this.saturation;
    }

    public void setSaturation(final double saturation)
    {
        this.saturation = saturation;
    }
}