package fr.craftechmc.contentmod.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.init.Items;

public class CommonProxy
{
    public void preInit(final FMLPreInitializationEvent e)
    {
        CTabManager.addTab("CONTENT_MULTIBLOCK", "multiblocks", Items.carrot);
        CTabManager.addTab("CONTENT_MODEL", "models", Items.potato);
        CTabManager.addTab("CONTENT_BLOCK", "blocks", Items.coal);
        CTabManager.addTab("CONTENT_DECLINAISON", "declinaisons", Items.slime_ball);
    }

    public void init(final FMLInitializationEvent e)
    {
    }

    public void postInit(final FMLPostInitializationEvent e)
    {

    }

    public void serverStarting(final FMLServerStartingEvent e)
    {
    }

    public void serverStarted(final FMLServerStartedEvent e)
    {
    }
}