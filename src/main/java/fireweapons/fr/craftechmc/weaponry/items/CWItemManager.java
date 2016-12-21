package fr.craftechmc.weaponry.items;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.baldr.BaldrStorage;
import fr.craftechmc.baldr.resource.objects.BaldrContentDescriptor;
import fr.craftechmc.core.common.tab.CTabManager;
import fr.craftechmc.needs.client.render.ISRSimpleInventoryModelRenderer;
import fr.craftechmc.weaponry.CraftechWeaponry;
import fr.craftechmc.weaponry.items.throwable.EntityThrowingKnife;
import fr.craftechmc.weaponry.items.throwable.ThrowingWeaponItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class CWItemManager
{
    private static volatile CWItemManager INSTANCE;

    public static CWItemManager getInstance()
    {
        if (CWItemManager.INSTANCE == null)
            synchronized (CWItemManager.class)
            {
                if (CWItemManager.INSTANCE == null)
                    CWItemManager.INSTANCE = new CWItemManager();
            }
        return CWItemManager.INSTANCE;
    }

    private final Gson                      GSON;

    public final ArrayList<String>          weapons;
    public final ArrayList<WeaponDescribed> weaponsItem;
    public ArrayList<LoaderDescribed>       loaderItem;

    public CWItemManager()
    {
        this.GSON = new Gson();

        this.weapons = Lists.newArrayList();
        this.weaponsItem = Lists.newArrayList();
        this.loaderItem = Lists.newArrayList();
    }

    public void preInitItems()
    {
        this.registerRepetionWeapon("p90", 2, 45, 2, 50, 24, 50);
        this.registerRepetionWeapon("famas", 2, 90, 2, 25, 12, 40);
        this.registerRepetionWeapon("hk416", 2, 90, 2, 30, 3, 60);
        this.registerRepetionWeapon("fn-minimi", 2, 90, 2, 25, 12, 40);

        // Throwable weapons
        GameRegistry.registerItem(new ThrowingWeaponItem().setUnlocalizedName("throwingKnife").setTextureName(""),
                "throwingKnife", CraftechWeaponry.MODID);
        EntityRegistry.registerModEntity(EntityThrowingKnife.class, "throwingKnife",
                EntityRegistry.findGlobalUniqueEntityId(), CraftechWeaponry.instance, 64, 20, true);

        // Baldr for hand weapons
        final BaldrStorage provider = BaldrStorage.getInstance();
        final BaldrContentDescriptor itemsDescriptor = provider.getContentDescriptor("craftechweaponry-hand");

        final boolean isClient = FMLCommonHandler.instance().getEffectiveSide().isClient();

        // Fetch groups
        for (final String contentFileName : itemsDescriptor.getContentFiles().keySet())
        {
            final HandWeaponGroupDescriptor weaponGroupDescriptor = this.GSON.fromJson(
                    provider.getContentFile(itemsDescriptor, contentFileName), HandWeaponGroupDescriptor.class);

            // Fetch items
            for (final HandWeaponDescriptor weaponDescriptor : weaponGroupDescriptor.getDescriptors())
            {
                final HandWeaponDescribed item = new HandWeaponDescribed(weaponDescriptor.getName(),
                        weaponDescriptor.getTextureName(), weaponDescriptor.getDamages(),
                        weaponDescriptor.getActionTime(), weaponDescriptor.getReloadTime(),
                        weaponDescriptor.getEffects(), weaponDescriptor.isCanParade());
                item.setTextureName("craftechweaponry-hand:" + weaponDescriptor.getTextureName());

                if (isClient && !weaponDescriptor.getModelName().isEmpty())
                    MinecraftForgeClient.registerItemRenderer(item, new ISRSimpleInventoryModelRenderer(item,
                            weaponDescriptor.getTextureName(), weaponDescriptor.getModelName()));

                // Finally register the item
                GameRegistry.registerItem(item, weaponDescriptor.getName(), CraftechWeaponry.MODID);
            }
        }
    }

    private void registerRepetionWeapon(final String name, final int damage, final int fireRange, final int fireRate,
            final int loaderCapacity, final int bulletDispersion, final int reloadTime)
    {
        final LoaderDescribed newLoader = (LoaderDescribed) new LoaderDescribed().setUnlocalizedName(name + "-loader")
                .setCreativeTab(CTabManager.getTab("WEAPONRY_FIRE"));
        newLoader.setMaxDurability(loaderCapacity);
        final WeaponDescribed newWeapon = (WeaponDescribed) new WeaponDescribed().setUnlocalizedName(name)
                .setCreativeTab(CTabManager.getTab("WEAPONRY_FIRE"));
        newWeapon.setName(name);
        newWeapon.setBulletDamage(damage);
        newWeapon.setFireRange(fireRange);
        newWeapon.setFireRate(fireRate);
        newWeapon.setLoader(newLoader);
        newWeapon.setMaxDurability(loaderCapacity);
        newWeapon.setBulletDispersion(bulletDispersion);
        newWeapon.setReloadTime(reloadTime);
        this.loaderItem.add(newLoader);
        this.registerItem(newLoader);
        this.weaponsItem.add(newWeapon);
        this.registerItem(newWeapon);

    }

    private void registerItem(final Item item)
    {
        GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
    }
}