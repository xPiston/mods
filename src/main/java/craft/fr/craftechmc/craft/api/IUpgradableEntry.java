package fr.craftechmc.craft.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Desc...
 * Created by Thog the 05/06/2016
 */
public interface IUpgradableEntry
{
    boolean canUpgrade(IInventory inventory);

    ItemStack getResult();
}
