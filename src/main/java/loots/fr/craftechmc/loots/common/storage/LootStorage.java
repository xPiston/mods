package fr.craftechmc.loots.common.storage;

import com.google.gson.Gson;
import fr.craftechmc.core.common.data.DataStorage;
import fr.craftechmc.loots.common.CraftechLoots;
import fr.craftechmc.loots.common.objects.ContainerLootTable;
import fr.craftechmc.loots.common.objects.LootZone;

import java.util.List;
import java.util.stream.Collectors;

public class LootStorage
{
    private static volatile LootStorage instance = null;
    private final Gson                  gson;
    private final DataStorage           dataStorage;

    private LootStorage()
    {
        this.gson = new Gson();
        this.dataStorage = DataStorage.getInstance();
    }

    public static final LootStorage getInstance()
    {
        if (LootStorage.instance == null)
            synchronized (LootStorage.class)
            {
                if (LootStorage.instance == null)
                    LootStorage.instance = new LootStorage();
            }
        return LootStorage.instance;
    }

    public LootZone getZone(final String name)
    {
        return this.gson.fromJson(this.dataStorage.getCachedFile(CraftechLoots.MODID, "zone/" + name + ".json"),
                LootZone.class);
    }

    public boolean addZone(final LootZone zone)
    {
        return this.dataStorage.setCachedFile(CraftechLoots.MODID, "zone/" + zone.getZoneName() + ".json",
                this.gson.toJson(zone));
    }

    public boolean zoneExist(final String name)
    {
        return this.dataStorage.cachedFileExist(CraftechLoots.MODID, "zone/" + name + ".json");
    }

    public boolean zoneExist(final LootZone zone)
    {
        return this.zoneExist(zone.getZoneName());
    }

    public int getZonesCount()
    {
        return this.dataStorage.listCachedFiles(CraftechLoots.MODID, "zone/");
    }

    public List<LootZone> getAllZones()
    {
        return this.dataStorage.getAllCachedFiles(CraftechLoots.MODID, "zone/").stream()
                .map(f -> this.gson.fromJson(f, LootZone.class)).collect(Collectors.toList());
    }

    public List<ContainerLootTable> getAllContainerLootTables()
    {
        return this.dataStorage.getAllCachedFiles(CraftechLoots.MODID, "loottables/containers/").stream()
                .map(f -> this.gson.fromJson(f, ContainerLootTable.class)).collect(Collectors.toList());
    }
}