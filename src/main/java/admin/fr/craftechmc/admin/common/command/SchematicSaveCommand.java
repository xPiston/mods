package fr.craftechmc.admin.common.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class SchematicSaveCommand implements ICommand
{
    @Override
    public int compareTo(final Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "schemsave";
    }

    @Override
    public String getCommandUsage(final ICommandSender sender)
    {
        return "schemsave <name>";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases()
    {
        return null;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (args.length >= 1)
            if (args[0].equals("..."))
            {
                final File schematic = new File("/home/minecraft/schematics/" + args[1] + ".schematic");

                try
                {
                    final NBTTagCompound tag = CompressedStreamTools.readCompressed(new FileInputStream(schematic));

                    if (tag != null && tag.hasKey("Blocks"))
                    {
                        final int[] blocks = new int[tag.getShort("Width") * tag.getShort("Length")
                                * tag.getShort("Height")];
                        final byte[] b_lower = tag.getByteArray("Blocks");

                        byte[] addBlocks = new byte[0];
                        if (tag.hasKey("AddBlocks"))
                            addBlocks = tag.getByteArray("AddBlocks");

                        for (int index = 0; index < b_lower.length; index++)
                            if (index >> 1 >= addBlocks.length)
                                blocks[index] = (short) (b_lower[index] & 0xFF);
                            else if ((index & 1) == 0)
                                blocks[index] = (short) (((addBlocks[index >> 1] & 0x0F) << 8)
                                        + (b_lower[index] & 0xFF));
                            else
                                blocks[index] = (short) (((addBlocks[index >> 1] & 0xF0) << 4)
                                        + (b_lower[index] & 0xFF));
                        final HashMap<Integer, String> equivalences = new HashMap<>();
                        for (final int i : blocks)
                            if (!equivalences.containsKey(i))
                                equivalences.put(i, Block.blockRegistry.getNameForObject(Block.getBlockById(i)));
                        sender.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.AQUA + "Found and processed " + EnumChatFormatting.GREEN
                                        + equivalences.size() + EnumChatFormatting.AQUA + " unique blocks."));

                        final File patch = new File("/home/minecraft/schematics/" + args[1] + ".schematic.patch");
                        patch.createNewFile();

                        final BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(new FileOutputStream(patch), Charsets.UTF_8));
                        for (final Map.Entry<Integer, String> entry : equivalences.entrySet())
                        {
                            writer.write(entry.getKey() + " = " + entry.getValue());
                            writer.newLine();
                        }
                        writer.close();
                    }
                    else
                        sender.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.DARK_RED + "No schematic found or no blocks identified !"));
                } catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "::Processing Schematic "
                        + EnumChatFormatting.GREEN + "(" + args[0] + ")"));
    }

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender)
    {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] p_71516_2_)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_)
    {
        return false;
    }
}