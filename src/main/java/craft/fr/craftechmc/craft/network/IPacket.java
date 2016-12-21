package fr.craftechmc.craft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;

/**
 * Simplest packet representation
 * Created by Thog the 16/04/2016
 */
public interface IPacket<T extends IMessage> extends IMessage, IMessageHandler<T, IMessage>
{
}
