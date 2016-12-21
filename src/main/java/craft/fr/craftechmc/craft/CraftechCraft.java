package fr.craftechmc.craft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.craft.block.BlockWorkbench;

/**
 * Desc...
 * Created by Thog the 16/04/2016
 */
@Mod(modid = CraftechCraft.MOD_ID, name = CraftechCraft.NAME, version = CraftechCraft.VERSION, dependencies = "required-after:craftechcontentmod") public class CraftechCraft
{
    public static final    BlockWorkbench WORKBENCH = new BlockWorkbench();
    protected static final String         MOD_ID    = "craftechcraft";
    protected static final String         NAME      = "Craftech Craft";
    protected static final String         VERSION   = "0.1-alpha";
    @SidedProxy(clientSide = "fr.craftechmc.craft.client.ClientProxy", serverSide = "fr.craftechmc.craft.CommonProxy") private static CommonProxy proxy;

    @Mod.Instance(value = MOD_ID) private static CraftechCraft instance;

    public static CraftechCraft getInstance()
    {
        return instance;
    }

    public static CommonProxy getProxy()
    {
        return proxy;
    }

    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }

    @Mod.EventHandler public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
}
