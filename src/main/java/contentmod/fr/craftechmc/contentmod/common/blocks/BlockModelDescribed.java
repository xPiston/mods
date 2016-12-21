package fr.craftechmc.contentmod.common.blocks;

import fr.craftechmc.contentmod.common.objects.EOrientable;
import fr.craftechmc.contentmod.common.objects.ModelDescriptor;
import fr.craftechmc.contentmod.common.tiles.TileModel;
import fr.craftechmc.core.common.blocks.BlockTESRBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockModelDescribed extends BlockTESRBase
{
    private EOrientable orientable;

    public BlockModelDescribed(final Material material)
    {
        super(material);
        this.orientable = EOrientable.NONE;
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z,
            final EntityLivingBase entityliving, final ItemStack itemStack)
    {
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

            final TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileModel)
            {
                final TileModel model = (TileModel) tile;
                model.setOrientation(ForgeDirection.getOrientation(facing));
                world.markBlockForUpdate(x, y, z);
            }
        }
    }

    public AxisAlignedBB getBox(final World w, final int x, final int y, final int z)
    {
        final ModelDescriptor data = ModelDataManager.getInstance().getModelDatas().get(this.getUnlocalizedName());
        return AxisAlignedBB.getBoundingBox(x + data.getOffsetX(), y + data.getOffsetY(), z + data.getOffsetZ(),
                x + data.getWidth(), y + data.getHeight(), z + data.getLength());
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
    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_)
    {
        return new TileModel();
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