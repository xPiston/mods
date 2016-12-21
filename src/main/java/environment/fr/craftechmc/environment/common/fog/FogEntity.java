package fr.craftechmc.environment.common.fog;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.zombie.common.entity.ISpawnable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * The fogentity is the object that represents a fog instance, and it is located at the center of the fog.
 * Created by arisu on 24/07/2016.
 */
public class FogEntity extends Entity implements ISpawnable
{

    public FogEntity(World world)
    {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {

    }

    @Override
    public void onEntityUpdate()
    {
        this.setPosition(posX + motionX / 20, posY + motionY / 20, posZ + motionZ / 20);
    }

    @SideOnly(Side.SERVER)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
    {
        this.motionX = p_70016_1_;
        this.motionY = p_70016_3_;
        this.motionZ = p_70016_5_;
    }
}
