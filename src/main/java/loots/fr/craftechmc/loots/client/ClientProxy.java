package fr.craftechmc.loots.client;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import fr.craftechmc.admin.client.GuiAdmin;
import fr.craftechmc.loots.client.gui.LootAdminTab;
import fr.craftechmc.loots.common.CommonProxy;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.storage.LootStorage;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);

        GuiAdmin.tabs.add(() -> new LootAdminTab());
    }

    @Override
    public void serverStarting(final FMLServerStartingEvent e)
    {
        LootStorage.getInstance().getAllZones();
        ContainerLootTablesManager.getInstance().loadTables();
    }
}