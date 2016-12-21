package fr.craftechmc.weaponry.items.throwable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by arisu on 15/09/2016.
 */
public abstract class ThrowingWeapon extends EntityArrow
{
    protected EntityLivingBase thrower;

    public ThrowingWeapon(World world)
    {
        super(world);
    }

    public ThrowingWeapon(World world, EntityLivingBase thrower)
    {
        super(world);
        this.thrower = thrower;
        
        this.setPositionAndRotation(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ,
                thrower.rotationYaw, thrower.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        float power = 0.1f;
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper
                .cos(this.rotationPitch / 180.0F * (float) Math.PI) * power);
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper
                .cos(this.rotationPitch / 180.0F * (float) Math.PI) * power);
        this.motionY = (double) (-MathHelper.sin((this.rotationPitch) / 180.0F * (float) Math.PI) * power);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5f, 1.0F);
    }
}
