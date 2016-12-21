package fr.craftechmc.contentmod.common.blocks.customs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCustomFenceGate extends BlockFenceGate
{
    private final BlockObject block;

    public BlockCustomFenceGate(final String name, final BlockObject block)
    {
        this.block = block;
        this.setUnlocalizedName(name);
        this.setCreativeTab(CTabManager.getTab("CONTENT_DECLINAISON"));
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int p_149691_1_, final int p_149691_2_)
    {
        return this.block.getBlock().getBlockTextureFromSide(p_149691_1_);
    }

    /**
     * Checks to see if its valid to put this block at the specified
     * coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(final World p_149742_1_, final int p_149742_2_, final int p_149742_3_,
            final int p_149742_4_)
    {
        return !p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_).getMaterial().isSolid() ? false
                : super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this
     * box can change after the pool has been cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World w, final int x, final int y, final int z)
    {
        final int l = w.getBlockMetadata(x, y, z);
        return BlockCustomFenceGate.isFenceGateOpen(l) ? null
                : l != 2 && l != 0 ? AxisAlignedBB.getBoundingBox(x + 0.375F, y, z, x + 0.625F, y + 1.5F, z + 1)
                        : AxisAlignedBB.getBoundingBox(x, y, z + 0.375F, x + 1, y + 1.5F, z + 0.625F);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess p_149719_1_, final int p_149719_2_, final int p_149719_3_,
            final int p_149719_4_)
    {
        final int l = BlockDirectional
                .getDirection(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));

        if (l != 2 && l != 0)
            this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        else
            this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isPassable(final IBlockAccess world, final int x, final int y, final int z)
    {
        /**
         * Returns if the fence gate is open according to its metadata.
         */
        return BlockCustomFenceGate.isFenceGateOpen(world.getBlockMetadata(x, y, z));
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 21;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(final World p_149689_1_, final int p_149689_2_, final int p_149689_3_,
            final int p_149689_4_, final EntityLivingBase p_149689_5_, final ItemStack p_149689_6_)
    {
        final int l = (MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(final World p_149727_1_, final int p_149727_2_, final int p_149727_3_,
            final int p_149727_4_, final EntityPlayer p_149727_5_, final int p_149727_6_, final float p_149727_7_,
            final float p_149727_8_, final float p_149727_9_)
    {
        int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);

        if (BlockCustomFenceGate.isFenceGateOpen(i1))
            p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, i1 & -5, 2);
        else
        {
            final int j1 = (MathHelper.floor_double(p_149727_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
            final int k1 = BlockDirectional.getDirection(i1);

            if (k1 == (j1 + 2) % 4)
                i1 = j1;

            p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, i1 | 4, 2);
        }

        p_149727_1_.playAuxSFXAtEntity(p_149727_5_, 1003, p_149727_2_, p_149727_3_, p_149727_4_, 0);
        return true;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor Block
     */
    @Override
    public void onNeighborBlockChange(final World p_149695_1_, final int p_149695_2_, final int p_149695_3_,
            final int p_149695_4_, final Block p_149695_5_)
    {
        if (!p_149695_1_.isRemote)
        {
            final int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
            final boolean flag = p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_);

            if (flag || p_149695_5_.canProvidePower())
                if (flag && !BlockCustomFenceGate.isFenceGateOpen(l))
                {
                    p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, l | 4, 2);
                    p_149695_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003, p_149695_2_, p_149695_3_, p_149695_4_, 0);
                }
                else if (!flag && BlockCustomFenceGate.isFenceGateOpen(l))
                {
                    p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, l & -5, 2);
                    p_149695_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003, p_149695_2_, p_149695_3_, p_149695_4_, 0);
                }
        }
    }

    /**
     * Returns if the fence gate is open according to its metadata.
     */
    public static boolean isFenceGateOpen(final int p_149896_0_)
    {
        return (p_149896_0_ & 4) != 0;
    }

    /**
     * Returns true if the given side of this block type should be rendered, if
     * the adjacent block is at the given coordinates. Args: blockAccess, x, y,
     * z, side
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess p_149646_1_, final int p_149646_2_, final int p_149646_3_,
            final int p_149646_4_, final int p_149646_5_)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister p_149651_1_)
    {
    }
}