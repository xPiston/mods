package fr.craftechmc.handweapons;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CHWTab extends CreativeTabs
{
    public CHWTab(String label)
    {
        super(label);
    }

    @Override
    public Item getTabIconItem()
    {
        return Items.diamond_sword;
    }
}