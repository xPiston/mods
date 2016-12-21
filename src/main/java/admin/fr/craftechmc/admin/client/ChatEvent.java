package fr.craftechmc.admin.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatEvent
{
    @SubscribeEvent
    public void onClientChatReceivedEvent(ClientChatReceivedEvent event)
    {
        if (event.message.getFormattedText().contains("::Processing"))
        {
            final String schem = event.message.getFormattedText().substring(
                    event.message.getFormattedText().indexOf("(") + 1, event.message.getFormattedText().indexOf(")"));
            Minecraft.getMinecraft().thePlayer.sendChatMessage("//schematic save " + schem);
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/schemsave ... " + schem);
        }
        else if (event.message.getFormattedText().contains("::Loading"))
        {
            final String schem = event.message.getFormattedText().substring(
                    event.message.getFormattedText().indexOf("(") + 1, event.message.getFormattedText().indexOf(")"));
            Minecraft.getMinecraft().thePlayer.sendChatMessage("//schematic load " + schem + "-temp");
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/schemload ... " + schem);
            System.out.println(schem);
        }
    }
}