package fr.craftechmc.lootsediting.client;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.lootsediting.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(new LootClientEventManager());
    }
}