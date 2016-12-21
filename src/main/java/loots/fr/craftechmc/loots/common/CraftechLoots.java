package fr.craftechmc.loots.common;

import com.unascribed.lambdanetwork.LambdaNetwork;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;

@Mod(modid = CraftechLoots.MODID, version = CraftechLoots.VERSION, name = CraftechLoots.MODNAME, dependencies = "required-after:craftechcore")
public class CraftechLoots
{
    public static final String               MODID     = "craftechloots";
    public static final String               MODNAME   = "Craftech | Loots";
    public static final String               VERSION   = "0.0.1";
    public static final String               MODASSETS = CraftechLoots.MODID;

    @SidedProxy(clientSide = "fr.craftechmc.loots.client.ClientProxy", serverSide = "fr.craftechmc.loots.server.ServerProxy")
    public static CommonProxy                proxy;

    @Instance(CraftechLoots.MODID)
    public static CraftechLoots              instance;

    public static LambdaNetwork              network;

    public static ContainerLootTablesManager containerLootTablesManager;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechLoots.proxy.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        CraftechLoots.proxy.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        CraftechLoots.proxy.postInit(event);
    }

    @EventHandler
    public void serverStarted(final FMLServerStartedEvent e)
    {
        CraftechLoots.proxy.serverStarted(e);
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent e)
    {
        CraftechLoots.proxy.serverStarting(e);
    }
}