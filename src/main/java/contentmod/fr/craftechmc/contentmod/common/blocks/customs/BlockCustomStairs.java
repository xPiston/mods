package fr.craftechmc.contentmod.common.blocks.customs;

import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.BlockStairs;

/**
 *
 * @author Ourten
 *
 */
public class BlockCustomStairs extends BlockStairs
{
    public BlockCustomStairs(final String name, final BlockObject block)
    {
        super(block.getBlock(), block.getMeta());
        this.setUnlocalizedName(name);
        this.setLightOpacity(0);
        this.setCreativeTab(CTabManager.getTab("CONTENT_DECLINAISON"));
    }
}