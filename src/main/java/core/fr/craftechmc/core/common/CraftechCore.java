package fr.craftechmc.core.common;

import java.util.logging.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CraftechCore.MODID, version = CraftechCore.VERSION, name = CraftechCore.NAME, dependencies = "required-after:brokkguiwrapper")
public class CraftechCore
{
    public static final String MODID   = "craftechcore";
    public static final String NAME    = "Craftech Core";
    public static final String VERSION = "1.0";

    public static final Logger logger  = Logger.getLogger(CraftechCore.NAME);

    @SidedProxy(clientSide = "fr.craftechmc.core.client.ClientProxy", serverSide = "fr.craftechmc.core.server.ServerProxy")
    public static CommonProxy  proxy;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechCore.proxy.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        CraftechCore.proxy.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        CraftechCore.proxy.postInit(event);
    }
}