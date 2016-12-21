package fr.craftechmc.zombie.common.sound;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;

public class SoundManager
{
    /**
     * Map which contain register sounds with their value
     */
    private HashMap<String, Double>   sounds;

    /**
     * List of modifiers
     */
    private List<ISoundValueModifier> modifiers;

    public SoundManager()
    {
        this.sounds = Maps.newHashMap();
        this.modifiers = Lists.newArrayList();
    }

    /**
     * Register the soudn into the system
     * 
     * @param name
     *            of the sound
     * @param value
     *            of the sound
     */
    public void registerSound(String name, double value)
    {
        // Check
        if (value <= 0)
            return;
        if (name == null || name.isEmpty())
            return;
        if (this.sounds.containsKey(name))
            return;

        // register sound
        this.sounds.put(name, value);
    }

    /**
     * Register the modifier into the system
     * 
     * @param modifier
     */
    public void registerModifier(ISoundValueModifier modifier)
    {
        // Check
        if (modifier == null)
            return;
        if (this.modifiers.contains(modifier))
            return;

        // register modifier
        this.modifiers.add(modifier);
    }

    /**
     * Remove the sound from the system
     * 
     * @param name
     *            of the sound
     */
    public void removeSound(String name)
    {
        if (this.sounds.containsKey(name))
            this.sounds.remove(name);
    }

    /**
     * Remove he modifier from the system
     * 
     * @param modifier
     */
    public void removeModifier(ISoundValueModifier modifier)
    {
        if (this.modifiers.contains(modifier))
            this.modifiers.remove(modifier);
    }

    /**
     * Get the sound value
     * 
     * @param name
     * @return the sound value if exist else 0
     */
    public double getSoundValue(Entity sender, String name)
    {
        double value = 0;
        if (this.sounds.containsKey(name))
            value = this.sounds.get(name);

        for (ISoundValueModifier modifier : this.modifiers)
        {
            value = modifier.modify(sender, name, value);
        }

        return value;
    }

    /**
     * Call when a sound is played. Use to send the information to entities
     * around.
     * 
     * @param entity
     * @param name
     */
    public void sendSound(Entity sender, String name, float volume, float pitch)
    {
        // get value of the sound
        double value = this.getSoundValue(sender, name);

        if (value > 0)
        {
            this.sendSound(sender, value);
        }
    }

    /**
     * Call when entities need to be notified
     * 
     * @param sender
     * @param value
     */
    public void sendSound(Entity sender, double value)
    {
        double distance = value * 2;

        // get entities around the sender
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(sender.posX, sender.posY, sender.posZ, sender.posX + 1,
                sender.posY + 2, sender.posZ + 1);
        box = box.expand(distance, 3, distance);

        @SuppressWarnings("unchecked")
        List<Entity> list = sender.worldObj.selectEntitiesWithinAABB(EntityLiving.class, box, selector);

        // send notification
        for (Entity listener : list)
        {
            // get sound value
            double ListenValue = getSoundValue(sender, listener, value);
            // check to not send the notification to itself and if the
            // entity doesn't listen the sound
            if (ListenValue > 0 && listener != sender)
            {
                ((ISoundListener) listener).processSound(sender, ListenValue);
            }
        }
    }

    /**
     * 
     * @param sender
     * @param listener
     * @param value
     * @return value of the sound after attenuation
     */
    public double getSoundValue(Entity sender, Entity listener, double value)
    {
        double x = Math.pow(sender.posX - listener.posX, 2);
        double y = Math.pow(sender.posY - listener.posY, 2);
        double z = Math.pow(sender.posZ - listener.posZ, 2);

        double distance3d = Math.sqrt(x + y + z);

        return value - (0.5 * distance3d);
    }

    /**
     * selector for sound manager (only entity implements with ISoundListener)
     */
    IEntitySelector selector = new IEntitySelector()
    {
        public boolean isEntityApplicable(Entity entity)
        {
            return entity instanceof ISoundListener && entity.isEntityAlive();
        }
    };

}
