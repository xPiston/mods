package fr.craftechmc.admin.common.fogpath;

import fr.craftechmc.core.math.Vector3d;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * This is the fogpath editor item tool
 * Created by arisu on 24/07/2016.
 */
public class ItemEditorTool extends Item
{

    public ItemEditorTool()
    {
        super();
        setUnlocalizedName("FogPathEditorTool").setTextureName("craftechadmin:fogpath_tool")
                .setCreativeTab(CreativeTabs.tabTools);
    }

    public static Vector3d getFacedBlock(int x, int y, int z, int face)
    {

        if (face == 0)
            --y;

        if (face == 1)
            ++y;

        if (face == 2)
            --z;

        if (face == 3)
            ++z;

        if (face == 4)
            --x;

        if (face == 5)
            ++x;

        return new Vector3d(x, y, z);
    }
}
