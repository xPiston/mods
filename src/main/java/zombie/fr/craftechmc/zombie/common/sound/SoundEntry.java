package fr.craftechmc.zombie.common.sound;

import net.minecraft.entity.Entity;

/**
 * Created by Phenix246 on 15/07/2016.
 */
public class SoundEntry
{
    public final double value;
    public final Entity sender;

    private double      interest;
    private long        startTime;

    /**
     * Sound entry use for sound system. This entry represent a sound with all
     * information useful for handle it
     * 
     * @param sender
     * @param value
     */
    public SoundEntry(Entity sender, double value)
    {
        this.sender = sender;
        this.value = value;

        this.interest = value;
        this.startTime = System.currentTimeMillis();
    }

    public void update()
    {
        long nowTime = System.currentTimeMillis();
        long diff = nowTime - this.startTime;
        this.interest = value - Math.log10((double) diff / 1000);
    }

    public double getInterest()
    {
        return interest;
    }

    public String toString()
    {
        return "[" + this.sender.getCommandSenderName() + "] " + this.value + " - " + this.getInterest();
    }
}
