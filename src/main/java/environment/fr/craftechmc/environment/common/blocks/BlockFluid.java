package fr.craftechmc.environment.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

/**
 * Created by arisu on 03/08/2016.
 */
public class BlockFluid extends BlockFluidClassic
{
    @SideOnly(Side.CLIENT)
    protected IIcon      stillIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon      flowingIcon;

    private final String texturePrefix;

    // BlockDynamicLiquid Fields
    private int          adjacentSourceBlocks;
    boolean[]            field_149814_b = new boolean[4];
    int[]                field_149816_M = new int[4];

    public BlockFluid(final Fluid fluid, final Material material, final String texturePrefix)
    {
        super(fluid, material);
        this.texturePrefix = texturePrefix;
        this.setFluidStackAmount(FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public IIcon getIcon(final int side, final int meta)
    {
        return (side == 0 || side == 1) ? this.stillIcon : this.flowingIcon;
    }

    @Override
    public void registerIcons(final IIconRegister register)
    {
        this.stillIcon = register.registerIcon("craftechenvironment:" + this.texturePrefix + "_still");
        this.flowingIcon = register.registerIcon("craftechenvironment:" + this.texturePrefix + "_flow");
    }

    @Override
    public boolean canDisplace(final IBlockAccess world, final int x, final int y, final int z)
    {
        if (world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(final World world, final int x, final int y, final int z)
    {
        if (world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.displaceIfPossible(world, x, y, z);
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied
     * against the blocks color. Note only called when first determining what to
     * render.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(final IBlockAccess blockAccess, final int x, final int y, final int z)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int zOffset = -1; zOffset <= 1; ++zOffset)
            for (int xOffset = -1; xOffset <= 1; ++xOffset)
            {
                final int i2 = blockAccess.getBiomeGenForCoords(x + xOffset, z + zOffset).getWaterColorMultiplier();
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand)
    {
        int metadataOrZero = this.getBlockMetadataOrZero(world, x, y, z);
        final byte flowLevelOffset = 1;

        final int tickRate = this.tickRate(world);
        int metadataBasedFlowLevel;

        if (metadataOrZero > 0)
        {
            final byte b1 = -100;
            this.adjacentSourceBlocks = 0;
            int metadataSum = this.metadataMin(world, x - 1, y, z, b1);
            metadataSum = this.metadataMin(world, x + 1, y, z, metadataSum);
            metadataSum = this.metadataMin(world, x, y, z - 1, metadataSum);
            metadataSum = this.metadataMin(world, x, y, z + 1, metadataSum);
            metadataBasedFlowLevel = metadataSum + flowLevelOffset;

            if (metadataBasedFlowLevel >= 8 || metadataSum < 0)
                metadataBasedFlowLevel = -1;

            if (this.getBlockMetadataOrZero(world, x, y + 1, z) >= 0)
            {
                final int metadata = this.getBlockMetadataOrZero(world, x, y + 1, z);

                if (metadata >= 8)
                    metadataBasedFlowLevel = metadata;
                else
                    metadataBasedFlowLevel = metadata + 8;
            }

            if (this.adjacentSourceBlocks >= 2)
                if (world.getBlock(x, y - 1, z).getMaterial().isSolid())
                    metadataBasedFlowLevel = 0;
                else if (world.getBlock(x, y - 1, z).getMaterial() == this.blockMaterial
                        && world.getBlockMetadata(x, y - 1, z) == 0)
                    metadataBasedFlowLevel = 0;

            if (metadataBasedFlowLevel == metadataOrZero)
                this.shiftBlockId(world, x, y, z);
            else
            {
                metadataOrZero = metadataBasedFlowLevel;

                if (metadataBasedFlowLevel < 0)
                    world.setBlockToAir(x, y, z);
                else
                {
                    world.setBlockMetadataWithNotify(x, y, z, metadataBasedFlowLevel, 2);
                    world.scheduleBlockUpdate(x, y, z, this, tickRate);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                }
            }
        }
        else
            this.shiftBlockId(world, x, y, z);

        if (this.notSameMaterialAndCanPassThrough(world, x, y - 1, z))
        {
            if (metadataOrZero >= 8)
                this.bulldozeFluidWayIfStrongEnough(world, x, y - 1, z, metadataOrZero);
            else
                this.bulldozeFluidWayIfStrongEnough(world, x, y - 1, z, metadataOrZero + 8);
        }
        else if (metadataOrZero >= 0 && (metadataOrZero == 0 || this.cantPassThrough(world, x, y - 1, z)))
        {
            final boolean[] aboolean = this.func_149808_o(world, x, y, z);
            metadataBasedFlowLevel = metadataOrZero + flowLevelOffset;

            if (metadataOrZero >= 8)
                metadataBasedFlowLevel = 1;

            if (metadataBasedFlowLevel >= 8)
                return;

            if (aboolean[0])
                this.bulldozeFluidWayIfStrongEnough(world, x - 1, y, z, metadataBasedFlowLevel);

            if (aboolean[1])
                this.bulldozeFluidWayIfStrongEnough(world, x + 1, y, z, metadataBasedFlowLevel);

            if (aboolean[2])
                this.bulldozeFluidWayIfStrongEnough(world, x, y, z - 1, metadataBasedFlowLevel);

            if (aboolean[3])
                this.bulldozeFluidWayIfStrongEnough(world, x, y, z + 1, metadataBasedFlowLevel);
        }
    }

    protected int getBlockMetadataOrZero(final World world, final int x, final int y, final int z)
    {
        return world.getBlock(x, y, z).getMaterial() == this.blockMaterial ? world.getBlockMetadata(x, y, z) : -1;
    }

    protected int metadataMin(final World world, final int x, final int y, final int z, final int metadataToCompare)
    {
        int metadata = this.getBlockMetadataOrZero(world, x, y, z);

        if (metadata < 0)
            return metadataToCompare;
        else
        {
            if (metadata == 0)
                ++this.adjacentSourceBlocks;

            if (metadata >= 8)
                metadata = 0;

            return metadataToCompare >= 0 && metadata >= metadataToCompare ? metadataToCompare : metadata;
        }
    }

    private void shiftBlockId(final World world, final int x, final int y, final int z)
    {
        final int metadata = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z,
                Block.getBlockById(Block.getIdFromBlock(this) /* + 1 */), metadata, 2);
    }

    private boolean notSameMaterialAndCanPassThrough(final World world, final int x, final int y, final int z)
    {
        final Material material = world.getBlock(x, y, z).getMaterial();
        return material != this.blockMaterial && !this.cantPassThrough(world, x, y, z);
    }

    private void bulldozeFluidWayIfStrongEnough(final World world, final int x, final int y, final int z,
            final int metadata)
    {
        if (this.notSameMaterialAndCanPassThrough(world, x, y, z))
        {
            final Block block = world.getBlock(x, y, z);

            block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);

            world.setBlock(x, y, z, this, metadata, 3);
        }
    }

    private boolean cantPassThrough(final World world, final int x, final int y, final int z)
    {
        final Block block = world.getBlock(x, y, z);
        return !(block != Blocks.wooden_door && block != Blocks.iron_door && block != Blocks.standing_sign
                && block != Blocks.ladder && block != Blocks.reeds)
                || (block.getMaterial() == Material.portal || block.getMaterial().blocksMovement());
    }

    private boolean[] func_149808_o(final World world, final int x, final int y, final int z)
    {
        int i;
        int x1;

        for (i = 0; i < 4; ++i)
        {
            this.field_149816_M[i] = 1000;
            x1 = x;
            int z1 = z;

            if (i == 0)
                x1 = x - 1;

            if (i == 1)
                ++x1;

            if (i == 2)
                z1 = z - 1;

            if (i == 3)
                ++z1;

            if (!this.cantPassThrough(world, x1, y, z1)
                    && (world.getBlock(x1, y, z1).getMaterial() != this.blockMaterial
                            || world.getBlockMetadata(x1, y, z1) != 0))
                if (this.cantPassThrough(world, x1, y - 1, z1))
                    this.field_149816_M[i] = this.func_149812_c(world, x1, y, z1, 1, i);
                else
                    this.field_149816_M[i] = 0;
        }

        i = this.field_149816_M[0];

        for (x1 = 1; x1 < 4; ++x1)
            if (this.field_149816_M[x1] < i)
                i = this.field_149816_M[x1];

        for (x1 = 0; x1 < 4; ++x1)
            this.field_149814_b[x1] = this.field_149816_M[x1] == i;

        return this.field_149814_b;
    }

    private int func_149812_c(final World world, final int x, final int y, final int z, final int metadata,
            final int p_149812_6_)
    {
        int j1 = 1000;

        for (int i = 0; i < 4; ++i)
            if ((i != 0 || p_149812_6_ != 1) && (i != 1 || p_149812_6_ != 0) && (i != 2 || p_149812_6_ != 3)
                    && (i != 3 || p_149812_6_ != 2))
            {
                int x1 = x;
                int z1 = z;

                if (i == 0)
                    x1 = x - 1;

                if (i == 1)
                    ++x1;

                if (i == 2)
                    z1 = z - 1;

                if (i == 3)
                    ++z1;

                if (!this.cantPassThrough(world, x1, y, z1)
                        && (world.getBlock(x1, y, z1).getMaterial() != this.blockMaterial
                                || world.getBlockMetadata(x1, y, z1) != 0))
                {
                    if (!this.cantPassThrough(world, x1, y - 1, z1))
                        return metadata;

                    if (metadata < 4)
                    {
                        final int j2 = this.func_149812_c(world, x1, y, z1, metadata + 1, i);

                        if (j2 < j1)
                            j1 = j2;
                    }
                }
            }

        return j1;
    }
}
