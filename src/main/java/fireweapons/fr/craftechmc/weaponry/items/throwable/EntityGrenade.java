package fr.craftechmc.weaponry.items.throwable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * Created by arisu on 15/09/2016.
 */
public class EntityGrenade extends ThrowingWeapon
{
    public EntityGrenade(World world)
    {
        super(world);
    }

    public EntityGrenade(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }
}
