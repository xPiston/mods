package fr.craftechmc.contentmod.common.blocks.customs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 *
 * @author Ourten
 *
 */
public class ItemBlockCustomSlab extends ItemBlock
{
    private final Block b;

    public ItemBlockCustomSlab(final Block block)
    {
        super(block);
        this.b = block;
        this.setMaxDurability(0);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int metadata)
    {
        return this.b.getIcon(2, metadata);
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack)
    {
        return ((BlockCustomSlab) this.b).getFullSlabName(stack.getMetadata());
    }

    @Override
    public int getMetadata(final int damageValue)
    {
        return damageValue;
    }
}