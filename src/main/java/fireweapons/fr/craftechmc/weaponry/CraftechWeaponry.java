package fr.craftechmc.weaponry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import fr.craftechmc.weaponry.entity.EntityBulletProjectile;
import fr.craftechmc.weaponry.items.CWItemManager;
import fr.craftechmc.weaponry.network.PacketPipeline;
import net.minecraft.util.DamageSource;

@Mod(modid = CraftechWeaponry.MODID, version = CraftechWeaponry.MODVER, name = CraftechWeaponry.MODNAME, dependencies = "required-after:craftechcore")
public class CraftechWeaponry
{
    public static final String         MODID          = "craftechweaponry";
    public static final String         MODVER         = "0.0.1";
    public static final String         MODNAME        = "CraftechWeaponry";
    public static final String         MODASSETS      = "craftechweaponry";
    @Instance(CraftechWeaponry.MODID)
    public static CraftechWeaponry     instance;

    @SidedProxy(clientSide = "fr.craftechmc.weaponry.client.ClientProxy", serverSide = "fr.craftechmc.weaponry.CommonProxy")
    public static CommonProxy          proxy;

    public static final PacketPipeline packetPipeline = new PacketPipeline();

    public CWItemManager               itemManager    = CWItemManager.getInstance();

    public static DamageSource         weaponDamageSource;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        CraftechWeaponry.proxy.preInit(event);
        CraftechWeaponry.proxy.loadSide();
        this.itemManager.preInitItems();
        CraftechWeaponry.weaponDamageSource = new DamageSource("weaponDamageSource").setProjectile();

        EntityRegistry.registerModEntity(EntityBulletProjectile.class, "BulletProjectile", 0, this, 64, 10, true);

        CraftechWeaponry.proxy.registerRenders();

        FMLCommonHandler.instance().bus().register(new CWEventManager());
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        CraftechWeaponry.packetPipeline.initalise();
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        CraftechWeaponry.packetPipeline.postInitialise();
    }
}