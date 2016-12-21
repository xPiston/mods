package fr.craftechmc.contentmod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBasicDescribed extends ItemBlock
{
    private final Block block;

    public ItemBlockBasicDescribed(final Block block)
    {
        super(block);
        this.block = block;
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack)
    {
        if (stack != null)
            return ((BlockDescribed) this.block).getNames()[stack.getMetadata()];
        return "ERROR";
    }

    @Override
    public int getMetadata(final int meta)
    {
        return meta;
    }
}