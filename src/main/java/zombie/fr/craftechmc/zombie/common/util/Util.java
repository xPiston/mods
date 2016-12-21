package fr.craftechmc.zombie.common.util;

import org.yggard.brokkgui.paint.Color;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;

/**
 * Created by Phenix246 on 20/06/2016.
 */
public class Util
{
    /**
     * Constants for sound system
     */
    public static final double SPRINT = 1.5;

    public static void registerZombie(final Object mod, final Class<? extends Entity> clazz, final String name,
            final Color foregroundColor, final Color backgroundColor)
    {
        final int id = EntityRegistry.findGlobalUniqueEntityId();
        final int trackingRange = 40;
        final int updateFrequency = 1;
        EntityRegistry.registerGlobalEntityID(clazz, name, id, backgroundColor.toInt(), foregroundColor.toInt());
        EntityRegistry.registerModEntity(clazz, name, id, mod, trackingRange, updateFrequency, true);
    }
}
