package fr.craftechmc.core.client.render;

import net.minecraft.item.ItemStack;

public interface IInventoryRenderer
{
    public void renderInventory(double x, double y, double z, int meta, String unloc, ItemStack stack);
}