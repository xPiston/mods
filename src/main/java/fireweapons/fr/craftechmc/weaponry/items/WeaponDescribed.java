package fr.craftechmc.weaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WeaponDescribed extends Item
{
    /**
     * Defined in ticks between each bullet
     */
    public int             fireRate;

    /**
     * Maximal distance of the bullet
     */
    public double          fireRange;

    /**
     * Bullet speed in b/ticks
     */
    public double          bulletSpeed;

    /**
     * Base bullet dispersion in �
     */
    public double          bulletDispersion;

    /**
     * Actual bullet dispersion in �
     */
    public double          actualBulletDispersion;

    /**
     * Bullet damage on hit
     */
    public float           bulletDamage;

    /**
     * Time to reload the weapon
     */
    public double          reloadTime;
    /**
     * Loader of the weapon
     */
    public LoaderDescribed loader;
    public String          name;

    public WeaponDescribed()
    {
        super();
        this.maxStackSize = 1;
        this.actualBulletDispersion = this.bulletDispersion;
    }

    @Override
    public int getMaxItemUseDuration(final ItemStack p_77626_1_)
    {
        return 72000;
    }

    @Override
    public void onPlayerStoppedUsing(final ItemStack stack, final World w, final EntityPlayer player, final int i)
    {
        if (stack.hasTagCompound())
        {
            if (stack.getTagCompound().getBoolean("isAiming"))
                stack.getTagCompound().setBoolean("isAiming", false);
        }
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World w, final EntityPlayer player)
    {
        final StackTraceElement[] test = Thread.currentThread().getStackTrace();
        for (final StackTraceElement element : test)
        {
            System.out.print(
                    element.getClassName() + " " + element.getMethodName() + " " + element.getLineNumber() + "\n");
        }
        if (stack.hasTagCompound())
        {
            stack.getTagCompound().setBoolean("isAiming", !stack.getTagCompound().getBoolean("isAiming"));
            stack.getTagCompound().setLong("aimingStart", System.currentTimeMillis());
        }
        return stack;
    }

    @Override
    public void onUpdate(final ItemStack stack, final World w, final Entity e, final int i, final boolean b)
    {
        super.onUpdate(stack, w, e, i, b);

        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        if (stack.getTagCompound().getBoolean("isAiming"))
        {
            if (stack.getTagCompound().getFloat("currentAiming") < 1)
                stack.getTagCompound().setFloat("currentAiming",
                        stack.getTagCompound().getFloat("currentAiming") + .05f);
            else
            {
                if (stack.getTagCompound().getFloat("currentAiming") != 1)
                    stack.getTagCompound().setFloat("currentAiming", 1);
            }
        }
        else
        {
            if (stack.getTagCompound().getFloat("currentAiming") > 0)
                stack.getTagCompound().setFloat("currentAiming",
                        stack.getTagCompound().getFloat("currentAiming") - .05f);
            else
            {
                if (stack.getTagCompound().getFloat("currentAiming") != 0)
                    stack.getTagCompound().setFloat("currentAiming", 0);
            }
        }
    }

    @Override
    public boolean onEntitySwing(final EntityLivingBase living, final ItemStack stack)
    {
        if (!living.worldObj.isRemote)
        {
        }
        return true;
    }

    @Override
    public boolean getShareTag()
    {
        return true;
    }

    public void increaseDispersion()
    {
        this.actualBulletDispersion += 5;
    }

    public int getFireRate()
    {
        return this.fireRate;
    }

    public void setFireRate(final int fireRate)
    {
        this.fireRate = fireRate;
    }

    public double getFireRange()
    {
        return this.fireRange;
    }

    public void setFireRange(final double fireRange)
    {
        this.fireRange = fireRange;
    }

    public double getBulletSpeed()
    {
        return this.bulletSpeed;
    }

    public void setBulletSpeed(final double bulletSpeed)
    {
        this.bulletSpeed = bulletSpeed;
    }

    public double getBulletDispersion()
    {
        return this.bulletDispersion;
    }

    public void setBulletDispersion(final double bulletDispersion)
    {
        this.bulletDispersion = bulletDispersion;
    }

    public double getActualBulletDispersion()
    {
        return this.actualBulletDispersion;
    }

    public void setActualBulletDispersion(final double actualBulletDispersion)
    {
        this.actualBulletDispersion = actualBulletDispersion;
    }

    public float getBulletDamage()
    {
        return this.bulletDamage;
    }

    public void setBulletDamage(final float bulletDamage)
    {
        this.bulletDamage = bulletDamage;
    }

    public double getReloadTime()
    {
        return this.reloadTime;
    }

    public void setReloadTime(final double reloadTime)
    {
        this.reloadTime = reloadTime;
    }

    public LoaderDescribed getLoader()
    {
        return this.loader;
    }

    public void setLoader(final LoaderDescribed loader)
    {
        this.loader = loader;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.actualBulletDispersion);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + Float.floatToIntBits(this.bulletDamage);
        temp = Double.doubleToLongBits(this.bulletDispersion);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.bulletSpeed);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.fireRange);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + this.fireRate;
        result = prime * result + (this.loader == null ? 0 : this.loader.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        temp = Double.doubleToLongBits(this.reloadTime);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final WeaponDescribed other = (WeaponDescribed) obj;
        if (Double.doubleToLongBits(this.actualBulletDispersion) != Double
                .doubleToLongBits(other.actualBulletDispersion))
            return false;
        if (Float.floatToIntBits(this.bulletDamage) != Float.floatToIntBits(other.bulletDamage))
            return false;
        if (Double.doubleToLongBits(this.bulletDispersion) != Double.doubleToLongBits(other.bulletDispersion))
            return false;
        if (Double.doubleToLongBits(this.bulletSpeed) != Double.doubleToLongBits(other.bulletSpeed))
            return false;
        if (Double.doubleToLongBits(this.fireRange) != Double.doubleToLongBits(other.fireRange))
            return false;
        if (this.fireRate != other.fireRate)
            return false;
        if (this.loader == null)
        {
            if (other.loader != null)
                return false;
        }
        else if (!this.loader.equals(other.loader))
            return false;
        if (this.name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(this.reloadTime) != Double.doubleToLongBits(other.reloadTime))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "WeaponDescribed [fireRate=" + this.fireRate + ", fireRange=" + this.fireRange + ", bulletSpeed="
                + this.bulletSpeed + ", bulletDispersion=" + this.bulletDispersion + ", actualBulletDispersion="
                + this.actualBulletDispersion + ", bulletDamage=" + this.bulletDamage + ", reloadTime="
                + this.reloadTime + ", loader=" + this.loader + ", name=" + this.name + "]";
    }
}