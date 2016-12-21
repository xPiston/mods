package fr.craftechmc.environment.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.craftechmc.environment.common.CommonProxy;
import fr.craftechmc.environment.common.fog.FogPathWorldSavedData;
import fr.craftechmc.environment.common.fog.FogZone;
import fr.craftechmc.environment.server.fog.ServerFogManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Created by arisu on 23/07/2016.
 */
public class ServerProxy extends CommonProxy
{
    private final ServerFogManager fogManager = ServerFogManager.getInstance();

    @Override
    public void init()
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
            fogManager.tick();
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event)
    {
        // Synchronize on player transactions
        if (event.entity instanceof EntityPlayer)
        {
            for (FogZone zone : FogPathWorldSavedData.get(event.world).getZones())
                ServerFogManager.getInstance().syncZone(zone);
        }
    }
}
