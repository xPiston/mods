package fr.craftechmc.craft.api;

import net.minecraft.item.ItemStack;

/**
 * Simple ItemStack entry renderer
 * Created by Thog the 15/05/2016
 */
public class ItemStackEntryRender extends BasicEntryRenderer
{

    public ItemStackEntryRender(ItemStack stack, String name)
    {
        super(stack, name);
    }

    @Override public String getName()
    {
        return name == null ? render.getDisplayName() : name;
    }
}
