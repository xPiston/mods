package fr.craftechmc.zombie.common.sound;

import fr.craftechmc.zombie.common.util.Util;

public class SoundModifierHandler
{
    /**
     * Return the sound value when player is sprinting
     * 
     * @param value
     *            : current sound value
     * @return sound value
     */
    public static double getSprintValue(double value)
    {
        return value += Util.SPRINT;
    }

    /**
     * Return the sound value when player is jumping
     * 
     * @param sprint
     *            : sprint value
     * @return sound value
     */
    public static double getJumpValue(double sprint)
    {
        return Math.min(0.75 * sprint, 1.5);
    }

    /**
     * Return the sound value when player is falling
     * 
     * @param jump
     *            : jump value
     * @param distance
     *            : distance fall
     * @return sound value
     */
    public static double getFallValue(double jump, double distance)
    {
        return jump + Math.log10(distance - 1);
    }
}
