package fr.craftechmc.core.common.utils;

import net.minecraft.item.Item;

public class ItemUtils
{
    public static Item getItemFromName(final String registryName)
    {
        if (Item.itemRegistry.containsKey(registryName))
        {
            return (Item) Item.itemRegistry.getObject(registryName);
        }
        else
        {
            try
            {
                return (Item) Item.itemRegistry.getObjectById(Integer.parseInt(registryName));
            } catch (final NumberFormatException numberformatexception)
            {
                return null;
            }
        }
    }
}