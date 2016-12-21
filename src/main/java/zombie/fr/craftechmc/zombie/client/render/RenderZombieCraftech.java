package fr.craftechmc.zombie.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.zombie.client.model.ModelZombieCraftech;
import fr.craftechmc.zombie.common.CraftechZombie;
import fr.craftechmc.zombie.common.entity.EntityZombieCraftech;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Phenix246 on 20/06/2016.
 */
@SideOnly(Side.CLIENT)
public class RenderZombieCraftech extends RenderLiving
{

    private static final String              BASE_LOC            = "textures/entity/zombie/";
    private static final ResourceLocation    ZOMBIE_TEXTURES[]   = {
            new ResourceLocation(CraftechZombie.MODID, RenderZombieCraftech.BASE_LOC + "zombiecraftech_1.png"),
            new ResourceLocation(CraftechZombie.MODID, RenderZombieCraftech.BASE_LOC + "zombiecraftech_2.png") };

    private static final ModelZombieCraftech ALTERNATIVE_MODEL[] = { new ModelZombieCraftech(false, false),
            new ModelZombieCraftech(false, true), new ModelZombieCraftech(true, false),
            new ModelZombieCraftech(true, true) };

    public RenderZombieCraftech()
    {
        super(new ModelZombieCraftech(), 0.5F);
    }

    public int getModelIndex(Entity entity)
    {
        return (int) (Math.abs(entity.getUniqueID().getMostSignificantBits()) % 4);
    }

    public void switchModel(Entity entity)
    {
        this.mainModel = RenderZombieCraftech.ALTERNATIVE_MODEL[this.getModelIndex(entity)];
    }

    public int getSkinIndex(Entity entity)
    {
        return (int) (Math.abs(entity.getUniqueID().getMostSignificantBits())
                % RenderZombieCraftech.ZOMBIE_TEXTURES.length);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityZombieCraftech zombie, int slot, float var3)
    {
        this.switchModel(zombie);
        return super.shouldRenderPass(zombie, slot, var3);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void func_76986_a(T entity, double d, double d1, double d2, float f,
     * float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityZombieCraftech zombie, double xPos, double yPos, double zPos, float entityYaw,
            float partialTicks)
    {
        // this.mainModel
        super.doRender(zombie, xPos, yPos, zPos, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityZombieCraftech zombie)
    {
        return RenderZombieCraftech.ZOMBIE_TEXTURES[this.getSkinIndex(zombie)];
    }

    protected void renderEquippedItems(EntityZombieCraftech zombie, float var2)
    {
        // this.mainModel
        super.renderEquippedItems(zombie, var2);
    }

    protected void rotateCorpse(EntityZombieCraftech zombie, float xRot, float yRot, float zRot)
    {
        super.rotateCorpse(zombie, xRot, yRot, zRot);
    }

    protected void renderEquippedItems(EntityLiving zombie, float var2)
    {
        this.renderEquippedItems((EntityZombieCraftech) zombie, var2);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving zombie)
    {
        return this.getEntityTexture((EntityZombieCraftech) zombie);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void func_76986_a(T entity, double d, double d1, double d2, float f,
     * float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(EntityLiving entityLiving, double xPos, double yPos, double zPos, float entityYaw,
            float partialTicks)
    {
        this.doRender((EntityZombieCraftech) entityLiving, xPos, yPos, zPos, entityYaw, partialTicks);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving entityLiving, int slot, float var3)
    {
        return this.shouldRenderPass((EntityZombieCraftech) entityLiving, slot, var3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase entityLiving, int slot, float var3)
    {
        return this.shouldRenderPass((EntityZombieCraftech) entityLiving, slot, var3);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase entityLiving, float var2)
    {
        this.renderEquippedItems((EntityZombieCraftech) entityLiving, var2);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase entityLiving, float xRot, float yRot, float zRot)
    {
        this.rotateCorpse((EntityZombieCraftech) entityLiving, xRot, yRot, zRot);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void func_76986_a(T entity, double d, double d1, double d2, float f,
     * float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(EntityLivingBase entityLiving, double xPos, double yPos, double zPos, float entityYaw,
            float partialTicks)
    {
        this.doRender((EntityZombieCraftech) entityLiving, xPos, yPos, zPos, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityZombieCraftech) entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void func_76986_a(T entity, double d, double d1, double d2, float f,
     * float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity entity, double xPos, double yPos, double zPos, float entityYaw, float partialTicks)
    {
        this.doRender((EntityZombieCraftech) entity, xPos, yPos, zPos, entityYaw, partialTicks);
    }
}
