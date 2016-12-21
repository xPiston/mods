package fr.craftechmc.weaponry.entity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.weaponry.CraftechWeaponry;
import fr.craftechmc.weaponry.client.fx.CWParticles;
import fr.craftechmc.weaponry.items.WeaponDescribed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

public class EntityBulletProjectile extends EntityThrowable
{
    final Random   rand  = new Random(System.nanoTime());
    private double range = 5;
    private double xBegin;
    private double yBegin;
    private double zBegin;
    private float damage;

    public EntityBulletProjectile(final World w)
    {
        super(w);
    }

    public EntityBulletProjectile(final World world, final EntityPlayer shootingEntity)
    {

        super(world, shootingEntity);
        WeaponDescribed weapon = null;
        if (shootingEntity.getCurrentEquippedItem() != null
                && shootingEntity.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            weapon = (WeaponDescribed) shootingEntity.getCurrentEquippedItem().getItem();
        Vec3 direction = shootingEntity.getLookVec().normalize();
        double speed = 10;
        double dispertion = 10;
        if (weapon != null)
        {
            dispertion = weapon.getActualBulletDispersion();
            System.out.println(dispertion);
            this.range = weapon.getFireRange();
            this.damage = weapon.getBulletDamage();
        }
        dispertion *= Math.PI / 180;
        final double disX = this.rand.nextDouble() * 2 * dispertion - dispertion;
        final double disY = this.rand.nextDouble() * 2 * dispertion - dispertion;
        final double disZ = this.rand.nextDouble() * 2 * dispertion - dispertion;
        direction.rotateAroundX((float) disX);
        direction.rotateAroundY((float) disY);
        direction.rotateAroundZ((float) disZ);
        direction = direction.normalize();
        this.motionX = direction.xCoord * speed;
        this.motionY = direction.yCoord * speed;
        this.motionZ = direction.zCoord * speed;
        final double r = 0.4375;
        final double xoffset = 0.1;
        final double yoffset = 0;
        final double zoffset = 0;
        final double horzScale = Math.sqrt(direction.xCoord * direction.xCoord + direction.zCoord * direction.zCoord);
        final double horzx = direction.xCoord / horzScale;
        final double horzz = direction.zCoord / horzScale;
        this.posX = shootingEntity.posX + direction.xCoord * xoffset - direction.yCoord * horzx * yoffset
                - horzz * zoffset;
        this.posY = shootingEntity.posY + shootingEntity.getEyeHeight() + direction.yCoord * xoffset
                + (1 - Math.abs(direction.yCoord)) * yoffset;
        this.posZ = shootingEntity.posZ + direction.zCoord * xoffset - direction.yCoord * horzz * yoffset
                + horzx * zoffset;
        this.boundingBox.setBounds(this.posX - r, this.posY - 0.0625, this.posZ - r, this.posX + r, this.posY + 0.0625,
                this.posZ + r);
        this.xBegin = this.posX;
        this.yBegin = this.posY;
        this.zBegin = this.posZ;
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public int getMaxLifetime()
    {
        return 100;
    }

    @Override
    public void onUpdate()
    {
        if ((this.ticksExisted > this.getMaxLifetime()
                || this.getDistance(this.xBegin, this.yBegin, this.zBegin) > this.range) && !this.worldObj.isRemote)
            this.setDead();
        super.onUpdate();
    }
    @Override
    protected float getGravityVelocity()
    {
        return 0F;
    }

    @Override
    protected void onImpact(final MovingObjectPosition mov)
    {
        if (mov.entityHit != null)
        {
            if (mov.entityHit != null && mov.entityHit instanceof EntityLivingBase)
            {
                mov.entityHit.attackEntityFrom(CraftechWeaponry.weaponDamageSource, damage);
                ((EntityLivingBase) mov.entityHit).hurtResistantTime = 0;
                ((EntityLivingBase) mov.entityHit).hurtTime = 1;
            }
        }
        else if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            CWParticles.spawnBulletImpact(mov.blockX, mov.blockY, mov.blockZ, mov.sideHit);
        this.setDead();
    }
}