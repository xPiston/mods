package fr.craftechmc.contentmod.common;

import java.util.logging.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.contentmod.common.blocks.BlocksManager;

@Mod(modid = CraftechContentMod.MODID, version = CraftechContentMod.VERSION, name = CraftechContentMod.MODNAME, dependencies = "required-after:baldr;required-after:craftechcore")
public class CraftechContentMod
{
    public static final String  MODID     = "craftechcontentmod";
    public static final String  MODNAME   = "craftechcontentmod";
    public static final String  VERSION   = "1.0";
    public static final String  MODASSETS = "craftechcontentmod";

    public static final Logger  LOGGER    = Logger.getLogger(CraftechContentMod.MODID);

    public static BlocksManager blocksManager;

    @SidedProxy(clientSide = "fr.craftechmc.contentmod.client.ClientProxy", serverSide = "fr.craftechmc.contentmod.server.ServerProxy")
    public static CommonProxy   proxy;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechContentMod.proxy.preInit(event);
    }
}