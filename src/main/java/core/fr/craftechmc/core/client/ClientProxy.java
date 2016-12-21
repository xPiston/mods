package fr.craftechmc.core.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.core.client.render.CraftechModelLoader;
import fr.craftechmc.core.client.render.ISBRHInventoryRenderer;
import fr.craftechmc.core.common.CommonProxy;
import net.minecraftforge.client.model.AdvancedModelLoader;

public class ClientProxy extends CommonProxy
{
    public static int coreISBRHInventoryID;

    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);

        // ISBRHandler
        ClientProxy.coreISBRHInventoryID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(ClientProxy.coreISBRHInventoryID, new ISBRHInventoryRenderer());

        AdvancedModelLoader.registerModelHandler(new CraftechModelLoader());
    }
}