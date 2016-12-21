package fr.craftechmc.loots.common.manager;

import java.util.Optional;

import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.loots.common.storage.LootItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author Ourten 12 déc. 2016
 */
public class LootItemManager
{
    private static volatile LootItemManager   serverInstance, clientInstance = null;
    private final HashBiMap<String, LootItem> lootItems;

    private LootItemManager(final Side side)
    {
        this.lootItems = HashBiMap.create();

        if (side.isServer())
        {
            this.lootItems.put(Items.apple.getUnlocalizedName() + ":" + 0,
                    LootItem.fromItemStack(new ItemStack(Items.apple, 1, 0)));
            this.lootItems.put(Items.apple.getUnlocalizedName() + ":" + 0,
                    LootItem.fromItemStack(new ItemStack(Items.golden_apple, 1, 0)));
        }
    }

    public Optional<String> getLootItemName(final ItemStack stack)
    {
        if (this.lootItems.values().stream().anyMatch(item -> item.compareToItemStack(stack)))
            return Optional.of(this.lootItems.inverse().get(
                    this.lootItems.values().stream().filter(item -> item.compareToItemStack(stack)).findFirst().get()));
        return Optional.of(null);
    }

    public Optional<LootItem> getLootItem(final String name)
    {
        return Optional.ofNullable(this.lootItems.get(name));
    }

    public String addLootItem(final ItemStack stack)
    {
        this.lootItems.put(stack.getUnlocalizedName() + ":" + stack.getMetadata(), LootItem.fromItemStack(stack));
        return stack.getUnlocalizedName() + ":" + stack.getMetadata();
    }

    public boolean removeLootItem(final ItemStack stack)
    {
        return this.lootItems.values().removeIf(item -> item.compareToItemStack(stack));
    }

    public boolean removeLootItem(final String name)
    {
        return this.lootItems.remove(name) == null;
    }

    public HashBiMap<String, LootItem> getLootItems()
    {
        return this.lootItems;
    }

    public static final LootItemManager getInstance()
    {
        return LootItemManager.getInstance(FMLCommonHandler.instance().getEffectiveSide());
    }

    public static final LootItemManager getInstance(final Side side)
    {
        if (side.isClient())
        {
            if (LootItemManager.clientInstance == null)
                synchronized (LootItemManager.class)
                {
                    if (LootItemManager.clientInstance == null)
                        LootItemManager.clientInstance = new LootItemManager(side);
                }
            return LootItemManager.clientInstance;
        }
        if (LootItemManager.serverInstance == null)
            synchronized (LootItemManager.class)
            {
                if (LootItemManager.serverInstance == null)
                    LootItemManager.serverInstance = new LootItemManager(side);
            }
        return LootItemManager.serverInstance;
    }
}