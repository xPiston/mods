package fr.craftechmc.craft.block;

import fr.craftechmc.craft.CraftechCraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Workbench block Created by Thog the 16/04/2016
 */
public class BlockWorkbench extends BlockContainer
{
    public BlockWorkbench()
    {
        super(Material.wood);
        this.setTextureName("stone");
        this.setUnlocalizedName("workbench");
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int side, final float hitX, final float hitY, final float hitZ)
    {
        if (!world.isRemote)
            player.openGui(CraftechCraft.getInstance(), 0, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int id)
    {
        return new Tile();
    }

    // Useless
    public static final class Tile extends TileEntity
    {

    }
}
