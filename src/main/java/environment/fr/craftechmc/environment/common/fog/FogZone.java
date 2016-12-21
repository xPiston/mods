package fr.craftechmc.environment.common.fog;

import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.admin.common.fogpath.ANameGetter;
import fr.craftechmc.core.math.Vector3d;
import fr.craftechmc.environment.common.CraftechEnvironment;
import fr.craftechmc.environment.common.CraftechEnvironmentPacketConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * A fog path zone contains only one fogentity and many fogpaths. Created by
 * arisu on 24/07/2016.
 */
public class FogZone extends ANameGetter
{
    private final ArrayList<FogPath> fogPaths                     = new ArrayList<>();

    private FogEntity                fogEntity                    = null;

    private Vector3d                 fogPosition                  = null;
    public Vector3d                  fogStartPosition             = null;
    public Vector3d                  fogTargetPosition            = null;
    public int                       fogStartPositionTickBegin    = 0;
    public int                       fogTargetPositionTickArrival = 0;

    public FogZone()
    {

    }

    public void addFogPath(final FogPath fogPath)
    {
        this.fogPaths.add(fogPath);
    }

    @SideOnly(Side.SERVER)
    public void updateFogVelocity()
    {
        if (this.fogTargetPosition == null)
        {
            this.fogEntity.setVelocity(0, 0, 0);
            return;
        }

        final Vector3d vel = new Vector3d(this.fogTargetPosition).substract(this.fogStartPosition).normalize();
        this.fogEntity.setVelocity(vel.getX(), vel.getY(), vel.getZ());
    }

    @SideOnly(Side.SERVER)
    public void killFogEntity()
    {
        if (this.fogEntity != null)
        {
            this.fogEntity.setDead();
            final World world = this.fogEntity.worldObj;

            if (FMLCommonHandler.instance().getEffectiveSide().isServer())
                for (final Object p : world.playerEntities)
                    CraftechEnvironment.network.send().packet(CraftechEnvironmentPacketConstants.ENTITY_SYNC_PACKET)
                            .with("id", this.fogEntity.getEntityId()).with("isDead", true).with("x", 0d).with("y", 0d)
                            .with("z", 0d).with("xv", 0d).with("yv", 0d).with("zv", 0d).to((EntityPlayer) p);
        }
        this.fogEntity = null;
        this.resetPosVars();
    }

    @SideOnly(Side.SERVER)
    private void resetPosVars()
    {
        this.fogPosition = null;
        this.fogStartPosition = null;
        this.fogTargetPosition = null;
        this.fogStartPositionTickBegin = 0;
        this.fogTargetPositionTickArrival = 0;
    }

    public ArrayList<FogPath> getFogPaths()
    {
        return this.fogPaths;
    }

    public FogEntity getFogEntity()
    {
        return this.fogEntity;
    }

    public void setFogEntity(final FogEntity fogEntity)
    {
        this.fogEntity = fogEntity;
    }

    public Vector3d getFogPosition()
    {
        return this.fogPosition;
    }

    public void setFogPosition(final Vector3d fogPosition)
    {
        this.fogPosition = fogPosition;
    }
}