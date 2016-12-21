package fr.craftechmc.tracking.client;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftechmc.admin.client.GuiAdmin;
import fr.craftechmc.tracking.CommonProxy;
import fr.craftechmc.tracking.client.gui.AdminTrackingTab;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerSidedOps()
    {
        FMLCommonHandler.instance().bus().register(new TrackingKeyHandler());

        GuiAdmin.tabs.add(() -> new AdminTrackingTab());
    }
}