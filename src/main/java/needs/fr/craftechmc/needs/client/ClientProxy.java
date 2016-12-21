package fr.craftechmc.needs.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.needs.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by arisu on 22/06/2016.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void init(FMLPreInitializationEvent e)
    {
        super.init(e);
        NeedsHud hud = new NeedsHud();
        MinecraftForge.EVENT_BUS.register(hud);
        FMLCommonHandler.instance().bus().register(hud);
    }
}
