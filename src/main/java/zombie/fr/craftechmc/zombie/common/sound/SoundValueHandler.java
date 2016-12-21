package fr.craftechmc.zombie.common.sound;

import fr.craftechmc.zombie.common.CraftechZombie;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class SoundValueHandler
{
    /**
     * 
     * @param entity
     * @return block under the entity
     */
    public static Block getBlockUnder(final EntityLivingBase entity)
    {
        final World world = entity.worldObj;
        final int xPos = (int) entity.posX;
        final int yPos = (int) (entity.posY - 0.5f);
        final int zPos = (int) entity.posZ;

        return world.getBlock(xPos, yPos, zPos);
    }

    /**
     * Return the sound value when player is sprinting
     * 
     * @param entity
     * @return sound value
     */
    public static double getSprintValue(final EntityLivingBase entity)
    {
        // Get block under the entity
        final Block block = SoundValueHandler.getBlockUnder(entity);

        // Get sound value
        final String name = block.stepSound.getStepSound();
        double value = CraftechZombie.soundManager.getSoundValue(entity, name);

        // Apply modifier
        value = SoundModifierHandler.getSprintValue(value);

        return value;
    }

    /**
     * Return the sound value when player is jumping
     * 
     * @param entity
     * @return sound value
     */
    public static double getJumpValue(final EntityLivingBase entity)
    {
        return SoundModifierHandler.getJumpValue(SoundValueHandler.getSprintValue(entity));
    }

    /**
     * Return the sound value when player is falling
     * 
     * @param entity
     * @param distance
     * @return sound value
     */
    public static double getFallValue(final EntityLivingBase entity, final double distance)
    {
        return SoundModifierHandler.getFallValue(SoundValueHandler.getJumpValue(entity), distance);
    }
}
