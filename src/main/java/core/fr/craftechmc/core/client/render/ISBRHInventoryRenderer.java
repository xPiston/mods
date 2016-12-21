package fr.craftechmc.core.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import fr.craftechmc.core.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;

public class ISBRHInventoryRenderer implements ISimpleBlockRenderingHandler
{
    public static HashMap<TESRIndex, IInventoryRenderer> blockByTESR = new HashMap<>();

    @Override
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID,
            final RenderBlocks renderer)
    {
        final TESRIndex index = new TESRIndex(block, metadata, false);
        if (ISBRHInventoryRenderer.blockByTESR.containsKey(index))
            ISBRHInventoryRenderer.blockByTESR.get(index).renderInventory(-0.5, -0.5, -0.5, metadata,
                    block.getUnlocalizedName(), null);
    }

    @Override
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block,
            final int modelId, final RenderBlocks renderer)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.coreISBRHInventoryID;
    }

    @Override
    public boolean shouldRender3DInInventory(final int modelId)
    {
        return true;
    }
}