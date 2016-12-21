package fr.craftechmc.baldr;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.logging.Logger;

@Mod(modid = BaldrVars.MODID, name = BaldrVars.MODNAME, version = BaldrVars.MODVERSION)
public class Baldr
{
    public static final Logger logger = Logger.getLogger(BaldrVars.MODNAME);

    @SidedProxy(clientSide = "fr.craftechmc.baldr.client.ClientProxy", serverSide = "fr.craftechmc.baldr.CommonProxy")
    public static CommonProxy  proxy;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        Baldr.proxy.makeSidedOps();
    }
}