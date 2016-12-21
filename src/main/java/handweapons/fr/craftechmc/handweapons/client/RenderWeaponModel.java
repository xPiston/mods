package fr.craftechmc.handweapons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.opengl.GL11;

public class RenderWeaponModel implements IItemRenderer
{
    private float            rotationX, rotationY, rotationZ;
    private float            inventoryRotationX, inventoryRotationY, inventoryRotationZ;
    private float            lootRotationX, lootRotationY, lootRotationZ;
    private float            thirdRotationX, thirdRotationY, thirdRotationZ;
    private float            scale           = 1f;
    private float            inventoryScale  = 1f;
    private float            lootScale       = 1f;
    private float            xInventoryPos, yInventoryPos;
    private boolean          useTransparency = false;
    private ResourceLocation texturePath;
    private MutablePair<String, IModelCustom> model3D;

    public RenderWeaponModel(String model, String texture)
    {
        model3D = new MutablePair<String, IModelCustom>();
        model3D.setLeft(model);
        texturePath = new ResourceLocation("craftech-handweapons", texture + ".png");
    }

    public RenderWeaponModel setScale(float scale)
    {
        this.scale = scale;
        return this;
    }

    public RenderWeaponModel setRotation(float x, float y, float z)
    {
        rotationX = x;
        rotationY = y;
        rotationZ = z;
        return this;
    }

    public RenderWeaponModel setLootRotation(float x, float y, float z)
    {
        lootRotationX = x;
        lootRotationY = y;
        lootRotationZ = z;
        return this;
    }

    public RenderWeaponModel setThirdPersonRotation(float x, float y, float z)
    {
        thirdRotationX = x;
        thirdRotationY = y;
        thirdRotationZ = z;
        return this;
    }

    public RenderWeaponModel setInventoryRotation(float x, float y, float z)
    {
        inventoryRotationX = x;
        inventoryRotationY = y;
        inventoryRotationZ = z;
        return this;
    }

    public RenderWeaponModel setTransparent()
    {
        useTransparency = true;
        return this;
    }

    public RenderWeaponModel setInventoryScale(float scale)
    {
        inventoryScale = scale;
        return this;
    }

    public RenderWeaponModel setInventoryPos(float posX, float posY)
    {
        xInventoryPos = posX;
        yInventoryPos = posY;
        return this;
    }

    public RenderWeaponModel setLootScale(float scale)
    {
        lootScale = scale;
        return this;
    }

    public void renderModel()
    {
        if (this.model3D.right != null)
            this.model3D.getRight().renderAll();
        else
            this.model3D.setRight(AdvancedModelLoader
                    .loadModel(new ResourceLocation("craftechhandweapons", this.model3D.getLeft() + ".obj")));
    }

    public void renderEquipped()
    {
        GL11.glPushMatrix();
        if (useTransparency)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        else
            GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(0.45f, 0.0f, -0.1f);
        GL11.glRotatef(-170F, 1F, 0F, 0F);
        GL11.glRotatef(-20f, 0f, 1f, 1f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        GL11.glRotatef(thirdRotationX, 1f, 0f, 0f);
        GL11.glRotatef(thirdRotationY, 0f, 1f, 0f);
        GL11.glRotatef(thirdRotationZ, 0f, 0f, 1f);
        GL11.glScalef(scale, scale, scale);
        Minecraft.getMinecraft().renderEngine.bindTexture(texturePath);
        this.renderModel();
        if (useTransparency)
            GL11.glDisable(GL11.GL_BLEND);
        else
            GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    public void renderEquippedFirstPerson()
    {
        GL11.glPushMatrix();
        if (useTransparency)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        else
            GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(1f, 0.2f, 0f);
        GL11.glRotatef(-90F, 1F, 0F, 0F);
        GL11.glRotatef(-130, 0f, 1f, 0f);
        GL11.glRotatef(90f, 0f, 0f, 1f);
        GL11.glRotatef(rotationX, 1f, 0f, 0f);
        GL11.glRotatef(rotationY, 0f, 1f, 0f);
        GL11.glRotatef(rotationZ, 0f, 0f, 1f);
        GL11.glScalef(scale, scale, scale);
        Minecraft.getMinecraft().renderEngine.bindTexture(texturePath);
        this.renderModel();
        if (useTransparency)
            GL11.glDisable(GL11.GL_BLEND);
        else
            GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    public void renderInInventory()
    {
        GL11.glPushMatrix();
        if (useTransparency)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        else
            GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(8f + xInventoryPos, 8f + yInventoryPos, 0f);
        GL11.glRotatef(-20F, 1F, 0F, 0F);
        GL11.glRotatef(-45F, 0f, -1F, 0F);
        GL11.glRotatef(180f, 0f, 1f, 0f);
        GL11.glRotatef(inventoryRotationX, 1f, 0f, 0f);
        GL11.glRotatef(inventoryRotationY, 0f, 1f, 0f);
        GL11.glRotatef(inventoryRotationZ, 0f, 0f, 1f);
        GL11.glScalef(16f * inventoryScale, 16f * inventoryScale, 16f * inventoryScale);
        Minecraft.getMinecraft().renderEngine.bindTexture(texturePath);
        this.renderModel();
        if (useTransparency)
            GL11.glDisable(GL11.GL_BLEND);
        else
            GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    public void renderEntity()
    {
        GL11.glPushMatrix();
        if (useTransparency)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        else
            GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(lootScale, lootScale, lootScale);
        GL11.glRotatef(180f, 0f, 0f, 1f);
        GL11.glRotatef(lootRotationX, 1f, 0f, 0f);
        GL11.glRotatef(lootRotationY, 0f, 1f, 0f);
        GL11.glRotatef(lootRotationZ, 0f, 0f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(texturePath);
        this.renderModel();
        if (useTransparency)
            GL11.glDisable(GL11.GL_BLEND);
        else
            GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type)
        {
            case EQUIPPED:
                return true;
            case EQUIPPED_FIRST_PERSON:
                return true;
            case INVENTORY:
                return true;
            case ENTITY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case EQUIPPED:
            {
                renderEquipped();
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                renderEquippedFirstPerson();
                break;
            }
            case INVENTORY:
            {
                renderInInventory();
                break;
            }
            case ENTITY:
            {
                renderEntity();
                break;
            }
            default:
                break;
        }
    }
}