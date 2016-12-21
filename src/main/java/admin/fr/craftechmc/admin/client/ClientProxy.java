package fr.craftechmc.admin.client;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftechmc.admin.common.CommonProxy;
import fr.craftechmc.admin.common.fogpath.FogPathManager;
import fr.craftechmc.admin.common.fogpath.ItemEditorTool;
import fr.craftechmc.core.math.Vector3d;
import fr.craftechmc.environment.common.fog.FogPath;
import fr.craftechmc.environment.common.fog.FogPathWorldSavedData;
import fr.craftechmc.environment.common.fog.FogZone;
import fr.craftechmc.lootsediting.client.LootClientEventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(new ChatEvent());

        FMLCommonHandler.instance().bus().register(new AdminKeyHandler());
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(final EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayer)
            FogPathManager.getInstance().setWorld(net.minecraft.client.Minecraft.getMinecraft().theWorld);
    }

    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null
                && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemEditorTool)
        {
            GL11.glPushMatrix();

            final Entity entity = Minecraft.getMinecraft().renderViewEntity;
            LootClientEventManager.translateToWorldCoords(entity, event.partialTicks);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glLineWidth(1.5f);

            GL11.glColor4d(0, 0, .8, 1);

            GL11.glBegin(GL11.GL_LINES);
            for (final FogZone zone : FogPathWorldSavedData.get(Minecraft.getMinecraft().theWorld).getZones())
                for (final FogPath fogPath : zone.getFogPaths())
                {
                    Vector3d lastCoords = null;
                    final LinkedList<Vector3d> checkpoints = fogPath.getCheckpoints();
                    for (final Vector3d coords : checkpoints)
                    {
                        if (lastCoords == null || coords.equals(checkpoints.get(checkpoints.size() - 1)))
                            GL11.glColor4d(.8, 0, 0, 1);
                        else
                            GL11.glColor4d(.8, .8, 0, 1);
                        GL11.glVertex3d(coords.getX() + 0.25, coords.getY() + .5, coords.getZ() + 0.25);
                        GL11.glVertex3d(coords.getX() + 0.75, coords.getY() + .5, coords.getZ() + 0.25);
                        GL11.glVertex3d(coords.getX() + 0.75, coords.getY() + .5, coords.getZ() + 0.25);
                        GL11.glVertex3d(coords.getX() + 0.75, coords.getY() + .5, coords.getZ() + 0.75);
                        GL11.glVertex3d(coords.getX() + 0.75, coords.getY() + .5, coords.getZ() + 0.75);
                        GL11.glVertex3d(coords.getX() + 0.25, coords.getY() + .5, coords.getZ() + 0.75);
                        GL11.glVertex3d(coords.getX() + 0.25, coords.getY() + .5, coords.getZ() + 0.75);
                        GL11.glVertex3d(coords.getX() + 0.25, coords.getY() + .5, coords.getZ() + 0.25);
                        GL11.glColor4d(0, 0, .8, 1);

                        if (lastCoords == null)
                        {
                            lastCoords = coords;
                            continue;
                        }
                        GL11.glVertex3d(lastCoords.getX() + 0.5, lastCoords.getY() + 0.5, lastCoords.getZ() + 0.5);
                        GL11.glVertex3d(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);
                        lastCoords = coords;
                    }
                }
            GL11.glEnd();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();
        }
    }
}