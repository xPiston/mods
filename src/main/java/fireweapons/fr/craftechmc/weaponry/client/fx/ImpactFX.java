package fr.craftechmc.weaponry.client.fx;

import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftechmc.weaponry.CraftechWeaponry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ImpactFX extends EntityFX
{
    private final ResourceLocation TEX_IMPACT = new ResourceLocation(CraftechWeaponry.MODASSETS, "textures/impact.png");

    private final int              side;

    public ImpactFX(World w, double x, double y, double z, int side)
    {
        super(w, x, y, z);
        this.side = side;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        // this.particleScale=1*((float)this.particleAge/40);
        // this.particleAlpha=1-((float)this.particleAge/40);

        if (this.particleAge >= this.particleMaxAge)
            this.setDead();
        this.particleAge++;
    }

    @Override
    public int getFXLayer()
    {
        return 2;
    }

    @Override
    public void renderParticle(Tessellator t, float f, float p_70539_3_, float p_70539_4_, float p_70539_5_,
            float p_70539_6_, float p_70539_7_)
    {
        t.draw();

        Minecraft.getMinecraft().renderEngine.bindTexture(this.TEX_IMPACT);

        GL11.glPushMatrix();

        final float xx = (float) (this.prevPosX + (this.posX - this.prevPosX) * f - EntityFX.interpPosX);
        final float yy = (float) (this.prevPosY + (this.posY - this.prevPosY) * f - EntityFX.interpPosY);
        final float zz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * f - EntityFX.interpPosZ);
        GL11.glTranslated(xx, yy + .002, zz);

        if (this.side == 4)
            GL11.glRotated(-90, 0, 0, 1);
        else if (this.side == 5)
            GL11.glRotated(90, 0, 0, 1);
        else if (this.side == 2)
            GL11.glRotated(90, 1, 0, 0);
        else if (this.side == 3)
            GL11.glRotated(-90, 1, 0, 0);

        t.startDrawingQuads();
        t.setBrightness(200);
        t.setColorRGBA_F(1, 1, 1, this.particleAlpha);
        t.addVertexWithUV(-this.particleScale / 2, -.001, -this.particleScale / 2, 0, 0);
        t.addVertexWithUV(-this.particleScale / 2, -.001, this.particleScale / 2, 0, 1);
        t.addVertexWithUV(this.particleScale / 2, -.001, this.particleScale / 2, 1, 1);
        t.addVertexWithUV(this.particleScale / 2, -.001, -this.particleScale / 2, 1, 0);
        t.draw();

        t.startDrawingQuads();
        t.setBrightness(200);
        t.setColorRGBA_F(1, 1, 1, this.particleAlpha);
        t.addVertexWithUV(-this.particleScale / 2, -.041, -this.particleScale / 2, 0, 0);
        t.addVertexWithUV(this.particleScale / 2, -.041, -this.particleScale / 2, 1, 0);
        t.addVertexWithUV(this.particleScale / 2, -.041, this.particleScale / 2, 1, 1);
        t.addVertexWithUV(-this.particleScale / 2, -.041, this.particleScale / 2, 0, 1);
        t.draw();

        GL11.glPopMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(ImpactFX.getParticleTexture());
        t.startDrawingQuads();
    }

    public static ResourceLocation getParticleTexture()
    {
        try
        {
            return (ResourceLocation) ReflectionHelper.getPrivateValue(EffectRenderer.class, null,
                    new String[] { "particleTextures", "b", "field_110737_b" });
        } catch (final Exception e)
        {
        }
        return null;
    }
}