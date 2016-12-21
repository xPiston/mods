package fr.craftechmc.craft.network;

import cpw.mods.fml.common.network.IGuiHandler;
import fr.craftechmc.craft.CommonProxy;
import fr.craftechmc.craft.api.IWorkbenchEntry;
import fr.craftechmc.craft.api.WorkbenchRegistry;
import fr.craftechmc.craft.block.BlockWorkbench;
import fr.craftechmc.craft.client.GuiWorkbench;
import fr.craftechmc.craft.container.WorkbenchContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Desc...
 * Created by Thog the 16/04/2016
 */
public class GuiHandler implements IGuiHandler
{
    private final CommonProxy proxy;
    private       Object      data;
    private       byte        flag;

    public GuiHandler(CommonProxy proxy)
    {
        this.proxy = proxy;
    }

    @Override public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof BlockWorkbench.Tile)
        {
            List<IWorkbenchEntry.Render> renders = WorkbenchRegistry.buildAvailableList(player);
            if (!renders.isEmpty())
            {
                PacketWorkbench workbench = new PacketWorkbench(renders);
                proxy.pipeline().sendTo(workbench, (EntityPlayerMP) player);
            }
            return new WorkbenchContainer(player, world, x, y, z);
        }
        return null;
    }

    @Override public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof BlockWorkbench.Tile)
            return new GuiWorkbench(this, player, world, x, y, z);
        return null;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data, int i)
    {
        this.data = data;
        setFlag((byte) i);
    }

    public byte getFlag()
    {
        byte ret = flag;
        setFlag((byte) 0);
        return ret;
    }

    public void setFlag(byte flag)
    {
        this.flag = flag;
    }
}
