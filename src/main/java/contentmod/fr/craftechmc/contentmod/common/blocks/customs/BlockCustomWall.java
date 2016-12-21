package fr.craftechmc.contentmod.common.blocks.customs;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.BlockWall;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 *
 * @author Ourten
 *
 */
public class BlockCustomWall extends BlockWall
{
    public BlockObject model;

    public BlockCustomWall(final String name, final BlockObject model)
    {
        super(model.getBlock());
        this.setUnlocalizedName(name);
        this.setCreativeTab(CTabManager.getTab("CONTENT_DECLINAISON"));
        this.model = model;
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

    @Override
    public int getDamageValue(final World p_149643_1_, final int p_149643_2_, final int p_149643_3_,
            final int p_149643_4_)
    {
        return super.getDamageValue(p_149643_1_, p_149643_2_, p_149643_3_, p_149643_4_);
    }

    @Override
    public int damageDropped(final int meta)
    {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return this.model.getBlock().getRenderBlockPass();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta)
    {
        return this.model.getBlock().getIcon(side, this.model.getMeta());
    }
}