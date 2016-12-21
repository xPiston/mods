package fr.craftechmc.contentmod.common.multiblocks;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.contentmod.common.objects.BlockDescriptors.BasicMultiBlockDescriptor;
import fr.craftechmc.core.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockModelMultiBlockDescribed extends BlockContainer
{
    private HashMap<String, BasicMultiBlockDescriptor> models  = new HashMap<>();
    private final int                                  gagMeta = 4;

    /**
     *
     * @param material
     * @param width
     * @param height
     * @param length
     * @param offsetX
     * @param offsetY
     * @param offsetZ
     */
    public BlockModelMultiBlockDescribed(final String name, final Material material)
    {
        super(material);
        this.setUnlocalizedName(name);
    }

    @Override
    public int getLightValue(final IBlockAccess w, final int x, final int y, final int z)
    {
        IMultiBlockTile tile = (IMultiBlockTile) w.getTileEntity(x, y, z);

        if (tile != null)
        {
            if (!tile.isCore())
                tile = ((IMultiBlockTileGag) tile).getCore();
            if (tile != null && this.models.get(((TileModelMultiBlockCore) tile).getIndex()) != null)
                return this.models.get(((TileModelMultiBlockCore) tile).getIndex()).getLightning();
        }
        return 0;
    }

    @Override
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y,
            final int z, final EntityPlayer player)
    {
        final ItemStack stack = new ItemStack(this, 1, 0);
        IMultiBlockTile tile = (IMultiBlockTile) world.getTileEntity(x, y, z);

        if (tile != null)
        {
            if (!tile.isCore())
                tile = ((IMultiBlockTileGag) tile).getCore();
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setString("name", ((TileModelMultiBlockCore) tile).getIndex());
            stack.setTagCompound(tag);
        }
        return stack;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List l)
    {
        for (final String s : this.models.keySet())
        {
            final ItemStack stack = new ItemStack(this, 1, 0);
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setString("name", s);
            stack.setTagCompound(tag);
            l.add(stack);
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World w, final int x, final int y, final int z)
    {
        final TileEntity tile = w.getTileEntity(x, y, z);

        if (tile != null && tile instanceof IMultiBlockTile && ((IMultiBlockTile) tile).needSelectionHandling())
            return ((IMultiBlockTile) tile).getSelectionBox();
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    @Override
    public void onNeighborBlockChange(final World w, final int x, final int y, final int z, final Block b)
    {
        final IMultiBlockTile tile = (IMultiBlockTile) w.getTileEntity(x, y, z);

        if (tile != null)
            if (!tile.isCore())
                if (!((IMultiBlockTileGag) tile).isCorePresent())
                    w.setBlockToAir(x, y, z);

        super.onNeighborBlockChange(w, x, y, z, b);
    }

    @Override
    public void breakBlock(final World w, final int x, final int y, final int z, final Block b, final int meta)
    {
        final IMultiBlockTile tile = (IMultiBlockTile) w.getTileEntity(x, y, z);

        if (tile != null)
            if (!tile.isCore())
                if (((IMultiBlockTileGag) tile).isCorePresent())
                    ((IMultiBlockTileGag) tile).destroyCore();
    }

    public boolean placeGag(final World w, final int x, final int y, final int z, final String index,
            final boolean perp)
    {
        final IMultiBlockTile core = (IMultiBlockTile) w.getTileEntity(x, y, z);
        boolean success = true;

        if (core != null)
        {
            final Vec3[] blocks = new Vec3[(this.models.get(index).getWidthBlocks()
                    * this.models.get(index).getHeightBlocks() * this.models.get(index).getLengthBlocks()) + 1];

            if (!perp)
                for (int i = 0; i < this.models.get(index).getWidth(); i++)
                {
                    for (int j = 0; j < this.models.get(index).getHeight(); j++)
                    {
                        for (int k = 0; k < this.models.get(index).getLength(); k++)
                        {
                            if ((x + i + this.models.get(index).getOffsetX()) != x
                                    || (y + j + this.models.get(index).getOffsetY()) != y
                                    || (z + k + this.models.get(index).getOffsetZ()) != z)
                            {
                                if (!w.isAirBlock(x + i + (int) this.models.get(index).getOffsetX(),
                                        y + j + (int) this.models.get(index).getOffsetY(),
                                        z + k + (int) this.models.get(index).getOffsetZ()))
                                    success = false;

                                if (success)
                                {
                                    final boolean b = w.setBlock(x + i + (int) this.models.get(index).getOffsetX(),
                                            y + j + (int) this.models.get(index).getOffsetY(),
                                            z + k + (int) this.models.get(index).getOffsetZ(),
                                            w.getTileEntity(x, y, z).getBlockType(), 4, 3);
                                    blocks[(i + 1) * (j + 1) * (k + 1)] = Vec3.createVectorHelper(
                                            x + i + (int) this.models.get(index).getOffsetX(),
                                            y + j + (int) this.models.get(index).getOffsetY(),
                                            z + k + (int) this.models.get(index).getOffsetZ());
                                    if (!b)
                                        success = false;

                                    final IMultiBlockTileGag gag = (IMultiBlockTileGag) w.getTileEntity(
                                            x + i + (int) this.models.get(index).getOffsetX(),
                                            y + j + (int) this.models.get(index).getOffsetY(),
                                            z + k + (int) this.models.get(index).getOffsetZ());

                                    if (gag != null)
                                    {
                                        gag.setUnlocalizedCoreName(
                                                w.getTileEntity(x, y, z).getBlockType().getUnlocalizedName());
                                        gag.setxCore(x);
                                        gag.setyCore(y);
                                        gag.setzCore(z);
                                    }
                                    else
                                        success = false;
                                }
                            }

                            if (!success)
                            {
                                for (final Vec3 vec : blocks)
                                    if (vec != null)
                                    {
                                        w.breakBlock((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord, false);
                                        w.removeTileEntity((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
                                    }
                                w.breakBlock(x, y, z, false);
                                w.removeTileEntity(x, y, z);
                                break;
                            }
                        }
                        if (!success)
                            break;
                    }
                    if (!success)
                        break;
                }
            else
                for (int i = 0; i < this.models.get(index).getLength(); i++)
                {
                    for (int j = 0; j < this.models.get(index).getHeight(); j++)
                    {
                        for (int k = 0; k < this.models.get(index).getWidth(); k++)
                        {
                            if ((x + i + this.models.get(index).getOffsetZ()) != x
                                    || (y + j + this.models.get(index).getOffsetY()) != y
                                    || (z + k + this.models.get(index).getOffsetX()) != z)
                            {
                                if (!w.isAirBlock(x + i + (int) this.models.get(index).getOffsetZ(),
                                        y + j + (int) this.models.get(index).getOffsetY(),
                                        z + k + (int) this.models.get(index).getOffsetX()))
                                    success = false;

                                if (success)
                                {
                                    final boolean b = w.setBlock(x + i + (int) this.models.get(index).getOffsetZ(),
                                            y + j + (int) this.models.get(index).getOffsetY(),
                                            z + k + (int) this.models.get(index).getOffsetX(),
                                            w.getTileEntity(x, y, z).getBlockType(), 4, 3);
                                    blocks[(i + 1) * (j + 1) * (k + 1)] = Vec3.createVectorHelper(
                                            x + i + this.models.get(index).getOffsetZ(),
                                            y + j + this.models.get(index).getOffsetY(),
                                            z + k + this.models.get(index).getOffsetX());
                                    if (!b)
                                        success = false;

                                    final IMultiBlockTileGag gag = (IMultiBlockTileGag) w.getTileEntity(
                                            x + i + (int) this.models.get(index).getOffsetZ(),
                                            y + j + (int) this.models.get(index).getOffsetY(),
                                            z + k + (int) this.models.get(index).getOffsetX());

                                    if (gag != null)
                                    {
                                        gag.setUnlocalizedCoreName(
                                                w.getTileEntity(x, y, z).getBlockType().getUnlocalizedName());
                                        gag.setxCore(x);
                                        gag.setyCore(y);
                                        gag.setzCore(z);
                                    }
                                    else
                                        success = false;
                                }
                            }

                            if (!success)
                            {
                                for (final Vec3 vec : blocks)
                                    if (vec != null)
                                    {
                                        w.breakBlock((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord, false);
                                        w.removeTileEntity((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
                                    }
                                w.breakBlock(x, y, z, false);
                                w.removeTileEntity(x, y, z);
                                break;
                            }
                        }
                        if (!success)
                            break;
                    }
                    if (!success)
                        break;
                }
        }
        return success;
    }

    public boolean canPlaceGag(final World w, final int x, final int y, final int z, final String index)
    {
        for (int i = 0; i < this.models.get(index).getWidth(); i++)
            for (int j = 0; j < this.models.get(index).getHeight(); j++)
                for (int k = 0; k < this.models.get(index).getLength(); k++)
                    if ((x + i + this.models.get(index).getOffsetX()) != x
                            || (y + j + this.models.get(index).getOffsetY()) != y
                            || (z + k + this.models.get(index).getOffsetZ()) != z)
                        if (!w.isAirBlock(x + i + (int) this.models.get(index).getOffsetX(),
                                y + j + (int) this.models.get(index).getOffsetY(),
                                z + k + (int) this.models.get(index).getOffsetZ()))
                            return false;
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World w, final int meta)
    {
        if (meta != this.gagMeta)
            return this.getNewTile(w, meta);
        return new TileModelMultiBlockGag();
    }

    public TileEntity getNewTile(final World w, final int meta)
    {
        return new TileModelMultiBlockCore();
    }

    //////////////////////////////////////
    // Global Render //
    //////////////////////////////////////

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

    public HashMap<String, BasicMultiBlockDescriptor> getModels()
    {
        return this.models;
    }

    public void setModels(final HashMap<String, BasicMultiBlockDescriptor> models)
    {
        this.models = models;
    }

    public void addModel(final BasicMultiBlockDescriptor descriptor)
    {
        this.models.put(descriptor.getName(), descriptor);
    }

    public float getScale(final String index)
    {
        if (this.models.containsKey(index))
        {
            final BasicMultiBlockDescriptor model = this.models.get(index);
            return Math.max(model.getLength(), Math.max(model.getWidth(), model.getHeight()));
        }
        return 1;
    }
}