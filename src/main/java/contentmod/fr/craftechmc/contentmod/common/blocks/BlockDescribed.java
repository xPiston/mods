package fr.craftechmc.contentmod.common.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.contentmod.common.objects.BlockDescriptors.BasicBlockDescriptor;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDescribed extends Block
{
    private String[] names;
    private float[]  resistance;
    private float[]  hardness;
    private String[] textures;
    private IIcon[]  icons;
    private boolean  isOpaque;
    private int[]    lightning;

    public BlockDescribed(final Material mat)
    {
        super(mat);
        this.setCreativeTab(CTabManager.getTab("CONTENT_BLOCK"));
    }

    @Override
    public int getLightValue(final IBlockAccess w, final int x, final int y, final int z)
    {
        final int meta = w.getBlockMetadata(x, y, z);
        if (meta < this.lightning.length)
            return this.lightning[meta];
        return 0;
    }

    @Override
    public int damageDropped(final int meta)
    {
        return meta;
    }

    public void addSubBlocks(final BasicBlockDescriptor... descriptors)
    {
        this.names = new String[descriptors.length];
        this.resistance = new float[descriptors.length];
        this.hardness = new float[descriptors.length];
        this.textures = new String[descriptors.length];
        this.lightning = new int[descriptors.length];

        int i = 0;
        for (final BasicBlockDescriptor descriptor : descriptors)
        {
            this.names[i] = descriptor.getName();
            this.resistance[i] = descriptor.getBlockResistance();
            this.hardness[i] = descriptor.getBlockHardness();
            this.textures[i] = descriptor.getTexture();
            this.lightning[i] = descriptor.getLightning();
            i++;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube()
    {
        return this.isOpaque;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister register)
    {
        this.icons = new IIcon[this.textures.length];
        for (int i = 0; i < this.textures.length; i++)
            this.icons[i] = register.registerIcon("craftechcontent-block:" + this.textures[i]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta)
    {
        if (meta < this.textures.length)
            return this.icons[meta];
        return this.blockIcon;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List l)
    {
        for (int i = 0; i < this.names.length; i++)
            l.add(new ItemStack(item, 1, i));
    }

    @Override
    public float getPlayerRelativeBlockHardness(final EntityPlayer player, final World w, final int x, final int y,
            final int z)
    {
        if (w.getBlockMetadata(x, y, z) < this.hardness.length)
            return this.hardness[w.getBlockMetadata(x, y, z)];
        return 0;
    }

    @Override
    public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y,
            final int z, final double explosionX, final double explosionY, final double explosionZ)
    {
        return this.getResistancePerMetadata(world.getBlockMetadata(x, y, z)) / 5.0F;
    }

    public float getResistancePerMetadata(final int metadata)
    {
        if (metadata < this.resistance.length)
            return this.resistance[metadata] * 3.0F;
        return 0;
    }

    public String[] getNames()
    {
        return this.names;
    }

    public void setNames(final String[] names)
    {
        this.names = names;
    }

    public float[] getResistance()
    {
        return this.resistance;
    }

    public void setResistance(final float[] resistance)
    {
        this.resistance = resistance;
    }

    public float[] getHardness()
    {
        return this.hardness;
    }

    public void setHardness(final float[] hardness)
    {
        this.hardness = hardness;
    }

    public String[] getTextures()
    {
        return this.textures;
    }

    public void setTextures(final String[] textures)
    {
        this.textures = textures;
    }

    public IIcon[] getIcons()
    {
        return this.icons;
    }

    public void setIcons(final IIcon[] icons)
    {
        this.icons = icons;
    }

    public boolean isOpaque()
    {
        return this.isOpaque;
    }

    public void setOpaque(final boolean isOpaque)
    {
        this.isOpaque = isOpaque;
    }

    public int[] getLightning()
    {
        return this.lightning;
    }

    public void setLightning(final int[] lightning)
    {
        this.lightning = lightning;
    }
}