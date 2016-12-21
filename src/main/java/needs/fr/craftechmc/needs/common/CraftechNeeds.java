package fr.craftechmc.needs.common;

import java.util.logging.Logger;

import com.google.gson.Gson;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.needs.common.messages.NeedsMessage;
import fr.craftechmc.needs.common.messages.NeedsMessageHandler;

/**
 * Created by arisu on 22/06/2016.
 */
@Mod(modid = CraftechNeeds.MODID, version = CraftechNeeds.VERSION, name = CraftechNeeds.MODNAME, dependencies = "required-after:craftechcore")
public class CraftechNeeds
{
    public static final String         MODID     = "craftechneeds";
    public static final String         MODNAME   = "craftechneeds";
    public static final String         VERSION   = "1.0";
    public static final String         MODASSETS = "craftechneeds";

    public static final Logger         LOGGER    = Logger.getLogger(CraftechNeeds.MODID);

    public static Gson                 GSON      = new Gson();
    public static SimpleNetworkWrapper network;

    @SidedProxy(clientSide = "fr.craftechmc.needs.client.ClientProxy", serverSide = "fr.craftechmc.needs.server.ServerProxy")
    public static CommonProxy          proxy;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        // Initialize the network and register Messages
        CraftechNeeds.network = NetworkRegistry.INSTANCE.newSimpleChannel(CraftechNeeds.MODID);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            CraftechNeeds.network.registerMessage(NeedsMessageHandler.class, NeedsMessage.class, 0, Side.CLIENT);

        // Register the proxy then init() it
        FMLCommonHandler.instance().bus().register(CraftechNeeds.proxy);
        CraftechNeeds.proxy.init(event);
    }

}
