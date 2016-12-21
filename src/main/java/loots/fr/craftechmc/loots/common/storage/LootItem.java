package fr.craftechmc.loots.common.storage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Ourten 12 déc. 2016
 */
public class LootItem
{
    private String         referenceName;
    private int            referenceMeta;
    private NBTTagCompound tagData;

    private LootItem()
    {
    }

    public LootItem(final String referenceName, final int referenceMeta, final NBTTagCompound tagData)
    {
        this.referenceName = referenceName;
        this.referenceMeta = referenceMeta;
        this.tagData = tagData;
    }

    public static LootItem fromItemStack(final ItemStack stack)
    {
        final LootItem rtn = new LootItem();

        rtn.setReferenceName(Item.itemRegistry.getNameForObject(stack.getItem()));
        rtn.setReferenceMeta(stack.getMetadata());
        if (stack.hasTagCompound())
            rtn.setTagData((NBTTagCompound) stack.getTagCompound().copy());
        return rtn;
    }

    public boolean compareToItemStack(final ItemStack stack)
    {
        if (stack.getMetadata() == this.referenceMeta
                && Item.itemRegistry.getNameForObject(stack.getItem()).equals(this.referenceName))
        {
            if (stack.hasTagCompound())
            {
                if (stack.getTagCompound().equals(this.tagData))
                    return true;
                return false;
            }
            return true;
        }
        return false;
    }

    public String getReferenceName()
    {
        return this.referenceName;
    }

    public void setReferenceName(final String referenceName)
    {
        this.referenceName = referenceName;
    }

    public int getReferenceMeta()
    {
        return this.referenceMeta;
    }

    public void setReferenceMeta(final int referenceMeta)
    {
        this.referenceMeta = referenceMeta;
    }

    public NBTTagCompound getTagData()
    {
        return this.tagData;
    }

    public void setTagData(final NBTTagCompound tagData)
    {
        this.tagData = tagData;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.referenceMeta;
        result = prime * result + (this.referenceName == null ? 0 : this.referenceName.hashCode());
        result = prime * result + (this.tagData == null ? 0 : this.tagData.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final LootItem other = (LootItem) obj;
        if (this.referenceMeta != other.referenceMeta)
            return false;
        if (this.referenceName == null)
        {
            if (other.referenceName != null)
                return false;
        }
        else if (!this.referenceName.equals(other.referenceName))
            return false;
        if (this.tagData == null)
        {
            if (other.tagData != null)
                return false;
        }
        else if (!this.tagData.equals(other.tagData))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "LootItem [referenceName=" + this.referenceName + ", referenceMeta=" + this.referenceMeta + ", tagData="
                + this.tagData + "]";
    }
}