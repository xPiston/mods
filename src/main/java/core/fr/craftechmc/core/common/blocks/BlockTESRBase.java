package fr.craftechmc.core.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.core.client.ClientProxy;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public abstract class BlockTESRBase extends BlockContainer
{
    public BlockTESRBase(final Material mat)
    {
        super(mat);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return ClientProxy.coreISBRHInventoryID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess w, final int x, final int y, final int z, final int side)
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}