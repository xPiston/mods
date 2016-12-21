package fr.craftechmc.admin.common;

import com.unascribed.lambdanetwork.DataType;
import com.unascribed.lambdanetwork.LambdaNetwork;
import com.unascribed.lambdanetwork.Token;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.admin.common.command.SchematicLoadCommand;
import fr.craftechmc.admin.common.command.SchematicSaveCommand;
import fr.craftechmc.admin.common.fogpath.FogPathManager;
import fr.craftechmc.admin.common.fogpath.ItemEditorTool;
import fr.craftechmc.core.math.Vector3d;
import fr.craftechmc.environment.common.fog.FogPathWorldSavedData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public class CommonProxy
{
    private boolean lockRightClickAir = false;

    public void preInit(final FMLPreInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        GameRegistry.registerItem(CraftechAdmin.FOGPATH_EDITOR_TOOL, "FogPathEditorTool");

        // Build network system
        CraftechAdmin.network = LambdaNetwork.builder().channel(CraftechAdmin.MODID)
                .packet(AdminPacketConstants.FOG_PATH_SYNC).with(DataType.NBT_COMPOUND, "zones").boundTo(Side.CLIENT)
                .handledOnMainThreadBy((player, token) ->
                {
                    if (player.capabilities.isCreativeMode)
                        this.updateFogPaths(token);
                }).packet(AdminPacketConstants.NAME_INPUT).with(DataType.STRING, "name").boundTo(Side.SERVER)
                .handledOnMainThreadBy((player, token) -> FogPathManager.getInstance().input(token.getString("name")))
                .packet(AdminPacketConstants.SELECT_INPUT).with(DataType.STRING, "elName").boundTo(Side.SERVER)
                .handledOnMainThreadBy((player, token) -> FogPathManager.getInstance()
                        .select(FogPathManager.getInstance().getElementByName(token.getString("elName"))))
                .build();
    }

    public void init(final FMLInitializationEvent e)
    {
    }

    public void postInit(final FMLPostInitializationEvent e)
    {

    }

    public void serverStarting(final FMLServerStartingEvent e)
    {
        e.registerServerCommand(new SchematicLoadCommand());
        e.registerServerCommand(new SchematicSaveCommand());
    }

    public void serverStarted(final FMLServerStartedEvent e)
    {
    }

    @SubscribeEvent
    public void onPlayerBreakBlock(final BlockEvent.BreakEvent event)
    {
        if (event.getPlayer().getCurrentEquippedItem() != null
                && event.getPlayer().getCurrentEquippedItem().getItem() instanceof ItemEditorTool)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        final ItemStack equippedItem = event.entityPlayer.getCurrentEquippedItem();
        if (equippedItem != null && equippedItem.getItem() instanceof ItemEditorTool)
        {
            final Vector3d facedBlockPos = ItemEditorTool.getFacedBlock(event.x, event.y, event.z, event.face);
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            {
                FogPathManager.getInstance().addCheckpoint((int) facedBlockPos.getX(), (int) facedBlockPos.getY(),
                        (int) facedBlockPos.getZ(), event.entityPlayer);
                this.lockRightClickAir = true;
            }
            else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
            {
                if (!this.lockRightClickAir)
                {
                    FogPathManager.getInstance().setLastCoords(null);
                    FogPathManager.getInstance().selectZone();
                }
                this.lockRightClickAir = false;
            }
            else if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
                FogPathManager.getInstance().removeCheckpoint((int) facedBlockPos.getX(), (int) facedBlockPos.getY(),
                        (int) facedBlockPos.getZ(), event.entityPlayer);
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateFogPaths(final Token token)
    {
        FogPathWorldSavedData.get(net.minecraft.client.Minecraft.getMinecraft().theWorld)
                .readFromNBT(token.getNBT("zones"));
    }
}