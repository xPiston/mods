package fr.craftechmc.handweapons.items;

import com.google.gson.Gson;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.handweapons.CraftechHandWeapons;
import fr.craftechmc.handweapons.client.RenderWeaponModel;
import fr.craftechmc.handweapons.objects.HandWeaponsDescriptor;
import fr.craftechmc.handweapons.objects.HandWeaponsGroupDescriptor;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.ArrayList;

public class ItemsManager
{
    private final Gson             GSON;
    public final ArrayList<String> handweaponsgroups;

    private ItemsManager()
    {
        this.GSON = new Gson();

        this.handweaponsgroups = new ArrayList<String>();
    }

    private static ItemsManager INSTANCE;

    public static final ItemsManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new ItemsManager();
        return INSTANCE;
    }

    public final void buildHandWeapons()
    {
        ArrayList<HandWeaponsGroupDescriptor> handWeaponsGroupDescriptors = new ArrayList<HandWeaponsGroupDescriptor>();
        for (String hwgroup : handweaponsgroups)
            handWeaponsGroupDescriptors.add(GSON.fromJson(hwgroup, HandWeaponsGroupDescriptor.class));

        for (HandWeaponsGroupDescriptor hwgdescriptor : handWeaponsGroupDescriptors)
        {
            for (HandWeaponsDescriptor descriptor : hwgdescriptor.getDescriptors())
            {
                ItemWeaponDescribed weapon = (ItemWeaponDescribed) new ItemWeaponDescribed()
                        .setCreativeTab(CraftechHandWeapons.tab)
                        .setUnlocalizedName(descriptor.getName());
                weapon.setDescriptor(descriptor);

                GameRegistry.registerItem(weapon, descriptor.getName());

                if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                    MinecraftForgeClient.registerItemRenderer(weapon,
                            new RenderWeaponModel(descriptor.getTexture(), descriptor.getModel())
                                    .setScale(descriptor.getScale()).setInventoryScale(descriptor.getInventoryScale())
                                    .setLootScale(descriptor.getLootScale())
                                    .setLootRotation(descriptor.getLootRotationX(), descriptor.getLootRotationY(),
                                            descriptor.getLootRotationZ())
                                    .setRotation(descriptor.getRotationX(), descriptor.getRotationY(),
                                            descriptor.getRotationZ())
                                    .setThirdPersonRotation(descriptor.getThirdPersonRotationX(),
                                            descriptor.getThirdPersonRotationY(), descriptor.getThirdPersonRotationZ())
                                    .setInventoryPos(descriptor.getInventoryPosX(), descriptor.getInventoryPosY())
                                    .setInventoryRotation(descriptor.getInventoryRotationX(),
                                            descriptor.getInventoryRotationY(), descriptor.getInventoryRotationZ()));
            }
        }
    }
}