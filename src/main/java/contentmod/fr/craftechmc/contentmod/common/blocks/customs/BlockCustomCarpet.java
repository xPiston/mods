package fr.craftechmc.contentmod.common.blocks.customs;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.util.IIcon;

/**
 * Desc... Created by Thog the 10/07/2016
 */
public class BlockCustomCarpet extends BlockCarpet
{
    private final BlockObject model;

    public BlockCustomCarpet(final String name, final BlockObject model)
    {
        ReflectionHelper.setPrivateValue(Block.class, this, model.getBlock().getMaterial(), "J", "field_149764_J",
                "blockMaterial");
        this.translucent = this.isOpaqueCube();
        this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
        this.setUnlocalizedName(name);
        this.model = model;
        this.setCreativeTab(CTabManager.getTab("CONTENT_DECLINAISON"));
        this.setStepSound(this.model.getBlock().stepSound);
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
