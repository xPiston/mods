package fr.craftechmc.loots.common.gui;

import org.yggard.brokkgui.wrapper.BrokkGuiManager;

import fr.craftechmc.loots.client.gui.GuiEditLootContainer;
import fr.craftechmc.loots.client.gui.GuiLootContainer;
import fr.craftechmc.loots.common.tiles.TileLootContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Ourten 30 oct. 2016
 */
public class ClientGuiHandler
{
    public static Object getGui(final int ID, final EntityPlayer player, final World world, final int x, final int y,
            final int z)
    {
        switch (ID)
        {
            case 0:
                return BrokkGuiManager
                        .getBrokkGuiScreen(new GuiEditLootContainer((TileLootContainer) world.getTileEntity(x, y, z)));
            case 1:
                return new GuiLootContainer(
                        new ContainerLootContainer(player.inventory, (TileLootContainer) world.getTileEntity(x, y, z)));
            default:
                return null;
        }
    }
}