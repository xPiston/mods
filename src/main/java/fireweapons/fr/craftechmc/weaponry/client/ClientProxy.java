package fr.craftechmc.weaponry.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import fr.craftechmc.weaponry.CWEventManager;
import fr.craftechmc.weaponry.CommonProxy;
import fr.craftechmc.weaponry.client.render.entity.RenderBullet;
import fr.craftechmc.weaponry.client.render.item.RenderWeapon;
import fr.craftechmc.weaponry.client.settings.WeaponryModSettings;
import fr.craftechmc.weaponry.entity.EntityBulletProjectile;
import fr.craftechmc.weaponry.items.CWItemManager;
import fr.craftechmc.weaponry.items.WeaponDescribed;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    public static RenderWeapon renderweapon;
    public static final WeaponryModSettings settings = WeaponryModSettings.getSettings();

    @Override
    public void registerRenders()
    {
        // Events
        MinecraftForge.EVENT_BUS.register(new CWEventManager());
        
        settings.init();
        // Items renderer
        for (final WeaponDescribed weapon : CWItemManager.getInstance().weaponsItem)
        {
            ClientProxy.renderweapon = new RenderWeapon(weapon.getName());
            MinecraftForgeClient.registerItemRenderer(weapon, ClientProxy.renderweapon);
        }
        // Entities
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletProjectile.class, new RenderBullet());
    }
}