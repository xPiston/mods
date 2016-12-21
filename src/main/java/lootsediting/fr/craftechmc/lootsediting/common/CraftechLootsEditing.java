package fr.craftechmc.lootsediting.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CraftechLootsEditing.MODID, version = CraftechLootsEditing.VERSION, name = CraftechLootsEditing.MODNAME)
public class CraftechLootsEditing
{
    public static final String MODID     = "craftechlootsediting";
    public static final String MODNAME   = "Craftech | Loots Editing Tools";
    public static final String VERSION   = "0.0.1";
    public static final String MODASSETS = CraftechLootsEditing.MODID;

    @SidedProxy(clientSide = "fr.craftechmc.lootsediting.client.ClientProxy", serverSide = "fr.craftechmc.lootsediting.server.ServerProxy")
    public static CommonProxy  proxy;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechLootsEditing.proxy.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        CraftechLootsEditing.proxy.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        CraftechLootsEditing.proxy.postInit(event);
    }
}