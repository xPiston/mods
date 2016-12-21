package fr.craftechmc.contentmod.client.render;

import java.util.concurrent.ExecutionException;

import org.lwjgl.opengl.GL11;

import fr.craftechmc.contentmod.common.multiblocks.BlockModelMultiBlockDescribed;
import fr.craftechmc.contentmod.common.multiblocks.TileModelMultiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class RenderModelMultiBlock extends TileEntitySpecialRenderer implements IItemRenderer
{
    private final ClientModelManager modelManager = ClientModelManager.getInstance();

    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float f)
    {
        this.renderModelMultiBlock(((TileModelMultiBlockCore) tile).getIndex(),
                x + ((TileModelMultiBlockCore) tile).getRenderOffset().xCoord,
                y + ((TileModelMultiBlockCore) tile).getRenderOffset().yCoord,
                z + ((TileModelMultiBlockCore) tile).getRenderOffset().zCoord, 1, tile.getBlockMetadata() == 0 ? 180
                        : tile.getBlockMetadata() == 1 ? 90 : tile.getBlockMetadata() == 2 ? 0 : -90);
    }

    public void renderModelMultiBlock(final String index, final double x, final double y, final double z,
            final float scale, final float rotate)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + .5, y, z + .5);
        GL11.glRotated(rotate, 0, 1, 0);
        GL11.glScalef(1 / scale, 1 / scale, 1 / scale);
        this.bindTexture(this.modelManager.getMbTextures().get(index));

        try
        {
            if (this.modelManager.getMultiblockModelCache().get(index) != null)
                this.modelManager.getMultiblockModelCache().get(index).renderAll();
        } catch (final ExecutionException e)
        {
            e.printStackTrace();
        }
        GL11.glPopMatrix();
    }

    public void renderInventory(final double x, final double y, final double z, final int meta, final String unloc,
            final ItemStack stack)
    {
        if (stack != null && stack.hasTagCompound())
            this.renderModelMultiBlock(stack.getTagCompound().getString("name"), x, y, z,
                    ((BlockModelMultiBlockDescribed) Block.getBlockFromItem(stack.getItem()))
                            .getScale(stack.getTagCompound().getString("name")),
                    0);
    }

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
            final ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data)
    {
        this.renderInventory(-0.5, -0.5, -0.5, item.getMetadata(), item.getUnlocalizedName(), item);
    }
}