package fr.craftechmc.weaponry.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LoaderDescribed extends Item
{
    public LoaderDescribed()
    {
        super();
        this.maxStackSize = 1;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack,World w,EntityPlayer player)
    {
        stack.damageItem(5, player);
        return stack;
    }
}
