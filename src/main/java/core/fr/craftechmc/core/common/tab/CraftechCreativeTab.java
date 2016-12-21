package fr.craftechmc.core.common.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Ourten 30 oct. 2016
 */
public class CraftechCreativeTab extends CreativeTabs
{
    private final Item iconItem;
    private ItemStack  iconStack;

    public CraftechCreativeTab(final String label, final Item item)
    {
        super(label);

        this.iconItem = item;
    }

    public CraftechCreativeTab(final String label, final ItemStack stack)
    {
        this(label, stack.getItem());

        this.iconStack = stack;
    }

    @Override
    public Item getTabIconItem()
    {
        return this.iconItem;
    }

    @Override
    public ItemStack getIconItemStack()
    {
        if (this.iconStack == null)
            return super.getIconItemStack();
        else
            return this.iconStack;
    }
}