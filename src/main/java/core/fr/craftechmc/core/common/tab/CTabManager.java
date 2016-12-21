package fr.craftechmc.core.common.tab;

import java.util.HashMap;
import java.util.logging.Level;

import com.google.common.collect.Maps;

import fr.craftechmc.core.common.CraftechCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Ourten 30 oct. 2016
 */
public class CTabManager
{
    private static final HashMap<String, CreativeTabs> tabs = Maps.newHashMap();

    public static final void addTab(final String identifier, final Item icon)
    {
        CTabManager.addTab(identifier, identifier, icon);
    }

    public static final void addTab(final String identifier, final ItemStack icon)
    {
        CTabManager.addTab(identifier, identifier, icon);
    }

    public static final void addTab(final String identifier, final String title, final Item icon)
    {
        CTabManager.tabs.put(identifier, new CraftechCreativeTab(title, icon));
    }

    public static final void addTab(final String identifier, final String title, final ItemStack icon)
    {
        CTabManager.tabs.put(identifier, new CraftechCreativeTab(title, icon));
    }

    public static final CreativeTabs getTab(final String identifier)
    {
        if (!CTabManager.tabs.containsKey(identifier))
            CraftechCore.logger.log(Level.SEVERE, "A unregistered CreativeTabs has been requested : " + identifier);
        return CTabManager.tabs.get(identifier);
    }
}