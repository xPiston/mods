package fr.craftechmc.craft.container;

import fr.craftechmc.craft.CraftechCraft;
import fr.craftechmc.craft.network.PacketWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Slot of upgrade part
 * Created by Thog
 * 06/08/2016
 */
public class SlotOutput extends Slot
{

    private final IInventory inputInventory;
    private       ItemStack  opaqueStack;

    public SlotOutput(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition,
            IInventory inputInventory)
    {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.inputInventory = inputInventory;
    }

    @Override public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    @Override public boolean canTakeStack(EntityPlayer player)
    {
        return true;
    }

    @Override public boolean getHasStack()
    {
        return super.getHasStack();
    }

    @Override public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
    {
        inputInventory.setInventorySlotContents(0, null);
        inputInventory.setInventorySlotContents(1, null);
        inputInventory.setInventorySlotContents(2, null);
        super.onPickupFromSlot(player, stack);
    }

    public void setOpaqueStack(EntityPlayer player, ItemStack opaqueStack)
    {
        if (player != null && !player.worldObj.isRemote && opaqueStack != this.opaqueStack)
        {
            CraftechCraft.getProxy().pipeline()
                    .sendTo(new PacketWorkbench((byte) 2, opaqueStack), (EntityPlayerMP) player);
        }
        this.opaqueStack = opaqueStack;
    }

    public ItemStack getOpaqueStack()
    {
        return opaqueStack;
    }
}

