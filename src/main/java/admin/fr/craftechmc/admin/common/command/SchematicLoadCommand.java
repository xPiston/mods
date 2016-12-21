package fr.craftechmc.admin.common.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class SchematicLoadCommand implements ICommand
{
    @Override
    public int compareTo(final Object arg0)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "schemload";
    }

    @Override
    public String getCommandUsage(final ICommandSender sender)
    {
        return "schemload <name>";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases()
    {
        return null;
    }

    public HashMap<Integer, String> getEquivalences(final File patch)
    {
        final HashMap<Integer, String> equivalences = Maps.newHashMap();

        try
        {
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(patch), Charsets.UTF_8));
            String str;

            while ((str = reader.readLine()) != null)
                equivalences.put(Integer.parseInt(str.split(" = ")[0]), str.split(" = ")[1]);
            reader.close();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
        return equivalences;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (!args[0].equals("..."))
        {
            final File origin = new File("/home/minecraft/schematics/" + args[0] + ".schematic");
            final File patch = new File("/home/minecraft/schematics/" + args[0] + ".schematic.patch");
            final File temp = new File("/home/minecraft/schematics/" + args[0] + "-temp.schematic");

            try
            {
                Files.copy(origin, temp);

                final HashMap<Integer, String> equivalences = this.getEquivalences(patch);

                final NBTTagCompound tag = CompressedStreamTools.readCompressed(new FileInputStream(temp));

                final int[] blocks = new int[tag.getShort("Width") * tag.getShort("Length") * tag.getShort("Height")];
                final byte[] b_lower = tag.getByteArray("Blocks");

                byte[] addBlocks = new byte[0];
                if (tag.hasKey("AddBlocks"))
                    addBlocks = tag.getByteArray("AddBlocks");

                for (int index = 0; index < b_lower.length; index++)
                    if (index >> 1 >= addBlocks.length)
                        blocks[index] = (short) (b_lower[index] & 0xFF);
                    else if ((index & 1) == 0)
                        blocks[index] = (short) (((addBlocks[index >> 1] & 0x0F) << 8) + (b_lower[index] & 0xFF));
                    else
                        blocks[index] = (short) (((addBlocks[index >> 1] & 0xF0) << 4) + (b_lower[index] & 0xFF));
                for (int i = 0; i < blocks.length; i++)
                    blocks[i] = Block.getIdFromBlock(Block.getBlockFromName(equivalences.get(blocks[i])));

                final byte[] blocks_lower = new byte[blocks.length];
                final double d = blocks.length / 2D;
                final byte[] blocks_upper = new byte[(int) Math.ceil(d)];

                for (int index = 0; index < blocks.length; index++)
                {
                    blocks_lower[index] = (byte) (blocks[index] & 0xFF);
                    final byte upperbits = (byte) ((blocks[index] & 0xF00) >> 8);

                    if ((index & 1) == 0)
                        blocks_upper[index >> 1] = (byte) (blocks_upper[index >> 1] | upperbits);
                    else
                        blocks_upper[index >> 1] = (byte) (blocks_upper[index >> 1] | upperbits << 4);
                }

                tag.setByteArray("Blocks", blocks_lower);
                boolean tripAddBlocks = false;
                for (final byte b : blocks_upper)
                    if (b > 0)
                    {
                        tripAddBlocks = true;
                        break;
                    }

                if (tripAddBlocks)
                    tag.setByteArray("AddBlocks", blocks_upper);
                CompressedStreamTools.writeCompressed(tag, new FileOutputStream(temp));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "::Loading Schematic "
                        + EnumChatFormatting.GREEN + "(" + args[0] + ")"));
            } catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
        else
            new File("/home/minecraft/schematics/" + args[1] + "-temp.schematic").deleteOnExit();
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
        final List<String> l = Lists.newArrayList();

        for (final File f : new File("/home/minecraft/schematics").listFiles())
            if (f.getName().endsWith(".patch"))
                l.add(f.getName().replace(".schematic.patch", ""));
        return l;
    }

    @Override
    public boolean isUsernameIndex(final String[] sender, final int p_82358_2_)
    {
        return false;
    }
}