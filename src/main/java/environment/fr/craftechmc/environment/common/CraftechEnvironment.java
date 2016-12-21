package fr.craftechmc.environment.common;

import java.util.logging.Logger;
import com.unascribed.lambdanetwork.LambdaNetwork;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import fr.craftechmc.environment.common.blocks.BlockFluid;
import fr.craftechmc.environment.common.items.ItemNonDrinkableWaterBucket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.fluids.Fluid;

/**
 * Craftech environment mod
 * Created by arisu on 23/07/2016.
 */
@Mod(modid = CraftechEnvironment.MODID, version = CraftechEnvironment.VERSION, name = CraftechEnvironment.MODNAME, dependencies = "required-after:craftechcontentmod;required-after:craftechcore;required-after:craftechzombie")
public class CraftechEnvironment
{
    public static final String MODID     = "craftechenvironment";
    public static final String MODNAME   = "craftechenvironment";
    public static final String VERSION   = "1.0";
    public static final String MODASSETS = "craftechenvironment";

    @Mod.Instance(CraftechEnvironment.MODID)
    public static CraftechEnvironment instance;

    public static final Logger LOGGER = Logger.getLogger(CraftechEnvironment.MODID);

    @SidedProxy(clientSide = "fr.craftechmc.environment.client.ClientProxy", serverSide = "fr.craftechmc.environment.server.ServerProxy")
    public static CommonProxy proxy;

    public static LambdaNetwork network;

    public static Fluid      nonDrinkableWater           = new Fluid("non_drinkable_water")
            .setUnlocalizedName("NonDrinkableWater").setDensity(500).setViscosity(1200).setTemperature(300);
    public static Block      nonDrinkableWaterBlock;
    public static ItemBucket itemNonDrinkableWaterBucket;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(proxy);
        proxy.init();
    }

    public static void registerEntity(Class<? extends Entity> entityClass, String name)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 1, true);
    }
}
