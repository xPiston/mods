package fr.craftechmc.loots.common.gui;

import fr.craftechmc.loots.common.tiles.TileLootContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLootContainer extends Container
{
    private final TileLootContainer tile;

    public ContainerLootContainer(final InventoryPlayer inventoryPlayer, final TileLootContainer tile)
    {
        this.tile = tile;
        int l;
        for (l = 0; l < 3; ++l)
            for (int i1 = 0; i1 < 9; ++i1)
                this.addSlotToContainer(new Slot(inventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 83 + l * 18));
        for (l = 0; l < 9; ++l)
            this.addSlotToContainer(new Slot(inventoryPlayer, l, 8 + l * 18, 141));
        for (l = 0; l < tile.getSizeInventory(); l++)
            this.addSlotToContainer(new Slot(tile, l, 8 + l % 9 * 18, 16 + l / 9 * 19)
            {
                @Override
                public boolean isItemValid(final ItemStack stack)
                {
                    return false;
                }
            });
    }

    public int getSlotsCount()
    {
        return this.tile.getSizeInventory();
    }

    @Override
    public ItemStack transferStackInSlot(final EntityPlayer player, final int index)
    {
        ItemStack itemstack = null;
        final Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index <= this.getSlotsCount())
            {
                return null;
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.getSlotsCount(), false))
                return null;
            if (itemstack1.stackSize == 0)
                slot.putStack((ItemStack) null);
            else
                slot.onSlotChanged();
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player)
    {
        return this.tile.isUseableByPlayer(player);
    }

    public TileLootContainer getTile()
    {
        return this.tile;
    }
}