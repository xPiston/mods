package fr.craftechmc.zombie.common;

import org.yggard.brokkgui.paint.Color;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.zombie.common.entity.EntityZombieCraftech;
import fr.craftechmc.zombie.common.event.PreventSpawnEvent;
import fr.craftechmc.zombie.common.event.SoundEvent;
import fr.craftechmc.zombie.common.sound.ModifierList;
import fr.craftechmc.zombie.common.sound.SoundList;
import fr.craftechmc.zombie.common.sound.SoundManager;
import fr.craftechmc.zombie.common.util.Util;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Phenix246 on 20/06/2016.
 */
@Mod(modid = CraftechZombie.MODID, name = CraftechZombie.MODNAME, version = CraftechZombie.MODVER, dependencies = "required-after:craftechcontentmod")
public class CraftechZombie
{
    public static final String   MODID        = "craftechzombie";
    public static final String   MODNAME      = "Craftech | Zombie";
    public static final String   MODVER       = "alpha-0.2";

    @Instance(CraftechZombie.MODID)
    public static CraftechZombie instance;

    @SidedProxy(clientSide = "fr.craftechmc.zombie.client.CZClientProxy", serverSide = "fr.craftechmc.zombie.server.CZServerProxy")
    public static CZCommonProxy  proxy;

    public static SoundManager   soundManager = new SoundManager();

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        Util.registerZombie(CraftechZombie.instance, EntityZombieCraftech.class, "craftech_zombie_classic",
                new Color(255, 0, 0), new Color(0, 153, 76));
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new PreventSpawnEvent());
        MinecraftForge.EVENT_BUS.register(new SoundEvent());

        CraftechZombie.proxy.registerEntity();

        SoundList.registerSounds();
        ModifierList.registerModifier();

    }
}
