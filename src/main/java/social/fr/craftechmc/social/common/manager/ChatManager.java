package fr.craftechmc.social.common.manager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ChatManager {

	private void sendAllMessage(EntityPlayerMP player, String message){
		MinecraftServer mcServers = MinecraftServer.getServer();
		String[] playerUsernameList = mcServers.getAllUsernames();
		for(int i=0; i < playerUsernameList.length; i++)
			for(World world : mcServers.worldServers){
				world.getPlayerEntityByName(playerUsernameList[i]).addChatMessage(new ChatComponentText(player.getDisplayName() + " : " + message));
			}
	}
}
