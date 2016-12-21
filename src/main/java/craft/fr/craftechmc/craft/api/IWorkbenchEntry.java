package fr.craftechmc.craft.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Desc...
 * Created by Thog the 15/05/2016
 */
public interface IWorkbenchEntry
{
    Render getRender();

    boolean canCraft(EntityPlayer player);

    boolean concludeTransaction(EntityPlayer player);

    interface Render
    {
        ItemStack getIcon();

        String getName();
    }
}
