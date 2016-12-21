package fr.craftechmc.craft.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 * ItemStack workbench entry implementation Created by Thog the 15/05/2016
 */
public class ItemStackEntry implements IWorkbenchEntry
{

    private final ItemStack            output;
    private final ItemStackEntryRender render;
    private final ItemStack[]          inputs;

    public ItemStackEntry(final ItemStack result, final String name, final ItemStack[] inputs)
    {
        this.output = result;
        this.render = new ItemStackEntryRender(result, name);
        this.inputs = inputs;
    }

    @Override
    public Render getRender()
    {
        return this.render;
    }

    @Override
    public boolean canCraft(final EntityPlayer player)
    {
        final InventoryPlayer inventoryPlayer = player.inventory;
        final Map<String, ItemStack> inventoryContent = new HashMap<>();

        for (final ItemStack slotStack : inventoryPlayer.mainInventory)
            if (slotStack != null)
            {
                final String entryName = slotStack.getUnlocalizedName() + ":" + slotStack.getMetadata();
                if (inventoryContent.containsKey(entryName) && slotStack.isItemEqual(inventoryContent.get(entryName)))
                    inventoryContent.get(entryName).stackSize += slotStack.stackSize;
                else
                    inventoryContent.put(entryName, slotStack.copy());
            }

        for (final ItemStack requirement : this.inputs)
        {
            final ItemStack inv = inventoryContent
                    .get(requirement.getUnlocalizedName() + ":" + requirement.getMetadata());
            if (inv == null || (inv.stackSize < requirement.stackSize))
                return false;
        }
        inventoryContent.clear();
        return true;
    }

    @Override
    public boolean concludeTransaction(final EntityPlayer player)
    {
        final InventoryPlayer inventoryPlayer = player.inventory;

        for (final ItemStack requirement : this.inputs)
            if (!this.consumeInventoryItem(inventoryPlayer, requirement))
                return false;
        if (player.inventory.addItemStackToInventory(this.output.copy()))
            player.inventoryContainer.detectAndSendChanges();
        return true;
    }

    private boolean consumeInventoryItem(final InventoryPlayer player, final ItemStack stack)
    {
        final int id = this.getSlotID(player, stack);

        if (id < 0)
            return false;
        else
        {
            player.mainInventory[id].stackSize -= stack.stackSize;
            if (player.mainInventory[id].stackSize <= 0)
                player.mainInventory[id] = null;
            return true;
        }
    }

    private int getSlotID(final InventoryPlayer player, final ItemStack stack)
    {
        for (int i = 0; i < player.mainInventory.length; ++i)
            if (player.mainInventory[i] != null && player.mainInventory[i].isItemEqual(stack))
                return i;
        return -1;
    }
}
