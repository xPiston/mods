package fr.craftechmc.contentmod.common.blocks.customs;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A custom door declination Created by Thog the 10/07/2016
 */
public class BlockCustomDoor extends BlockDoor
{
    private final List<String> textures;
    private final BlockObject  model;
    private IIcon[]            upperSide;
    private IIcon[]            lowerSide;
    private Item               drop;
    private final boolean      isPressureActivate;

    public BlockCustomDoor(final String name, final List<String> textures, final BlockObject model,
            final boolean isPressureActivate)
    {
        super(model.getBlock().getMaterial());

        // FIXME: Remove this, this isn't supposed to happened!
        if (textures == null)
            this.textures = Arrays.asList("door_wood_upper", "door_wood_lower", "door_wood");
        else
            this.textures = textures;
        this.textureName = this.textures.get(2);
        this.setUnlocalizedName(name);
        this.model = model;
        this.isPressureActivate = isPressureActivate;
        this.setStepSound(this.model.getBlock().stepSound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return this.model.getBlock().getRenderBlockPass();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(final IIconRegister registry)
    {
        this.upperSide = new IIcon[2];
        this.lowerSide = new IIcon[2];

        this.upperSide[0] = registry.registerIcon(this.textures.get(0));
        this.upperSide[1] = new IconFlipped(this.upperSide[0], true, false);
        this.lowerSide[0] = registry.registerIcon(this.textures.get(1));
        this.lowerSide[1] = new IconFlipped(this.lowerSide[0], true, false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(final int p_149691_1_, final int p_149691_2_)
    {
        return this.lowerSide[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess world, final int x, final int y, final int z, final int side)
    {
        if (side != 1 && side != 0)
        {
            final int i1 = this.getFullMetadata(world, x, y, z);
            final int j1 = i1 & 3;
            final boolean flag = (i1 & 4) != 0;
            boolean isFlipped = false;
            final boolean isUpperSide = (i1 & 8) != 0;

            if (flag)
            {
                if (j1 == 0 && side == 2 || j1 == 1 && side == 5 || j1 == 2 && side == 3 || j1 == 3 && side == 4)
                    isFlipped = true;
            }
            else
            {
                if (j1 == 0 && side == 5 || j1 == 1 && side == 3 || j1 == 2 && side == 4 || j1 == 3 && side == 2)
                    isFlipped = true;
                if ((i1 & 16) != 0)
                    isFlipped = !isFlipped;
            }

            return isUpperSide ? this.upperSide[isFlipped ? 1 : 0] : this.lowerSide[isFlipped ? 1 : 0];
        }
        else
            return this.lowerSide[0];
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int side, final float hitX, final float hitY, final float hitZ)
    {
        if (this.isPressureActivate)
            return false;
        else
        {
            final int i1 = this.getFullMetadata(world, x, y, z);
            int j1 = i1 & 7;
            j1 ^= 4;

            if ((i1 & 8) == 0)
            {
                world.setBlockMetadataWithNotify(x, y, z, j1, 2);
                world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            }
            else
            {
                world.setBlockMetadataWithNotify(x, y - 1, z, j1, 2);
                world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
            }

            world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
            return true;
        }
    }

    public Item setDrop(final Item drop)
    {
        this.drop = drop;
        return drop;
    }

    @Override
    public Item getItemDropped(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_)
    {
        return this.drop;
    }

    @Override
    public Item getItem(final World world, final int x, final int y, final int z)
    {
        return this.drop;
    }

    @Override
    public String getTextureName()
    {
        return super.getTextureName();
    }
}
