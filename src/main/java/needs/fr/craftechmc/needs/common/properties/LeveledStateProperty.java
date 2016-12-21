package fr.craftechmc.needs.common.properties;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

/**
 * Created by arisu on 28/06/2016.
 */
public class LeveledStateProperty extends TimedProperty
{
    private double                         level    = 0.0;
    private double                         maxLevel = 1.0;
    private HashMap<Integer, PotionEffect> effects  = new HashMap<>();

    protected LeveledStateProperty(final EntityPlayer player, final double maxLevel)
    {
        super(player);
        this.maxLevel = maxLevel;
    }

    // BUILDER //
    public LeveledStateProperty addEffect(final int minLevel, final PotionEffect effect)
    {
        this.effects.put(minLevel, effect);
        return this;
    }
    // END BUILDER //

    @Override
    public boolean isActive()
    {
        return this.level > 0;
    }

    @Override
    void update()
    {
        if (this.isActive())
            this.effects.forEach((minValue, effect) ->
            {
                if (this.level >= minValue)
                    this.player.addPotionEffect(effect);
            });
        else
            this.effects.values().forEach(effect -> this.player.removePotionEffect(effect.getPotionID()));
    }

    @Override
    public void save(final NBTTagCompound compound, final String tagName)
    {
        super.save(compound, tagName);
        compound.setDouble(tagName + "Level", this.level);
    }

    @Override
    public void load(final NBTTagCompound compound, final String tagName)
    {
        super.load(compound, tagName);
        this.level = compound.getDouble(tagName + "Level");
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

    public HashMap<Integer, PotionEffect> getEffects()
    {
        return this.effects;
    }

    public void setEffects(final HashMap<Integer, PotionEffect> effects)
    {
        this.effects = effects;
    }
}