package fr.craftechmc.handweapons;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;

@Mod(modid = CraftechHandWeapons.MODID, name = CraftechHandWeapons.MODNAME, version = CraftechHandWeapons.MODVER, dependencies = "required-after:craftechcontentmod")
public class CraftechHandWeapons
{
    public static final String MODID   = "craftechhandweapons";
    public static final String MODNAME = "Craftech | Hand Weapons";
    public static final String MODVER  = "alpha-0.2";

    @Instance(CraftechHandWeapons.MODID)
    public static CraftechHandWeapons instance;

    public static final CreativeTabs  tab     = new CHWTab(MODNAME);

    @SidedProxy(clientSide = "fr.craftechmc.handweapons.client.CHWClientProxy", serverSide = "fr.craftechmc.handweapons.CHWCommonProxy")
    public static CHWCommonProxy      proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.makeSidedOps();
        proxy.loadSide();
    }
}