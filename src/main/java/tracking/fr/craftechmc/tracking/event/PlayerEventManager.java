package fr.craftechmc.tracking.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import fr.craftechmc.tracking.TrackingManager;
import fr.craftechmc.tracking.TrackingManager.PlayerData;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.commons.lang3.tuple.Pair;

public class PlayerEventManager
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            if (TrackingManager.getInstance().players.containsKey(event.player.getCommandSenderName()))
                TrackingManager.getInstance().players.get(event.player.getCommandSenderName())
                        .setLastJoinDate(System.currentTimeMillis());
            else
            {
                final PlayerData data = new PlayerData();
                data.setLastJoinDate(System.currentTimeMillis());
                TrackingManager.getInstance().players.put(event.player.getCommandSenderName(), data);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            if (TrackingManager.getInstance().players.containsKey(event.player.getCommandSenderName()))
            {
                final PlayerData data = TrackingManager.getInstance().players.get(event.player.getCommandSenderName());
                data.getSessions()
                        .add((Pair.of(System.currentTimeMillis() - data.getLastJoinDate(), data.getCurrentSession())));
                data.setLastJoinDate(0);
                data.setCurrentSession(0);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerBlock(BlockEvent.PlaceEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            final PlayerData data = TrackingManager.getInstance().players.get(event.player.getCommandSenderName());
            data.setCurrentSession(data.getCurrentSession() + 1);
        }
    }
}