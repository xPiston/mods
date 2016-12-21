package fr.craftechmc.admin.common;

import com.unascribed.lambdanetwork.LambdaNetwork;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import fr.craftechmc.admin.common.fogpath.ItemEditorTool;

@Mod(modid = CraftechAdmin.MODID, version = CraftechAdmin.VERSION, name = CraftechAdmin.MODNAME, dependencies = "required-after:craftechcore")
public class CraftechAdmin
{
    public static final String MODID     = "craftechadmin";
    public static final String MODNAME   = "Craftech | Admin";
    public static final String VERSION   = "0.0.1";
    public static final String MODASSETS = CraftechAdmin.MODID;

    @SidedProxy(clientSide = "fr.craftechmc.admin.client.ClientProxy", serverSide = "fr.craftechmc.admin.server.ServerProxy")
    public static CommonProxy proxy;

    @Instance(CraftechAdmin.MODID)
    public static CraftechAdmin instance;

    public static LambdaNetwork network;

    public static final ItemEditorTool FOGPATH_EDITOR_TOOL = new ItemEditorTool();

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechAdmin.proxy.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        CraftechAdmin.proxy.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        CraftechAdmin.proxy.postInit(event);
    }

    @EventHandler
    public void serverStarted(final FMLServerStartedEvent e)
    {
        CraftechAdmin.proxy.serverStarted(e);
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent e)
    {
        CraftechAdmin.proxy.serverStarting(e);
    }
}