package fr.craftechmc.admin.server;

import java.util.HashMap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.craftechmc.admin.common.CommonProxy;
import fr.craftechmc.admin.common.fogpath.FogPathManager;
import fr.craftechmc.admin.common.fogpath.ItemEditorTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ServerProxy extends CommonProxy
{
    private static final HashMap<EntityPlayer, Item> fogPathPlayerHeldItem = new HashMap<>();

    public static boolean isPlayerUsingFogPath()
    {
        if (fogPathPlayerHeldItem.size() == 0)
            return false;
        for (Item item : fogPathPlayerHeldItem.values())
            if (item instanceof ItemEditorTool)
                return true;
        return false;
    }

    public static boolean isPlayerUsingFogPath(EntityPlayer player)
    {
        return fogPathPlayerHeldItem.containsKey(player) && fogPathPlayerHeldItem
                .get(player) instanceof ItemEditorTool;
    }

    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event)
    {
        // Sync fog paths
        if (event.entity instanceof EntityPlayer)
            FogPathManager.getInstance().sync((EntityPlayer) event.entity);
    }

    @SubscribeEvent
    public void onPlayerChangeHeldItem(TickEvent.PlayerTickEvent event)
    {
        // Manage checpoint show/hide
        if (event.phase.equals(TickEvent.Phase.END) && (
                fogPathPlayerHeldItem.get(event.player) != null && event.player.getCurrentEquippedItem() == null
                        || event.player.getCurrentEquippedItem() != null && !event.player.getCurrentEquippedItem()
                        .getItem().equals(fogPathPlayerHeldItem.get(event.player))) && (
                isPlayerUsingFogPath(event.player) || event.player.getCurrentEquippedItem() != null && event.player
                        .getCurrentEquippedItem().getItem() instanceof ItemEditorTool))
        {
            if (event.player.getCurrentEquippedItem() == null)
                fogPathPlayerHeldItem.remove(event.player);
            else
            {
                // Warns players there's a danger
                if (isPlayerUsingFogPath() && FMLCommonHandler.instance().getEffectiveSide().isServer())
                {
                    event.player.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.RED + "WARNING" + EnumChatFormatting.YELLOW + " : "
                                    + EnumChatFormatting.AQUA + ((EntityPlayer) fogPathPlayerHeldItem.keySet()
                                    .toArray()[0]).getDisplayName() + EnumChatFormatting.YELLOW
                                    + " utilise déjà l'outil ! Si tu l'utilises en même temps, tout risque d'être aspiré par une faille spatio-temporelle de ta création."));
                    ((EntityPlayer) fogPathPlayerHeldItem.keySet().toArray()[0]).addChatMessage(new ChatComponentText(
                            EnumChatFormatting.RED + "WARNING" + EnumChatFormatting.YELLOW + " : "
                                    + EnumChatFormatting.AQUA + event.player.getDisplayName()
                                    + EnumChatFormatting.YELLOW
                                    + " essaie d'utiliser l'outil ! Il/Elle a été prévenu-e, mais sois prudent-e. Peut-être devrais-tu le/la prévenir ?"));
                }
                fogPathPlayerHeldItem.put(event.player, event.player.getCurrentEquippedItem().getItem());
            }
        }
    }
}