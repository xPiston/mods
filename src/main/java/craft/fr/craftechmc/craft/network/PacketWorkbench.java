package fr.craftechmc.craft.network;

import com.google.common.base.Charsets;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.craftechmc.craft.CraftechCraft;
import fr.craftechmc.craft.api.BasicEntryRenderer;
import fr.craftechmc.craft.api.IUpgradableEntry;
import fr.craftechmc.craft.api.IWorkbenchEntry;
import fr.craftechmc.craft.api.WorkbenchRegistry;
import fr.craftechmc.craft.container.SlotOutput;
import fr.craftechmc.craft.container.WorkbenchContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc...
 * Created by Thog the 24/04/2016
 */
public class PacketWorkbench implements IPacket<PacketWorkbench>
{
    private ItemStack                    stack;
    private byte                         type;
    private List<IWorkbenchEntry.Render> toDisplay;

    public PacketWorkbench()
    {
        this.type = -1;
    }

    public PacketWorkbench(byte type)
    {
        this.type = type;
    }

    public PacketWorkbench(List<IWorkbenchEntry.Render> itemStackList)
    {
        this((byte) 0);
        this.toDisplay = itemStackList;
    }

    public PacketWorkbench(byte type, ItemStack stack)
    {
        this(type);
        this.stack = stack;
    }

    @Override public void fromBytes(ByteBuf buf)
    {
        PacketBuffer wrapper = new PacketBuffer(buf);
        this.type = wrapper.readByte();

        if (this.type == 0)
        {
            int listSize = buf.readInt();
            if (listSize != 0)
            {
                this.toDisplay = new ArrayList<>();
                for (int i = 0; i < listSize; i++)
                {
                    try
                    {
                        ItemStack stack = wrapper.readItemStackFromBuffer();
                        byte[] data = wrapper.readBytes(wrapper.readInt()).array();
                        IWorkbenchEntry.Render render = new BasicEntryRenderer(stack, new String(data, Charsets.UTF_8));
                        this.toDisplay.add(render);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
            }

        }
        else if (this.type == 1 || this.type == 2)
        {
            try
            {
                if (!wrapper.readBoolean())
                    this.stack = wrapper.readItemStackFromBuffer();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override public void toBytes(ByteBuf buf)
    {
        PacketBuffer wrapper = new PacketBuffer(buf);
        wrapper.writeByte(type);
        if (this.type == 0)
        {
            int size = toDisplay == null ? 0 : this.toDisplay.size();
            buf.writeInt(size);
            for (int i = 0; i < size; i++)
            {
                try
                {
                    IWorkbenchEntry.Render render = toDisplay.get(i);
                    wrapper.writeItemStackToBuffer(render.getIcon());
                    byte[] data = render.getName().getBytes(Charsets.UTF_8);
                    wrapper.writeInt(data.length);
                    wrapper.writeBytes(data);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }
        else if (this.type == 1 || this.type == 2)
        {
            try
            {
                wrapper.writeBoolean(this.stack == null);
                wrapper.writeItemStackToBuffer(this.stack);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override public PacketWorkbench onMessage(PacketWorkbench message, MessageContext ctx)
    {
        if (message.type == 0)
            CraftechCraft.getProxy().guiHandler().setData(message.toDisplay, 1);
        else if (message.type == 1)
        {
            IWorkbenchEntry entry = WorkbenchRegistry.getEntryByIcon(message.stack);
            if (entry != null)
            {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                if (entry.concludeTransaction(player))
                {
                    List<IWorkbenchEntry.Render> renders = WorkbenchRegistry.buildAvailableList(player);
                    CraftechCraft.getProxy().pipeline()
                            .sendTo(new PacketWorkbench(renders.isEmpty() ? null : renders), player);
                }

            }
        }
        else if (message.type == 2)
            CraftechCraft.getProxy().guiHandler().setData(message.stack, 2);
        else if (message.type == 3)
        {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player.openContainer instanceof WorkbenchContainer)
            {
                WorkbenchContainer container = (WorkbenchContainer) player.openContainer;
                IUpgradableEntry entry = WorkbenchRegistry.getUpgradeEntry(container.getInventoryUpgrade());
                SlotOutput output = (SlotOutput) container.getUpgradeSlot(3);
                if (entry != null)
                {
                    ItemStack result = entry.getResult();
                    if (result != null)
                    {
                        output.putStack(result.copy());
                        output.onPickupFromSlot(player, output.getStack());
                        output.setOpaqueStack(player, null);
                        container.detectAndSendChanges();
                    }
                }
            }
        }
        return null;
    }
}
