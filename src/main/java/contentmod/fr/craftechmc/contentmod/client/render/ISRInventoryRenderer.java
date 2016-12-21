package fr.craftechmc.contentmod.client.render;

import java.util.HashMap;

import fr.craftechmc.core.client.render.IInventoryRenderer;
import fr.craftechmc.core.client.render.TESRIndex;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ISRInventoryRenderer implements IItemRenderer
{
    public static HashMap<TESRIndex, IInventoryRenderer> blockByTESR = new HashMap<>();

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type)
    {
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
            final ItemRendererHelper helper)
    {
        return false;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data)
    {
        final TESRIndex index = new TESRIndex(Block.getBlockFromItem(item.getItem()), item.getMetadata(), false);
        System.out.println("TT");
        if (ISRInventoryRenderer.blockByTESR.containsKey(index))
            ISRInventoryRenderer.blockByTESR.get(index).renderInventory(-0.5, -0.5, -0.5, item.getMetadata(),
                    item.getUnlocalizedName(), item);
    }
}