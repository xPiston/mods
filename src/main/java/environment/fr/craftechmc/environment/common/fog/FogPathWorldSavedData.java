package fr.craftechmc.environment.common.fog;

import java.util.ArrayList;
import java.util.HashMap;

import fr.craftechmc.core.math.Vector3d;
import fr.craftechmc.environment.common.CraftechEnvironment;
import fr.craftechmc.environment.common.utils.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

/**
 * With this, we store all the fogpath and zones related data in a world data
 * file. Created by arisu on 24/07/2016.
 */
public class FogPathWorldSavedData extends WorldSavedData
{
    private static final String                                DATA_NAME   = CraftechEnvironment.MODID + "_FogPaths";
    private static final String                                NBTTAG_NAME = "fogPath";

    private static final HashMap<World, FogPathWorldSavedData> instanceMap = new HashMap<>();

    private ArrayList<FogZone>                                 zones       = new ArrayList<>();

    public FogPathWorldSavedData(final String dataName)
    {
        super(FogPathWorldSavedData.DATA_NAME);
    }

    public FogPathWorldSavedData()
    {
        super(FogPathWorldSavedData.DATA_NAME);
    }

    public static FogPathWorldSavedData get(final World world)
    {
        final MapStorage storage = world.mapStorage;

        FogPathWorldSavedData instance = FogPathWorldSavedData.instanceMap.get(world);
        if (instance == null)
        {
            instance = (FogPathWorldSavedData) storage.loadData(FogPathWorldSavedData.class,
                    FogPathWorldSavedData.DATA_NAME);

            if (instance == null)
            {
                instance = new FogPathWorldSavedData();
                storage.setData(FogPathWorldSavedData.DATA_NAME, instance);
            }
            FogPathWorldSavedData.instanceMap.put(world, instance);
        }
        return instance;
    }

    @Override
    public void readFromNBT(final NBTTagCompound compound)
    {
        this.zones.clear();
        final NBTTagList zoneList = compound.getTagList(FogPathWorldSavedData.NBTTAG_NAME + "Zones", 10);
        for (int i = 0; i < zoneList.tagCount(); i++)
        {
            final FogZone zone = new FogZone();
            final NBTTagCompound zoneTag = zoneList.getCompoundTagAt(i);
            final NBTTagList fogPathList = zoneTag.getTagList("fogPaths", 10);
            for (int j = 0; j < fogPathList.tagCount(); j++)
            {
                final FogPath fogPath = new FogPath();
                final NBTTagCompound fogPathTag = fogPathList.getCompoundTagAt(j);
                final NBTTagList checkpointList = fogPathTag.getTagList("checkpoints", 10);
                int k;
                for (k = 0; k < checkpointList.tagCount(); k++)
                {
                    fogPath.addCheckpoint(Util.readCoordinates(checkpointList.getCompoundTagAt(k), ""));
                }
                fogPath.setName(fogPathTag.getString("name"));
                zone.addFogPath(fogPath);
            }
            zone.setName(zoneTag.getString("name"));
            this.zones.add(zone);
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound compound)
    {
        final NBTTagList zoneList = new NBTTagList();
        for (final FogZone zone : this.zones)
        {
            final NBTTagCompound zoneTag = new NBTTagCompound();
            final NBTTagList fogPathList = new NBTTagList();
            for (final FogPath fogPath : zone.getFogPaths())
            {
                final NBTTagCompound fogPathTag = new NBTTagCompound();
                final NBTTagList checkpointList = new NBTTagList();
                for (final Vector3d coords : fogPath.getCheckpoints())
                {
                    final NBTTagCompound checkpointTag = new NBTTagCompound();
                    Util.writeCoordinates(checkpointTag, "", coords);
                    checkpointList.appendTag(checkpointTag);
                }
                fogPathTag.setTag("checkpoints", checkpointList);
                fogPathTag.setString("name", fogPath.getName());
                fogPathList.appendTag(fogPathTag);
            }
            zoneTag.setTag("fogPaths", fogPathList);
            zoneTag.setString("name", zone.getName());
            zoneList.appendTag(zoneTag);
        }
        compound.setTag(FogPathWorldSavedData.NBTTAG_NAME + "Zones", zoneList);
    }

    public static void saveAll()
    {
        for (final World w : DimensionManager.getWorlds())
            FogPathWorldSavedData.get(w).markDirty();
    }

    public ArrayList<FogZone> getZones()
    {
        return this.zones;
    }

    public void setZones(final ArrayList<FogZone> zones)
    {
        this.zones = zones;
    }
}