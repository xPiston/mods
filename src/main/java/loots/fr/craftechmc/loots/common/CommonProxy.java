package fr.craftechmc.loots.common;

import java.util.logging.Level;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unascribed.lambdanetwork.DataType;
import com.unascribed.lambdanetwork.LambdaNetwork;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.core.common.CraftechCore;
import fr.craftechmc.core.common.tab.CTabManager;
import fr.craftechmc.core.common.utils.Smaz;
import fr.craftechmc.loots.common.blocks.BlockLootContainer;
import fr.craftechmc.loots.common.gui.CraftechLootsGuiHandler;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager.LootTablesWrapper;
import fr.craftechmc.loots.common.tiles.TileLootContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

public class CommonProxy
{
    protected final Gson                   gson           = new GsonBuilder().enableComplexMapKeySerialization()
            .create();
    protected final Smaz                   smaz           = new Smaz();
    public static final BlockLootContainer LOOT_CONTAINER = (BlockLootContainer) new BlockLootContainer(Material.rock)
            .setUnlocalizedName("BlockLootContainer");

    public void preInit(final FMLPreInitializationEvent e)
    {
        CTabManager.addTab("LOOTS", Item.getItemFromBlock(Blocks.chest));
        CommonProxy.LOOT_CONTAINER.setCreativeTab(CTabManager.getTab("LOOTS"));
        CraftechLoots.containerLootTablesManager = ContainerLootTablesManager.getInstance();
        NetworkRegistry.INSTANCE.registerGuiHandler(CraftechLoots.instance, new CraftechLootsGuiHandler());

        GameRegistry.registerBlock(CommonProxy.LOOT_CONTAINER, "BlockLootContainer");
        GameRegistry.registerTileEntity(TileLootContainer.class, CraftechLoots.MODID + ":TileLootContainer");

        CraftechLoots.network = LambdaNetwork.builder().channel(CraftechLoots.MODID)
                .packet(LootsPacketConstants.LOOT_TABLE_REQUEST).boundTo(Side.SERVER)
                .handledOnMainThreadBy((player, token) ->
                {
                    if (player.capabilities.isCreativeMode)
                        CraftechLoots.network.send().packet(LootsPacketConstants.LOOT_TABLE_DATA).with("tableData",
                                this.smaz.compress(
                                        this.gson.toJson(ContainerLootTablesManager.getInstance().getLootTables())))
                                .to(player);
                }).packet(LootsPacketConstants.LOOT_TABLE_DATA).with(DataType.BYTE_ARRAY, "tableData")
                .boundTo(Side.CLIENT).handledOnMainThreadBy((player, token) ->
                {
                    ContainerLootTablesManager.getInstance().getLootTables().getTables().clear();
                    ContainerLootTablesManager.getInstance().getLootTables().getTables()
                            .addAll(this.gson
                                    .fromJson(this.smaz.decompress(token.getData("tableData")), LootTablesWrapper.class)
                                    .getTables());
                    ContainerLootTablesManager.getInstance().getRefreshingProperty().setValue(false);
                }).packet(LootsPacketConstants.LOOT_TABLE_CHANGE).with(DataType.STRING, "tableName")
                .with(DataType.STRING, "changeType").with(DataType.ARBITRARY, "content").boundTo(Side.SERVER)
                .handledOnMainThreadBy((player, token) ->
                {
                    if (MinecraftServer.getServer().getConfigurationManager().getOppedPlayers()
                            .getGameProfileFromName(player.getCommandSenderName()) != null)
                        if (ContainerLootTablesManager.getInstance()
                                .getLootTableByName(token.getString("tableName")) != null)
                            if (token.getString("changeType") == "modify")
                            {

                            }
                            else if (token.getString("changeType") == "delete")
                            {

                            }
                            else if (token.getString("changeType") == "add")
                            {

                            }
                }).packet(LootsPacketConstants.LOOT_CONTAINER_LISTCHANGE).with(DataType.BOOLEAN, "remove")
                .with(DataType.STRING, "lootlistName").boundTo(Side.SERVER).with(DataType.INT, "lootlistLevel")
                .with(DataType.INT, "coordX").with(DataType.INT, "coordY").with(DataType.INT, "coordZ")
                .handledOnMainThreadBy((player, token) ->
                {
                    if (player.capabilities.isCreativeMode)
                    {
                        final TileLootContainer lootContainer = (TileLootContainer) player.getEntityWorld()
                                .getTileEntity(token.getInt("coordX"), token.getInt("coordY"), token.getInt("coordZ"));
                        if (lootContainer != null)
                        {
                            if (token.getBoolean("remove"))
                                lootContainer.getLoot().getLootLists().remove(
                                        Pair.of(token.getString("lootlistName"), token.getInt("lootlistLevel")));
                            else
                                lootContainer.getLoot().getLootLists().put(
                                        Pair.of(token.getString("lootlistName"), token.getInt("lootlistLevel")), 0L);
                            player.getEntityWorld().markBlockForUpdate(token.getInt("coordX"), token.getInt("coordY"),
                                    token.getInt("coordZ"));
                        }
                        else
                            CraftechCore.logger.log(Level.SEVERE,
                                    "A null TileEntity (TileLootContainer) was requested by "
                                            + player.getCommandSenderName() + " at [" + token.getInt("coordX") + ":"
                                            + token.getInt("coordY") + ":" + token.getInt("coordZ") + "]");
                    }
                }).build();
    }

    public void init(final FMLInitializationEvent e)
    {
    }

    public void postInit(final FMLPostInitializationEvent e)
    {

    }

    public void serverStarting(final FMLServerStartingEvent e)
    {
    }

    public void serverStarted(final FMLServerStartedEvent e)
    {
    }
}