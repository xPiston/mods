package fr.craftechmc.lootsediting.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.lootsediting.common.tool.ItemLootTool;
import net.minecraft.creativetab.CreativeTabs;

public class CommonProxy
{
    public ItemLootTool tool;

    public void preInit(final FMLPreInitializationEvent e)
    {
        FMLCommonHandler.instance().bus().register(CraftechLootsEditing.proxy);

        this.tool = new ItemLootTool();
        this.tool.setUnlocalizedName("ItemLootTool");
        this.tool.setCreativeTab(CreativeTabs.tabTools);
        this.tool.setTextureName(CraftechLootsEditing.MODASSETS + ":tool");

        GameRegistry.registerItem(this.tool, "ItemLootTool");
    }

    public void init(final FMLInitializationEvent e)
    {

    }

    public void postInit(final FMLPostInitializationEvent e)
    {

    }
}