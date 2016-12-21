package fr.craftechmc.contentmod.common.blocks.customs;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * Generic metadata Item Block
 * @author Ourten
 * Modified by Thog
 */
public class ItemBlockGenericMeta extends ItemBlock
{
    public ItemBlockGenericMeta(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damageValue)
    {
        return damageValue;
    }
}