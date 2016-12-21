package fr.craftechmc.loots.common.gui;

import fr.craftechmc.loots.common.tiles.TileLootContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Ourten 30 oct. 2016
 */
public class ServerGuiHandler
{
    public static Object getGui(final int ID, final EntityPlayer player, final World world, final int x, final int y,
            final int z)
    {
        switch (ID)
        {
            case 1:
                return new ContainerLootContainer(player.inventory, (TileLootContainer) world.getTileEntity(x, y, z));
            case 2:
                return new ContainerEditLootList(player.inventory);
            default:
                return null;
        }
    }
}