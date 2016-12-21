package fr.craftechmc.social.common;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Aursen on 01/10/2016.
 */

@Mod(modid = CraftechSocial.MODID, version = CraftechSocial.VERSION, name = CraftechSocial.MODNAME)
public class CraftechSocial {
	public static final String MODID     = "craftechsocial";
    public static final String MODNAME   = "craftechsocial";
    public static final String VERSION   = "1.0";
    public static final String MODASSETS = "craftechsocial";

    @Mod.Instance(CraftechSocial.MODID)
    public static CraftechSocial instance;

    public static final Logger LOGGER = Logger.getLogger(CraftechSocial.MODID);

    @SidedProxy(clientSide = "fr.craftechmc.social.client.ClientProxy", serverSide = "fr.craftechmc.social.server.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(proxy);
        proxy.init();
    }
}
