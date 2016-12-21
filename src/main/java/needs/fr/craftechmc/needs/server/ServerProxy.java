package fr.craftechmc.needs.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftechmc.needs.common.CommonProxy;
import fr.craftechmc.needs.common.properties.NeedsProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Created by arisu on 22/06/2016.
 */
public class ServerProxy extends CommonProxy
{

    @Override
    public void init(FMLPreInitializationEvent e)
    {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    /**
     * Used to sync nbts from server to client
     *
     * @param event
     */
    @SubscribeEvent
    public void onEntityJoinedWorld(final EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayerMP)
            NeedsProperties.get((EntityPlayer) event.entity).sync();
    }

    /**
     * Used to load the properties
     *
     * @param event
     */
    @SubscribeEvent
    public void onEntityConstructing(final EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayerMP)
            NeedsProperties.get((EntityPlayer) event.entity);
    }
}
