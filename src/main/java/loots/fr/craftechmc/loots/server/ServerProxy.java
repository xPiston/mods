package fr.craftechmc.loots.server;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.loots.common.CommonProxy;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.storage.LootStorage;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);
        LootStorage.getInstance().getAllZones();
        ContainerLootTablesManager.getInstance().loadTables();
    }
}