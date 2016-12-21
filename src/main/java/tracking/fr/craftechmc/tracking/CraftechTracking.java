package fr.craftechmc.tracking;

import java.util.LinkedHashMap;

import com.unascribed.lambdanetwork.DataType;
import com.unascribed.lambdanetwork.LambdaNetwork;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.tracking.TrackingManager.PlayerData;
import fr.craftechmc.tracking.event.PlayerEventManager;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = CraftechTracking.MODID, version = CraftechTracking.VERSION, name = CraftechTracking.MODNAME)
public class CraftechTracking
{
    public static final String     MODID     = "craftechtracking";
    public static final String     MODNAME   = "craftechtracking";
    public static final String     VERSION   = "1.0";
    public static final String     MODASSETS = "craftechtracking";

    @SidedProxy(clientSide = "fr.craftechmc.tracking.client.ClientProxy", serverSide = "fr.craftechmc.tracking.CommonProxy")
    public static CommonProxy      proxy;

    public static LambdaNetwork    network;

    @Instance(CraftechTracking.MODID)
    public static CraftechTracking instance;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechTracking.proxy.registerSidedOps();

        FMLCommonHandler.instance().bus().register(new PlayerEventManager());
        MinecraftForge.EVENT_BUS.register(new PlayerEventManager());
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        CraftechTracking.network = LambdaNetwork.builder().channel(CraftechTracking.MODID)
                .packet(TrackingPacketConstants.TRACKING_REQUEST).boundTo(Side.SERVER)
                .handledOnMainThreadBy((player, token) ->
                {
                    CraftechTracking.network.send().packet(TrackingPacketConstants.TRACKING_DATA)
                            .with("data", TrackingManager.getInstance().toString(TrackingManager.getInstance().players))
                            .to(player);
                }).packet(TrackingPacketConstants.TRACKING_DATA).boundTo(Side.CLIENT).with(DataType.STRING, "data")
                .handledOnMainThreadBy((player, token) ->
                {
                    TrackingManager.getInstance().players = (LinkedHashMap<String, PlayerData>) TrackingManager
                            .getInstance().fromString(token.getString("data"));
                }).build();
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent event)
    {
        try
        {
            TrackingManager.getInstance().readTrackingData();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void serverStopped(final FMLServerStoppedEvent event)
    {
        try
        {
            TrackingManager.getInstance().writeTrackingData();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
}