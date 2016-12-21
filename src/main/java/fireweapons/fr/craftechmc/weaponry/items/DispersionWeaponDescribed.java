package fr.craftechmc.weaponry.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DispersionWeaponDescribed extends WeaponDescribed
{
    /**
     * The number of bullet
     */
    public int     bulletNumber;

    /**
     * If the weapon has two canon
     */
    public boolean hasTwoCanon;

    public DispersionWeaponDescribed()
    {
        super();
        this.bulletNumber = 10;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World w, final EntityPlayer player)
    {
        if (!this.hasTwoCanon)
            return super.onItemRightClick(stack, w, player);
        return stack;
    }

    public int getBulletNumber()
    {
        return this.bulletNumber;
    }

    public void setBulletNumber(final int bulletNumber)
    {
        this.bulletNumber = bulletNumber;
    }

    public boolean isHasTwoCanon()
    {
        return this.hasTwoCanon;
    }

    public void setHasTwoCanon(final boolean hasTwoCanon)
    {
        this.hasTwoCanon = hasTwoCanon;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.bulletNumber;
        result = prime * result + (this.hasTwoCanon ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final DispersionWeaponDescribed other = (DispersionWeaponDescribed) obj;
        if (this.bulletNumber != other.bulletNumber)
            return false;
        if (this.hasTwoCanon != other.hasTwoCanon)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "DispersionWeaponDescribed [bulletNumber=" + this.bulletNumber + ", hasTwoCanon=" + this.hasTwoCanon
                + "]";
    }
}