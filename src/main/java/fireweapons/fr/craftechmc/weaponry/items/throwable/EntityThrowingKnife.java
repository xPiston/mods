package fr.craftechmc.weaponry.items.throwable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * Created by arisu on 15/09/2016.
 */
public class EntityThrowingKnife extends ThrowingWeapon
{
    public EntityThrowingKnife(World world)
    {
        super(world);
    }

    public EntityThrowingKnife(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }
}
