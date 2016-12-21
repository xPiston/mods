package fr.craftechmc.contentmod.common.blocks.customs;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 *
 * @author Ourten
 *
 */
public class BlockCustomSlab extends BlockSlab
{
    public BlockObject model;

    public BlockCustomSlab(final String name, final BlockObject model)
    {
        super(false, model.getBlock().getMaterial());
        this.model = model;
        this.setStepSound(model.getBlock().stepSound);
        this.setLightOpacity(0);
        this.setUnlocalizedName(name);

        this.setCreativeTab(CTabManager.getTab("CONTENT_DECLINAISON"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int data)
    {
        return this.model.getBlock().getIcon(side, this.model.getMeta());
    }

    @Override
    public Item getItemDropped(final int meta, final Random p_149650_2_, final int p_149650_3_)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public Item getItem(final World par1, final int par2, final int par3, final int par4)
    {
        return Item.getItemFromBlock(this);
    }

    /**
     * Returns an item stack containing a single instance of the current block
     * type. 'i' is the block's subtype/damage and is ignored for blocks which
     * do not support subtypes. Blocks which cannot be harvested should return
     * null.
     */
    @Override
    protected ItemStack createStackedBlock(final int p_149644_1_)
    {
        return new ItemStack(Item.getItemFromBlock(this), 2, p_149644_1_ & 7);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFullSlabName(final int p_150002_1_)
    {
        return this.getUnlocalizedName();
    }
}