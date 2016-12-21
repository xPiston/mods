package fr.craftechmc.weaponry.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftechWeaponryTab extends CreativeTabs
{
    public CraftechWeaponryTab(String label)
    {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(Items.arrow, 1, 0);
    }

    @Override
    public Item getTabIconItem()
    {
        return this.getIconItemStack().getItem();
    }
}