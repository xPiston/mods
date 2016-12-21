package fr.craftechmc.lootsediting.common.command;

import java.util.List;
import java.util.Optional;

import fr.craftechmc.loots.common.manager.LootItemManager;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

/**
 * @author Ourten 12 déc. 2016
 */
public class LootItemCommand implements ICommand
{
    @Override
    public int compareTo(final Object o)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "loot";
    }

    @Override
    public String getCommandUsage(final ICommandSender sender)
    {
        return "/loot add | remove | list | name";
    }

    @Override
    public List getCommandAliases()
    {
        return null;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (args.length == 2)
        {
            if (args[0].equals("remove"))
            {
                if (LootItemManager.getInstance().removeLootItem(args[1]))
                    sender.addChatMessage(new ChatComponentText("Loot Item " + args[1] + " has been deleted."));
                else
                    sender.addChatMessage(new ChatComponentText("Could not find any Loot Item named " + args[1]));
            }
        }
        else if (args.length == 1)
        {
            if (args[0].equals("list"))
            {
                sender.addChatMessage(new ChatComponentText("List of loot items registered on the server:"));
                LootItemManager.getInstance().getLootItems().keySet()
                        .forEach(loot -> sender.addChatMessage(new ChatComponentText(loot)));
            }
            else if (args[0].equals("add"))
            {
                if (MinecraftServer.getServer().getConfigurationManager()
                        .getPlayerByUsername(sender.getCommandSenderName()).getCurrentEquippedItem() != null)
                {
                    LootItemManager.getInstance().addLootItem(MinecraftServer.getServer().getConfigurationManager()
                            .getPlayerByUsername(sender.getCommandSenderName()).getCurrentEquippedItem());
                }
                else
                    sender.addChatMessage(new ChatComponentText("Duh. You have no item in your hand."));
            }
            else if (args[0].equals("remove"))
            {
                if (LootItemManager.getInstance().removeLootItem(MinecraftServer.getServer().getConfigurationManager()
                        .getPlayerByUsername(sender.getCommandSenderName()).getCurrentEquippedItem()))
                    sender.addChatMessage(
                            new ChatComponentText("The Loot Item extracted from your hand has been deleted."));
                else
                    sender.addChatMessage(
                            new ChatComponentText("Could not find any Loot Item from whats in your hand."));
            }
            else if (args[0].equals("name"))
            {
                final Optional<String> loot = LootItemManager.getInstance()
                        .getLootItemName(MinecraftServer.getServer().getConfigurationManager()
                                .getPlayerByUsername(sender.getCommandSenderName()).getCurrentEquippedItem());
                if (loot.isPresent())
                    sender.addChatMessage(new ChatComponentText("The name of your current item is " + loot.get()));
                else
                    sender.addChatMessage(new ChatComponentText("There is no banana here."));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender)
    {
        return MinecraftServer.getServer().getConfigurationManager().getOppedPlayers()
                .getGameProfileFromName(sender.getCommandSenderName()) != null;
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int index)
    {
        return false;
    }
}