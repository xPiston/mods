package fr.craftechmc.craft.container;

import fr.craftechmc.craft.CraftechCraft;
import fr.craftechmc.craft.api.IUpgradableEntry;
import fr.craftechmc.craft.api.WorkbenchRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Workbench container
 * Created by Thog the 16/04/2016
 */
public class WorkbenchContainer extends Container
{
    private final World worldObj;
    private final int   posX, posY, posZ;
    /**
     * 0: Result
     * 1: First slot
     * 2: Second slot
     * 3: Last slot
     */
    private final Slot[] upgradeSlots = new Slot[4];
    private final IInventory inventory, playerInventory;
    private final EntityPlayer player;
    private IInventory outputSlot = new InventoryCraftResult();
    private int tabID;

    public WorkbenchContainer(EntityPlayer player, World world, int x, int y, int z)
    {
        this.worldObj = world;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.player = player;
        this.playerInventory = player.inventory;
        this.inventory = new InventoryBasic("container.workbench", false, 4)
        {
            @Override public void markDirty()
            {
                super.markDirty();
                WorkbenchContainer.this.onCraftMatrixChanged(this);
            }
        };

        int l;

        for (l = 0; l < 3; ++l)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(playerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 100 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 158));
        }

        // Upgrade slots (Out of screen, avoid rendering)
        for (int i = 0; i < 3; i++)
        {
            upgradeSlots[i] = new Slot(inventory, i, Integer.MAX_VALUE, Integer.MAX_VALUE);
            // Revert old slot number because we have different inventories
            this.addSlotToContainer(upgradeSlots[i]);
        }

        upgradeSlots[3] = new SlotOutput(outputSlot, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, inventory);
        this.addSlotToContainer(upgradeSlots[3]);
    }

    @Override public boolean canInteractWith(EntityPlayer player)
    {
        return this.worldObj.getBlock(this.posX, this.posY, this.posZ) == CraftechCraft.WORKBENCH
                && player.getDistanceSq((double) this.posX + 0.5D, (double) this.posY + 0.5D, (double) this.posZ + 0.5D)
                <= 64.0D;
    }

    @Override public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
        return null;
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override public void onCraftMatrixChanged(IInventory inventory)
    {
        if (this.inventory == inventory)
        {
            SlotOutput out = ((SlotOutput) upgradeSlots[3]);

            IUpgradableEntry entry = WorkbenchRegistry.getUpgradeEntry(inventory);
            if (entry != null)
                out.setOpaqueStack(player, entry.getResult());
            else
                out.setOpaqueStack(player, null);
        }
        super.onCraftMatrixChanged(inventory);
    }

    public int getTabID()
    {
        return tabID;
    }

    public void setTabID(int tabID)
    {
        this.tabID = tabID;

        if (tabID == 0)
        {
            for (int i = 0; i < 4; i++)
            {
                upgradeSlots[i].yDisplayPosition = Integer.MAX_VALUE;
                upgradeSlots[i].xDisplayPosition = Integer.MAX_VALUE;
            }
        }
        else
        {
            upgradeSlots[3].yDisplayPosition = 51;
            upgradeSlots[3].xDisplayPosition = 98;
            upgradeSlots[0].yDisplayPosition = 31;
            upgradeSlots[0].xDisplayPosition = 62;
            upgradeSlots[1].yDisplayPosition = 51;
            upgradeSlots[1].xDisplayPosition = 62;
            upgradeSlots[2].yDisplayPosition = 71;
            upgradeSlots[2].xDisplayPosition = 62;
        }
    }

    @Override public void onContainerClosed(EntityPlayer player)
    {
        for (int i = 0; i < 4; i++)
            if (upgradeSlots[i].getHasStack())
                player.dropPlayerItemWithRandomChoice(upgradeSlots[i].getStack(), false);
        super.onContainerClosed(player);
    }

    public Slot getUpgradeSlot(int id)
    {
        return upgradeSlots[id];
    }

    public IInventory getInventoryUpgrade()
    {
        return inventory;
    }
}
