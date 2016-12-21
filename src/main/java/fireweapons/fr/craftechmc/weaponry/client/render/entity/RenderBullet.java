package fr.craftechmc.weaponry.client.render.entity;

import fr.craftechmc.weaponry.CraftechWeaponry;
import fr.craftechmc.weaponry.entity.EntityBulletProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderBullet extends Render
{
    private final ResourceLocation TEX_BULLET   = new ResourceLocation(CraftechWeaponry.MODASSETS,
            "textures/models/model_bullet.png");
    private final IModelCustom     MODEL_BULLET = AdvancedModelLoader
            .loadModel(new ResourceLocation(CraftechWeaponry.MODASSETS, "models/bullet.obj"));

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float f1)
    {
        this.renderBullet((EntityBulletProjectile) entity, x, y, z, f, f1);
    }

    public void renderBullet(EntityBulletProjectile entity, double x, double y, double z, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glScaled(.5, .5, .5);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F,
                0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F,
                1.0F);
        GL11.glTranslated(0, -2, 0);
        this.bindTexture(this.getEntityTexture(entity));
        this.MODEL_BULLET.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.TEX_BULLET;
    }
}
