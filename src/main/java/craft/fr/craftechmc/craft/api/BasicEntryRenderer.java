package fr.craftechmc.craft.api;

import net.minecraft.item.ItemStack;

/**
 * Basic entry renderer
 * Created by Thog the 15/05/2016
 */
public class BasicEntryRenderer implements IWorkbenchEntry.Render
{
    protected final ItemStack render;
    protected final String    name;

    public BasicEntryRenderer(ItemStack stack, String name)
    {
        this.render = stack;
        this.name = name;
    }

    @Override public ItemStack getIcon()
    {
        return render;
    }

    @Override public String getName()
    {
        return name;
    }
}
