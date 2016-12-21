package fr.craftechmc.needs.common.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.craftechmc.needs.common.properties.NeedsProperties;
import net.minecraft.client.Minecraft;

/**
 * Created by arisu on 28/06/2016.
 */
public class NeedsMessageHandler implements IMessageHandler<NeedsMessage, IMessage>
{
    @Override
    public IMessage onMessage(NeedsMessage message, MessageContext ctx)
    {
        NeedsProperties.get(Minecraft.getMinecraft().thePlayer).loadNBTData(message.data);
        return null; // No response needed
    }
}
