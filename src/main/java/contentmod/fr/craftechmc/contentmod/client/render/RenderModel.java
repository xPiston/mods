package fr.craftechmc.contentmod.client.render;

import java.util.concurrent.ExecutionException;

import org.lwjgl.opengl.GL11;

import fr.craftechmc.contentmod.common.blocks.ModelDataManager;
import fr.craftechmc.contentmod.common.objects.ModelDescriptor;
import fr.craftechmc.contentmod.common.tiles.TileModel;
import fr.craftechmc.core.client.render.IInventoryRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderModel extends TileEntitySpecialRenderer implements IInventoryRenderer
{
    private static final ClientModelManager modelManager     = ClientModelManager.getInstance();
    private static final ModelDataManager   modelDataManager = ModelDataManager.getInstance();

    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z,
            final float destroy)
    {
        this.renderModel(x, y, z, tile.getBlockType().getUnlocalizedName(), ((TileModel) tile).getOrientation());
    }

    public void renderModel(final double x, final double y, final double z, final String index, final ForgeDirection or)
    {
        if (RenderModel.modelDataManager.getModelDatas().containsKey(index))
        {
            final ModelDescriptor data = RenderModel.modelDataManager.getModelDatas().get(index);
            GL11.glPushMatrix();
            GL11.glTranslated(x + .5 + data.getRenderOffsetX(), y + data.getRenderOffsetY(),
                    z + .5 + data.getRenderOffsetZ());
            GL11.glRotated(or == ForgeDirection.NORTH ? 180
                    : or == ForgeDirection.SOUTH ? 90 : or == ForgeDirection.EAST ? -90 : 0, 0, 1, 0);
            GL11.glScalef(1 / data.getScale(), 1 / data.getScale(), 1 / data.getScale());
            this.bindTexture(RenderModel.modelManager.getBasicTextures().get(index));

            try
            {
                if (RenderModel.modelManager.getBlockModelCache().get(index) != null)
                    RenderModel.modelManager.getBlockModelCache().get(index).renderAll();
            } catch (final ExecutionException e)
            {
                e.printStackTrace();
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderInventory(final double x, final double y, final double z, final int meta, final String unloc,
            final ItemStack stack)
    {
        this.renderModel(x, y, z, unloc, ForgeDirection.UNKNOWN);
    }
}