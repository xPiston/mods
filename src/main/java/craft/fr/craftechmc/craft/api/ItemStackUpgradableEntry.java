package fr.craftechmc.craft.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Desc...
 * Created by Thog the 05/06/2016
 */
public class ItemStackUpgradableEntry implements IUpgradableEntry
{
    private final ItemStack inputOne, inputSecond, inputThree, output;

    public ItemStackUpgradableEntry(ItemStack[] inputs, ItemStack output)
    {
        this.inputOne = inputs[0];
        this.inputSecond = inputs[1];
        this.inputThree = inputs[2];
        this.output = output;
    }

    @Override public boolean canUpgrade(IInventory inventory)
    {
        return isItemStackEquals(inventory.getStackInSlot(0), inputOne) && isItemStackEquals(
                inventory.getStackInSlot(1), inputSecond) && isItemStackEquals(inventory.getStackInSlot(2), inputThree);
    }

    @Override public ItemStack getResult()
    {
        return output;
    }

    private boolean isItemStackEquals(ItemStack stackOne, ItemStack stackSecond)
    {
        return ItemStack.areItemStacksEqual(stackOne, stackSecond) && ItemStack
                .areItemStackTagsEqual(stackOne, stackSecond);
    }
}
