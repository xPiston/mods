package fr.craftechmc.weaponry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

;

public class ImpactWorldData extends WorldSavedData
{
    final static String key = CraftechWeaponry.MODID;

    public ImpactWorldData()
    {
        this(ImpactWorldData.key);
    }

    public ImpactWorldData(String key)
    {
        super(key);
    }

    public static ImpactWorldData forWorld(World world)
    {
        final MapStorage storage = world.perWorldStorage;
        ImpactWorldData result = (ImpactWorldData) storage.loadData(ImpactWorldData.class, ImpactWorldData.key);
        if (result == null)
        {
            result = new ImpactWorldData();
            storage.setData(ImpactWorldData.key, result);
        }
        return result;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        // Get your data from the nbt here
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        // Put your data in the nbt here
    }
}