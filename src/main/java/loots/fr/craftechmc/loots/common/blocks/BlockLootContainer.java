package fr.craftechmc.loots.common.blocks;

import fr.craftechmc.contentmod.common.objects.EOrientable;
import fr.craftechmc.core.common.blocks.BlockTESRBase;
import fr.craftechmc.loots.common.CraftechLoots;
import fr.craftechmc.loots.common.tiles.TileLootContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLootContainer extends BlockTESRBase
{
    private EOrientable orientable;

    public BlockLootContainer(final Material mat)
    {
        super(mat);
        this.orientable = EOrientable.NONE;
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z,
            final EntityLivingBase entityliving, final ItemStack itemStack)
    {
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileLootContainer)
        {
            final TileLootContainer lootContainer = (TileLootContainer) tile;
            lootContainer.setSlotNumber(20);

            if (this.orientable != EOrientable.NONE)
            {
                int facing = MathHelper.floor_double(entityliving.rotationYaw * 4F / 360F + 0.5D) & 3;
                if (facing == 0)
                    facing = 2;
                else if (facing == 1)
                    facing = 3;
                else if (facing == 2)
                    facing = 4;
                else if (facing == 3)
                    facing = 5;
                lootContainer.setOrientation(ForgeDirection.getOrientation(facing));
            }
            world.markBlockForUpdate(x, y, z);
        }
    }

    public AxisAlignedBB getBox(final World w, final int x, final int y, final int z)
    {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World w, final int x, final int y, final int z)
    {
        return this.getBox(w, x, y, z);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World w, final int x, final int y, final int z)
    {
        return this.getBox(w, x, y, z);
    }

    @Override
    public boolean onBlockActivated(final World w, final int x, final int y, final int z, final EntityPlayer player,
            final int side, final float hitX, final float hitY, final float hitZ)
    {
        if (player.capabilities.isCreativeMode)
            player.openGui(CraftechLoots.instance, 0, w, x, y, z);
        else
        {
            final TileEntity tile = w.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileLootContainer && !w.isRemote)
                ((TileLootContainer) tile).openChest();
            player.openGui(CraftechLoots.instance, 1, w, x, y, z);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World w, final int meta)
    {
        return new TileLootContainer();
    }

    public EOrientable getOrientable()
    {
        return this.orientable;
    }

    public void setOrientable(final EOrientable orientable)
    {
        this.orientable = orientable;
    }
}