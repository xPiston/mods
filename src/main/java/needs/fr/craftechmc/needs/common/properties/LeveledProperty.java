package fr.craftechmc.needs.common.properties;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

/**
 * Created by arisu on 28/06/2016.
 */
public class LeveledProperty extends Property
{
    protected double           level;
    private double             maxLevel;
    protected final double     decayAmount;
    private final int          hurtInterval;
    private final DamageSource damageSource;

    private int                tick                     = 0;

    private float              currentPlayerDamage      = 0;
    private int                currentPlayerDamageCount = 0;

    public LeveledProperty(final EntityPlayer player, final DamageSource damageSource, final double maxLevel,
            final double decayAmount, final int hurtInterval)
    {
        super(player);
        this.level = maxLevel;
        this.maxLevel = maxLevel;
        this.decayAmount = decayAmount;
        this.hurtInterval = hurtInterval;
        this.damageSource = damageSource;
    }

    public void tick()
    {
        this.tick++;
        if (this.tick >= this.hurtInterval)
            this.tick = 0;
    }

    /**
     * Decreases properly level then hurts the player
     *
     * @param additionalAmount
     */
    public void decay(final double additionalAmount)
    {
        final double amount = this.decayAmount + additionalAmount;

        if (this.level >= amount)
        {
            this.level -= amount;
            if (this.tick == 0)
            {
                this.currentPlayerDamageCount = 0;
                this.currentPlayerDamage = 0;
            }
        }
        else
        {
            this.level = 0d;
            if (this.tick == 0)
                this.damagePlayer();
        }
    }

    /**
     * Used to damage the player increasingly then decreasingly till death
     */
    private void damagePlayer()
    {
        if (this.player.getHealth() > 5.0)
        {
            if (this.currentPlayerDamageCount < 2)
                this.currentPlayerDamage = 0.5f;
            else if (this.currentPlayerDamageCount < 4)
                this.currentPlayerDamage = 1.0f;
            else
                this.currentPlayerDamage = 2.0f;
        }
        else
        {
            if (this.currentPlayerDamage > 1.0)
                this.currentPlayerDamage = 1.0f;
            else if (this.currentPlayerDamage > 0.5f)
                this.currentPlayerDamage = 0.5f;
            else
                this.currentPlayerDamage = 0.25f;
        }
        this.player.attackEntityFrom(this.damageSource, this.currentPlayerDamage);
        this.currentPlayerDamageCount++;
    }

    public void replenish(final double level)
    {
        this.level += level;
        if (this.level > this.maxLevel)
            this.level = this.maxLevel;
    }

    @Override
    public void save(final NBTTagCompound compound, final String tagName)
    {
        compound.setDouble(tagName + "Level", this.level);
        compound.setFloat(tagName + "CurrentPlayerDamage", this.currentPlayerDamage);
        compound.setInteger(tagName + "CurrentPlayerDamageCount", this.currentPlayerDamageCount);
    }

    @Override
    public void load(final NBTTagCompound compound, final String tagName)
    {
        this.setLevel(compound.getDouble(tagName + "Level"));
        this.currentPlayerDamage = compound.getFloat(tagName + "CurrentPlayerDamage");
        this.currentPlayerDamageCount = compound.getInteger(tagName + "CurrentPlayerDamageCount");
    }

    public double getLevel()
    {
        return this.level;
    }

    public void setLevel(final double level)
    {
        this.level = level;
    }

    public double getMaxLevel()
    {
        return this.maxLevel;
    }

    public void setMaxLevel(final double maxLevel)
    {
        this.maxLevel = maxLevel;
    }

    public int getTick()
    {
        return this.tick;
    }

    public void setTick(final int tick)
    {
        this.tick = tick;
    }
}