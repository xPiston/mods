package fr.craftechmc.needs.client.render;

import fr.craftechmc.baldr.client.BaldrResourcePack;
import fr.craftechmc.contentmod.client.render.ClientModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import org.lwjgl.opengl.GL11;

/**
 * Created by arisu on 25/06/2016.
 */
public class ISRSimpleInventoryModelRenderer implements IItemRenderer
{
    private final Item item;
    private final ResourceLocation texture;
    private final ResourceLocation modelLocation;
    private IModelCustom model;

    public ISRSimpleInventoryModelRenderer(final Item item, final String textureName, final String modelName)
    {
        this.item = item;
        System.out.println(BaldrResourcePack.resourceDomains);
        this.texture = new ResourceLocation("craftechneeds", textureName + ".png");
        this.modelLocation = new ResourceLocation("craftechneeds", modelName + ".obj");
    }

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type)
    {
        if (type.equals(ItemRenderType.EQUIPPED) || type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON) || type
                .equals(ItemRenderType.INVENTORY) || type.equals(ItemRenderType.ENTITY))
            if (this.item.equals(item.getItem()))
                return true;
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
                                         final ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data)
    {

        Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
        GL11.glPushMatrix();
        GL11.glScalef(2f, 2f, 2f);

        switch (type)
        {
            case EQUIPPED:
                GL11.glTranslatef(0.35f, 0.5f, 0.35f);
                break;

            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.3f, 0.5f, 0.3f);
                break;

            case ENTITY:
                GL11.glTranslatef(0f, 0.5f, 0f);
                break;
            default:
                break;
        }
        if (this.model != null)
            this.model.renderAll();
        else
        {
            try
            {
                this.model = AdvancedModelLoader.loadModel(this.modelLocation);
            } catch (ModelFormatException exception) // Error loading model?
            {
                exception.printStackTrace();

                // Apply the errored model because we can't the other (If we can't load it now, it cannot be loaded after)
                this.model = ClientModelManager.getInstance().getErroredModel();
            }
        }

        GL11.glPopMatrix();
    }
}
