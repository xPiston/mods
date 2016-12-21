package fr.craftechmc.weaponry.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class CWParticles
{
    private static Minecraft mc       = Minecraft.getMinecraft();
    private static World     theWorld = CWParticles.mc.theWorld;

    public static EntityFX spawnBulletImpact(double x, double y, double z, int side)
    {
        if (CWParticles.mc != null && CWParticles.mc.renderViewEntity != null && CWParticles.mc.effectRenderer != null)
        {
            int var14 = CWParticles.mc.gameSettings.particleSetting;

            if (var14 == 1 && CWParticles.theWorld.rand.nextInt(3) == 0)
            {
                var14 = 2;
            }

            final double var15 = CWParticles.mc.renderViewEntity.posX - x;
            final double var17 = CWParticles.mc.renderViewEntity.posY - y;
            final double var19 = CWParticles.mc.renderViewEntity.posZ - z;
            EntityFX entityFX = null;
            final double var22 = 64.0D;

            if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22)
            {
                return null;
            }
            else if (var14 > 1)
            {
                return null;
            }
            else
            {
                entityFX = new ImpactFX(CWParticles.theWorld, x, y, z, side);
                CWParticles.mc.effectRenderer.addEffect(entityFX);
                return entityFX;
            }
        }
        return null;
    }
}