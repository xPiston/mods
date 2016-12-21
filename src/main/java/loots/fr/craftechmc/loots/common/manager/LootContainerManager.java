package fr.craftechmc.loots.common.manager;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.contentmod.common.objects.EOrientable;
import fr.craftechmc.core.common.tab.CTabManager;
import fr.craftechmc.core.common.utils.MaterialUtils;
import fr.craftechmc.loots.common.CraftechLoots;
import fr.craftechmc.loots.common.blocks.BlockLootContainer;
import fr.craftechmc.loots.common.objects.LootContainerDescriptor;
import fr.craftechmc.loots.common.tiles.TileLootContainer;

public class LootContainerManager
{
    private static volatile LootContainerManager     instance = null;
    private final ArrayList<LootContainerDescriptor> lootDescriptors;

    private LootContainerManager()
    {
        this.lootDescriptors = Lists.newArrayList();
    }

    public static final LootContainerManager getInstance()
    {
        if (LootContainerManager.instance == null)
            synchronized (LootContainerManager.class)
            {
                if (LootContainerManager.instance == null)
                    LootContainerManager.instance = new LootContainerManager();
            }
        return LootContainerManager.instance;
    }

    public final void buildLootContainer()
    {
        GameRegistry.registerTileEntity(TileLootContainer.class, CraftechLoots.MODID + ":TileLootContainer");
        this.lootDescriptors.forEach(descriptor ->
        {
            final BlockLootContainer block = new BlockLootContainer(
                    MaterialUtils.getMaterialFromName(descriptor.getMaterial()));
            block.setCreativeTab(CTabManager.getTab("LOOTS"));
            block.setUnlocalizedName(descriptor.getName());
            block.setLightLevel(descriptor.getLightning());
            if (!StringUtils.isEmpty(descriptor.getOrientable()))
                block.setOrientable(EOrientable.valueOf(descriptor.getOrientable()));
            block.setTextureName("craftechloot-container:" + descriptor.getTexture());

            GameRegistry.registerBlock(block, descriptor.getName());
        });
    }
}