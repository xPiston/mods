package fr.craftechmc.loots.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * @author Ourten 29 oct. 2016
 */
public class ContainerEditLootList extends Container
{

    public ContainerEditLootList(final InventoryPlayer inventoryPlayer)
    {
        int l;
        for (l = 0; l < 3; ++l)
            for (int i1 = 0; i1 < 9; ++i1)
                this.addSlotToContainer(new Slot(inventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 83 + l * 18)
                {
                    @Override
                    public boolean canTakeStack(final EntityPlayer p_82869_1_)
                    {
                        return false;
                    }
                });
        for (l = 0; l < 9; ++l)
            this.addSlotToContainer(new Slot(inventoryPlayer, l, 8 + l * 18, 141)
            {
                @Override
                public boolean canTakeStack(final EntityPlayer p_82869_1_)
                {
                    return false;
                }
            });
    }

    @Override
    public boolean canInteractWith(final EntityPlayer p_75145_1_)
    {
        return true;
    }
}