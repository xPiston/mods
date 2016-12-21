package fr.craftechmc.loots.client.gui;

import fr.craftechmc.loots.common.CraftechLoots;
import fr.craftechmc.loots.common.gui.ContainerLootContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiLootContainer extends GuiContainer
{
    private final ResourceLocation       TEX_BACKGROUND;
    private final ContainerLootContainer container;

    public GuiLootContainer(final ContainerLootContainer container)
    {
        super(container);
        this.container = container;
        this.TEX_BACKGROUND = new ResourceLocation(CraftechLoots.MODASSETS, "textures/gui/container_background.png");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int x, final int y)
    {
        this.mc.getTextureManager().bindTexture(this.TEX_BACKGROUND);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < this.container.getSlotsCount(); i += 9)
            for (int j = 0; j < Math.min(this.container.getSlotsCount() - i, 9); j++)
                this.drawTexturedModalRect((int) (this.guiLeft + (this.xSize / 2) + (-(4.5 * 18) + (j * 18))),
                        this.guiTop + 15 + (i / 9 * 18), this.xSize, 0, 18, 18);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_)
    {
        this.fontRendererObj.drawString(I18n.format(this.container.getTile().getInventoryName(), new Object[0]), 8, 6,
                4210752);
    }

    public void drawTexturedModalRect(final int xStart, final int yStart, final int uMin, final int vMin,
            final int uMax, final int vMax, final int width, final int height)
    {
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(xStart + 0, yStart + height, this.zLevel, uMin, vMax);
        tessellator.addVertexWithUV(xStart + width, yStart + height, this.zLevel, uMax, vMax);
        tessellator.addVertexWithUV(xStart + width, yStart + 0, this.zLevel, uMax, vMin);
        tessellator.addVertexWithUV(xStart + 0, yStart + 0, this.zLevel, uMin, vMin);
        tessellator.draw();
    }
}