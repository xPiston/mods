package fr.craftechmc.environment.server.fog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftechmc.core.math.Vector3d;
import fr.craftechmc.environment.common.CraftechEnvironment;
import fr.craftechmc.environment.common.CraftechEnvironmentPacketConstants;
import fr.craftechmc.environment.common.fog.FogEntity;
import fr.craftechmc.environment.common.fog.FogPath;
import fr.craftechmc.environment.common.fog.FogPathWorldSavedData;
import fr.craftechmc.environment.common.fog.FogZone;
import fr.craftechmc.environment.common.utils.Util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * This fogmanager is serverside only; it manages instances and positions of
 * every fogentity related to their zones. Created by arisu on 26/07/2016.
 */
public class ServerFogManager
{
    private static volatile ServerFogManager instance;
    private int                              lastSyncTick = 0;

    private final Random                     rand         = new Random();

    public static ServerFogManager getInstance()
    {
        if (ServerFogManager.instance == null)
            synchronized (ServerFogManager.class)
            {
                if (ServerFogManager.instance == null)
                    ServerFogManager.instance = new ServerFogManager();
            }
        return ServerFogManager.instance;
    }

    private int tick = 0;

    private ServerFogManager()
    {
    }

    public void tick()
    {
        for (final WorldServer world : MinecraftServer.getServer().worldServers)
            for (final FogZone zone : FogPathWorldSavedData.get(world).getZones())
                this.tick(zone, world);
        this.tick++;
    }

    private void tick(final FogZone zone, final WorldServer world)
    {
        final FogEntity fogEntity = zone.getFogEntity();
        if (fogEntity == null
                || this.tick % 40 == 0 && fogEntity.posX == 0 && fogEntity.posY == 0 && fogEntity.posZ == 0)
        {
            this.spawnFog(zone, world);
            this.updateFogPosition(zone);
            this.updateFogMoveData(zone);
            this.updateFogPosition(zone);
        }
        if (this.tick >= zone.fogTargetPositionTickArrival)
        {
            this.updateFogMoveData(zone);
            this.updateFogPosition(zone);
            if (FMLCommonHandler.instance().getEffectiveSide().isServer())
                this.syncZone(zone);
        }
        this.updateFogPosition(zone);
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() && this.tick != this.lastSyncTick
                && (this.tick - this.lastSyncTick) % 40 == 0)
            this.syncZone(zone);
    }

    private void spawnFog(final FogZone zone, final WorldServer world)
    {
        zone.killFogEntity();
        final FogEntity fogEntity = new FogEntity(world);
        world.spawnEntityInWorld(fogEntity);
        zone.setFogEntity(fogEntity);

        final Vector3d pos = this.getFogPosition(zone);
        if (pos == null)
            return;
        fogEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());

        zone.fogStartPositionTickBegin = this.tick;
        zone.fogTargetPosition = pos;
        zone.fogTargetPosition = this.getNextCheckpoint(zone);

        // Calculate distance between start and arrival
        final Vector3d distance = new Vector3d(zone.fogTargetPosition).substract(zone.fogStartPosition);
        zone.fogTargetPositionTickArrival = (int) (this.tick + 20 * distance.getLength());
    }

    private void updateFogMoveData(final FogZone zone)
    {
        final Vector3d targetPosition = zone.fogTargetPosition;
        zone.fogTargetPosition = this.getNextCheckpoint(zone);
        zone.fogStartPosition = targetPosition;

        if (zone.fogTargetPosition == null)
            return;
        if (zone.fogStartPosition == null)
        {
            zone.fogStartPosition = this.getRandomStartCheckpoint(zone);
            if (zone.fogStartPosition == null)
                return;
        }

        zone.fogStartPositionTickBegin = this.tick;

        // Calculate distance between start and arrival
        final Vector3d distance = new Vector3d(zone.fogTargetPosition).substract(zone.fogStartPosition);
        zone.fogTargetPositionTickArrival = (int) (this.tick + 20 * distance.getLength());
    }

    private void updateFogPosition(final FogZone zone)
    {
        if (zone.getFogEntity() == null)
            return;
        final Vector3d pos = this.getFogPosition(zone);
        if (pos == null)
            return;
        zone.getFogEntity().setPosition(pos.getX(), pos.getY(), pos.getZ());
        zone.updateFogVelocity();
    }

    public void syncZone(final FogZone zone)
    {
        final World world = zone.getFogEntity().worldObj;
        if (zone.fogTargetPosition == null)
            return;
        final FogEntity fogEntity = zone.getFogEntity();
        final Vector3d pos = this.getFogPosition(zone);
        final Vector3d vel = new Vector3d(zone.fogTargetPosition.getX(), zone.fogTargetPosition.getY(),
                zone.fogTargetPosition.getZ()).substract(zone.fogStartPosition).normalize();

        for (final Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            if (((EntityPlayerMP) player).worldObj.getWorldInfo().getDimension() == world.getWorldInfo().getDimension())
                CraftechEnvironment.network.send().packet(CraftechEnvironmentPacketConstants.ENTITY_SYNC_PACKET)
                        .with("id", fogEntity.getEntityId()).with("isDead", false).with("x", pos.getX())
                        .with("y", pos.getY()).with("z", pos.getZ()).with("xv", vel.getX()).with("yv", vel.getY())
                        .with("zv", vel.getZ()).to((EntityPlayerMP) player);
        this.lastSyncTick = this.tick;
    }

    // Logistic methods

    /**
     * @param zone
     * @return the position delta start/end
     */
    public Vector3d getFogPosition(final FogZone zone)
    {
        if (zone.getFogPaths().size() == 0 || this.getRandomStartCheckpoint(zone) == null)
            return null;

        if (zone.getFogPosition() == null)
        {
            zone.fogStartPosition = this.getRandomStartCheckpoint(zone);
            zone.setFogPosition(new Vector3d(0, 0, 0));
        }
        if (zone.fogTargetPosition != null)
        {
            zone.setFogPosition(new Vector3d(zone.fogStartPosition));
            final Vector3d scaledVector = new Vector3d(zone.fogTargetPosition).substract(zone.fogStartPosition)
                    .scale(zone.fogTargetPositionTickArrival - zone.fogStartPositionTickBegin > 0
                            ? (double) (this.tick - zone.fogStartPositionTickBegin)
                                    / (zone.fogTargetPositionTickArrival - zone.fogStartPositionTickBegin)
                            : 0);
            zone.getFogPosition().add(scaledVector);
        }
        return zone.getFogPosition().add(0.5, 0, 0.5);
    }

    /**
     * @param zone
     * @return a random start position, always an extremity checkpoints of a
     *         random fog path
     */
    public Vector3d getRandomStartCheckpoint(final FogZone zone)
    {
        final ArrayList<FogPath> fp = Lists.newArrayList(zone.getFogPaths());
        while (fp.size() > 0)
        {
            final FogPath p = fp.get(fp.size() == 1 ? 0 : this.rand.nextInt(fp.size()));
            final LinkedList<Vector3d> checkpoints = p.getCheckpoints();
            if (checkpoints.size() < 2)
            {
                fp.remove(p);
                continue;
            }
            return checkpoints.get(this.rand.nextBoolean() ? 0 : checkpoints.size() - 1);
        }
        return null;
    }

    /**
     * @param zone
     * @return the logic (and sometimes random) next checkpoint for the fog
     *         entity to navigate to
     */
    public Vector3d getNextCheckpoint(final FogZone zone)
    {
        if (zone.getFogPaths().isEmpty())
            return null;
        final HashMap<FogPath, Integer> eligibleFogPaths = new HashMap<>();
        for (final FogPath fogPath : zone.getFogPaths())
            for (final Vector3d checkpoint : fogPath.getCheckpoints())
                if (checkpoint.equals(zone.fogTargetPosition))
                {
                    eligibleFogPaths.put(fogPath, Util.collectionIndexOfVector(fogPath.getCheckpoints(), checkpoint));
                    break;
                }
        while (eligibleFogPaths.size() > 0)
        {
            final FogPath fogPath = (FogPath) eligibleFogPaths.keySet().toArray()[eligibleFogPaths.size() == 1 ? 0
                    : this.rand.nextInt(eligibleFogPaths.size())];
            final int checkpointIndex = eligibleFogPaths.get(fogPath);
            final LinkedList<Vector3d> checkpoints = fogPath.getCheckpoints();

            // If extremity of fogpath
            if (checkpointIndex == 0 || checkpointIndex == checkpoints.size() - 1)
            {
                // Means that it's the first point
                if (zone.fogStartPosition == null || zone.fogStartPosition.equals(zone.fogTargetPosition)
                        || !Util.collectionHasVector(checkpoints, zone.fogStartPosition))
                {
                    if (checkpointIndex + 1 < checkpoints.size())
                        return checkpoints.get(checkpointIndex + 1);
                    if (checkpointIndex - 1 > 0)
                        return checkpoints.get(checkpointIndex - 1);
                }
                else if (Util.collectionHasVector(checkpoints, zone.fogStartPosition))
                {
                    eligibleFogPaths.remove(fogPath);
                    if (eligibleFogPaths.size() == 0)
                    {
                        if (checkpoints.get(0).equals(checkpoints.get(checkpoints.size() - 1)))
                            return checkpointIndex == 0 ? checkpoints.get(checkpoints.size() - 1) : checkpoints.get(0);
                        return zone.fogStartPosition;
                    }
                    continue;
                }
            }
            // If middle of fogpath
            else if (checkpointIndex > 0 && checkpointIndex < checkpoints.size() - 1)
                if (Util.collectionIndexOfVector(checkpoints, zone.fogStartPosition) < checkpointIndex)
                    return checkpoints.get(checkpointIndex + 1);
                else if (Util.collectionIndexOfVector(checkpoints, zone.fogStartPosition) > checkpointIndex)
                    return checkpoints.get(checkpointIndex - 1);
            eligibleFogPaths.remove(fogPath);
        }

        return this.getRandomStartCheckpoint(zone);
    }
}