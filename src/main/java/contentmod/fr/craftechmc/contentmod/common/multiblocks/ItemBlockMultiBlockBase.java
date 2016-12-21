package fr.craftechmc.contentmod.common.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBlockMultiBlockBase extends ItemBlock
{
    public ItemBlockMultiBlockBase(final Block block)
    {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack)
    {
        if (stack != null && stack.hasTagCompound())
            return stack.getTagCompound().getString("name");
        return "ERROR";
    }

    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer p_77648_2_, final World w, int x, int y, int z,
            int p_77648_7_, final float p_77648_8_, final float p_77648_9_, final float p_77648_10_)
    {
        final Block block = w.getBlock(x, y, z);

        if (block == Blocks.snow_layer && (w.getBlockMetadata(x, y, z) & 7) < 1)
            p_77648_7_ = 1;
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush
                && !block.isReplaceable(w, x, y, z))
        {
            if (p_77648_7_ == 0)
                --y;
            if (p_77648_7_ == 1)
                ++y;
            if (p_77648_7_ == 2)
                --z;
            if (p_77648_7_ == 3)
                ++z;
            if (p_77648_7_ == 4)
                --x;
            if (p_77648_7_ == 5)
                ++x;
        }

        if (stack.stackSize == 0)
            return false;
        else if (!p_77648_2_.canPlayerEdit(x, y, z, p_77648_7_, stack))
            return false;
        else if (y == 255 && this.blockInstance.getMaterial().isSolid())
            return false;
        else if (w.canPlaceEntityOnSide(this.blockInstance, x, y, z, false, p_77648_7_, p_77648_2_, stack))
        {
            if (this.canPlaceBlockAt(w, x, y, z, stack.getTagCompound().getString("name")))
            {
                final int i1 = this.getMetadata(stack.getMetadata());
                final int j1 = this.blockInstance.onBlockPlaced(w, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_,
                        p_77648_10_, i1);

                if (this.placeBlockAt(stack, p_77648_2_, w, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_,
                        j1))
                {
                    w.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, this.blockInstance.stepSound.soundName,
                            (this.blockInstance.stepSound.getVolume() + 1.0F) / 2.0F,
                            this.blockInstance.stepSound.getFrequency() * 0.8F);
                    --stack.stackSize;
                }
                return true;
            }
            return false;
        }
        else
            return false;
    }

    @Override
    public int getMetadata(final int damageValue)
    {
        return damageValue;
    }

    @Override
    public boolean getShareTag()
    {
        return true;
    }

    public boolean canPlaceBlockAt(final World w, final int x, final int y, final int z, final String index)
    {
        return ((BlockModelMultiBlockDescribed) this.blockInstance).canPlaceGag(w, x, y, z, index);
    }

    @Override
    public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World w, final int x,
            final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ,
            final int meta)
    {
        final int facing = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;

        if (!w.setBlock(x, y, z, this.blockInstance, facing, 3)
                || !((BlockModelMultiBlockDescribed) this.blockInstance).placeGag(w, x, y, z,
                        stack.getTagCompound().getString("name"), (facing == 0 || facing == 2 ? true : false)))
            return false;

        if (w.getBlock(x, y, z) == this.blockInstance)
        {
            this.blockInstance.onBlockPlacedBy(w, x, y, z, player, stack);
            this.blockInstance.onPostBlockPlaced(w, x, y, z, meta);
        }

        System.out.println(stack.getTagCompound().getString("name"));
        ((TileModelMultiBlockCore) w.getTileEntity(x, y, z)).setIndex(stack.getTagCompound().getString("name"));
        return true;
    }
}