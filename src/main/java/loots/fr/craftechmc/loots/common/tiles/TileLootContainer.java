package fr.craftechmc.loots.common.tiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.craftechmc.core.common.tiles.TileCraftechBase;
import fr.craftechmc.core.common.utils.ItemUtils;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.objects.ContainerLootEntry;
import fr.craftechmc.loots.common.objects.ContainerLootList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileLootContainer extends TileCraftechBase implements IInventory
{
    private ForgeDirection            orientation;
    private ItemStack[]               stacks;
    private int                       slotNumber;
    private final LootContainerObject loot;

    public TileLootContainer()
    {
        this.stacks = new ItemStack[0];
        this.orientation = ForgeDirection.UNKNOWN;
        this.loot = new LootContainerObject();
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public void setSlotNumber(final int slotNumber)
    {
        this.slotNumber = slotNumber;
        this.stacks = Arrays.copyOf(this.stacks, slotNumber);
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setTag("lootlist", this.loot.toNBT(new NBTTagCompound()));
        tag.setInteger("slotNumber", this.slotNumber);
        for (int i = 0; i < this.slotNumber; i++)
            if (this.stacks[i] != null)
                tag.setTag("stack" + i, this.stacks[i].writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.loot.fromNBT(tag.getCompoundTag("lootlist"));
        this.setSlotNumber(tag.getInteger("slotNumber"));
        for (int i = 0; i < this.slotNumber; i++)
            if (tag.getCompoundTag("stack" + i) != null)
                this.stacks[i] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack" + i));
    }

    @Override
    public int getSizeInventory()
    {
        return this.slotNumber;
    }

    @Override
    public ItemStack getStackInSlot(final int slot)
    {
        return this.stacks[slot];
    }

    @Override
    public ItemStack decrStackSize(final int slot, final int quantity)
    {
        if (this.stacks[slot] != null)
        {
            ItemStack itemstack;

            if (this.stacks[slot].stackSize <= quantity)
            {
                itemstack = this.stacks[slot];
                this.stacks[slot] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.stacks[slot].splitStack(quantity);

                if (this.stacks[slot].stackSize == 0)
                    this.stacks[slot] = null;

                this.markDirty();
                return itemstack;
            }
        }
        else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slot)
    {
        if (this.stacks[slot] != null)
        {
            final ItemStack itemstack = this.stacks[slot];
            this.stacks[slot] = null;
            return itemstack;
        }
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(final int slot, final ItemStack stack)
    {
        this.stacks[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();
        this.markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return "container.lootcontainer.name";
    }

    @Override
    public boolean isCustomInventoryName()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer player)
    {
        return this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == this.getBlockType()
                && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest()
    {
        final ArrayList<ItemStack> generated = Lists.newArrayList(this.loot.generate(1));
        if (generated.size() != 0)
        {
            this.wipeInventory();
            for (int i = 0; i < generated.size(); i++)
                this.setInventorySlotContents(i, generated.get(i));
        }
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack stack)
    {
        return false;
    }

    public void wipeInventory()
    {
        for (int i = 0; i < this.getStacks().length; i++)
            this.getStacks()[i] = null;
    }

    public ForgeDirection getOrientation()
    {
        return this.orientation;
    }

    public void setOrientation(final ForgeDirection orientation)
    {
        this.orientation = orientation;
    }

    public ItemStack[] getStacks()
    {
        return this.stacks;
    }

    public int getSlotNumber()
    {
        return this.slotNumber;
    }

    public LootContainerObject getLoot()
    {
        return this.loot;
    }

    public static final class LootContainerObject
    {
        /**
         * A Map using as key a Pair representing a LootList by the lootTable
         * name owning the list and the level of the selected list. The value is
         * the ready time of the lootList, if that time is in the future, then
         * if will be modulated accordingly to the {@link ContainerLootList}
         * specifications.
         *
         * @see ContainerLootList#getRespawnTime()
         * @see ContainerLootList#getModulation()
         */
        private final HashMap<Pair<String, Integer>, Long> lootLists;

        public LootContainerObject()
        {
            this.lootLists = Maps.newHashMap();
        }

        /**
         * @param maxSlots
         *            the maximum number of slots the container can accept, no
         *            more will be returned.
         * @return An array containing the generated stacks. Can be empty if no
         *         lists is ready.
         */
        public Collection<ItemStack> generate(final int maxSlots)
        {
            final long currentTime = System.currentTimeMillis();
            final Random rand = new Random();
            final HashMap<Float, ItemStack> rtn = Maps.newHashMap();
            for (final Entry<Pair<String, Integer>, Long> lootListEntry : this.lootLists.entrySet())
            {
                final ContainerLootList lootList = ContainerLootTablesManager.getInstance()
                        .getLootListByIdentifiers(lootListEntry.getKey().getLeft(), lootListEntry.getKey().getRight());
                final long readyTime = lootListEntry.getValue();
                if (readyTime != 0 && currentTime < readyTime)
                {
                    if (readyTime - currentTime > lootList.getRespawnTime() * (1 - 1 / lootList.getModulation()))
                        this.lootLists.replace(lootListEntry.getKey(),
                                (long) (currentTime + (readyTime - currentTime) * lootList.getModulation()));
                }
                else
                {
                    for (final ContainerLootEntry entry : lootList.getLoots())
                    {
                        final float random = rand.nextFloat();
                        if (random <= entry.getChance())
                            if (rtn.size() >= maxSlots)
                            {
                                for (final Entry<Float, ItemStack> entryStack : rtn.entrySet())
                                    if (entryStack.getKey() > entry.getChance())
                                    {
                                        rtn.remove(entryStack.getKey());
                                        rtn.put((float) entry.getChance(), new ItemStack(
                                                ItemUtils.getItemFromName(entry.getItem().getReferenceName()),
                                                entry.getQuantityMin()
                                                        + rand.nextInt(entry.getQuantityMax() - entry.getQuantityMin()),
                                                entry.getItem().getReferenceMeta()));
                                    }
                            }
                            else
                                rtn.put((float) entry.getChance(),
                                        new ItemStack(ItemUtils.getItemFromName(entry.getItem().getReferenceName()),
                                                entry.getQuantityMin()
                                                        + rand.nextInt(entry.getQuantityMax() - entry.getQuantityMin()),
                                                entry.getItem().getReferenceMeta()));
                    }
                    this.lootLists.replace(lootListEntry.getKey(), currentTime + lootList.getRespawnTime());
                }
            }
            return rtn.values();
        }

        public HashMap<Pair<String, Integer>, Long> getLootLists()
        {
            return this.lootLists;
        }

        public NBTTagCompound toNBT(final NBTTagCompound tag)
        {
            int i = 0;
            for (final Entry<Pair<String, Integer>, Long> entry : this.lootLists.entrySet())
            {
                tag.setString(i + "lootlistName", entry.getKey().getLeft());
                tag.setInteger(i + "lootlistLevel", entry.getKey().getRight());
                tag.setLong(i + "readyTime", entry.getValue());
                i++;
            }
            tag.setInteger("lootQty", i);
            return tag;
        }

        public LootContainerObject fromNBT(final NBTTagCompound tag)
        {
            this.getLootLists().clear();

            final int qty = tag.getInteger("lootQty");
            for (int i = 0; i < qty; i++)
                this.getLootLists().put(Pair.of(tag.getString(i + "lootlistName"), tag.getInteger(i + "lootlistLevel")),
                        tag.getLong(i + "readyTime"));
            return this;
        }
    }
}