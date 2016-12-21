package fr.craftechmc.loots.common.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CraftechLootsGuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z)
    {
        return ServerGuiHandler.getGui(ID, player, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z)
    {
        return ClientGuiHandler.getGui(ID, player, world, x, y, z);
    }
}