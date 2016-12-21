package fr.craftechmc.weaponry.client.render.item;

import fr.craftechmc.weaponry.CraftechWeaponry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderWeapon implements IItemRenderer
{
    private final IModelCustom     modelWeapon;
    private final ResourceLocation textWeapon;

    public float                   currentAiming = 0f;

    public RenderWeapon(final String name)
    {
        this.modelWeapon = AdvancedModelLoader
                .loadModel(new ResourceLocation(CraftechWeaponry.MODASSETS, "models/" + name + ".obj"));
        this.textWeapon = new ResourceLocation(CraftechWeaponry.MODASSETS, "textures/models/model_" + name + ".png");
    }

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
            final ItemRendererHelper helper)
    {
        return type.equals(ItemRenderType.INVENTORY);
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(this.textWeapon);
        GL11.glPushMatrix();

        if (type.equals(ItemRenderType.EQUIPPED))
        {
            GL11.glTranslatef(0.6F, -.3F, 0);
            GL11.glScalef(.2f, .2f, .2f);
            GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(10F, 1.0F, 0.0F, 0.0F);
        }
        else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
        {
            if (item.getTagCompound().getBoolean("isAiming"))
            {
                if (this.currentAiming < 1)
                    this.currentAiming += 0.05;
                else
                {
                    if (this.currentAiming != 1)
                        this.currentAiming = 1;
                }
            }
            else
            {
                if (this.currentAiming > 0)
                    this.currentAiming -= 0.05;
                else
                {
                    if (this.currentAiming != 0)
                        this.currentAiming = 0;
                }
            }
            GL11.glTranslatef(1.0F, 0.1F + -.75f * this.currentAiming, .4F + -1.225f * this.currentAiming);
            GL11.glScalef(.2f + .1f * this.currentAiming, .2f + .1f * this.currentAiming,
                    .2f + .1f * this.currentAiming);
            GL11.glRotatef(90 - 5 * this.currentAiming, 0, 1, 0);
            GL11.glRotated(2 * this.currentAiming, 0, 0, 1);
            GL11.glRotatef(-40, 1, 0, 0);
        }
        else if (type.equals(ItemRenderType.ENTITY))
        {
            GL11.glTranslatef(0F, -0.3F, 0F);
            GL11.glScalef(0.2f, 0.2f, 0.2f);
            GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        }
        else if (type.equals(ItemRenderType.INVENTORY))
        {
            GL11.glTranslatef(0F, -.7F, 0F);
            GL11.glScalef(0.2f, 0.2f, 0.2f);
            GL11.glRotatef(-45.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);
        }

        this.modelWeapon.renderAll();

        GL11.glPopMatrix();
    }
}