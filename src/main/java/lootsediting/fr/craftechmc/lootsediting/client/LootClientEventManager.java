package fr.craftechmc.lootsediting.client;

import org.lwjgl.opengl.GL11;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftechmc.lootsediting.common.CraftechLootsEditing;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class LootClientEventManager
{
    public static double clip(double value, final double min, final double max)
    {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static void translateToWorldCoords(final Entity entity, final float frame)
    {
        final double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
        final double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
        final double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
    }

    @SubscribeEvent
    public void onRenderLast(final RenderWorldLastEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer
                .getCurrentEquippedItem().getItem().equals(CraftechLootsEditing.proxy.tool))
        {
            GL11.glPushMatrix();

            final World world = Minecraft.getMinecraft().theWorld;
            final Entity entity = Minecraft.getMinecraft().renderViewEntity;
            LootClientEventManager.translateToWorldCoords(entity, event.partialTicks);
            final int x = (int) entity.posX;
            final int z = (int) entity.posZ;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glLineWidth(1.5F);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glColor4d(.8, 0, 0, 1);

            for (int l = -4; l < 4; l++)
                for (int m = -4; m < 4; m++)
                {
                    final Chunk chunk = world.getChunkFromBlockCoords(x + l * 16, z + m * 16);
                    final int startX = chunk.xPosition * 16;
                    final int startZ = chunk.zPosition * 16;

                    for (int i = 0; i < 16; i++)
                        for (int j = 0; j < 16; j++)
                            for (int k = 0; k < 256; k++)
                            {
                                GL11.glVertex3d(startX + i, k + 0.04, startZ + j);
                                GL11.glVertex3d(startX + i + 1, k + 0.04, startZ + j + 1);
                                GL11.glVertex3d(startX + i + 1, k + 0.04, startZ + j);
                                GL11.glVertex3d(startX + i, k + 0.04, startZ + j + 1);
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