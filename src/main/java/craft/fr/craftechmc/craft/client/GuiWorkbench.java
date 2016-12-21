package fr.craftechmc.craft.client;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.craftechmc.craft.CraftechCraft;
import fr.craftechmc.craft.api.IWorkbenchEntry;
import fr.craftechmc.craft.container.SlotOutput;
import fr.craftechmc.craft.container.WorkbenchContainer;
import fr.craftechmc.craft.network.GuiHandler;
import fr.craftechmc.craft.network.PacketWorkbench;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Desc... Created by Thog the 16/04/2016
 */
public class GuiWorkbench extends GuiContainer
{
    private static final ResourceLocation WORKBENCH = new ResourceLocation("craftechcraft",
            "textures/gui/workbench_table.png");
    private static final ResourceLocation UPGRADE   = new ResourceLocation("craftechcraft",
            "textures/gui/upgrade_table.png");
    private final WorkbenchContainer      container;
    private final GuiHandler              handler;
    private int                           workbenchPosID;
    private List<IWorkbenchEntry.Render>  toDisplay;
    private int                           scrollbarY;
    private boolean                       scrollBarSelected;
    private int                           selectedID;

    public GuiWorkbench(final GuiHandler handler, final EntityPlayer player, final World world, final int x,
            final int y, final int z)
    {
        super(new WorkbenchContainer(player, world, x, y, z));
        this.handler = handler;
        this.ySize = 182;
        this.container = (WorkbenchContainer) this.inventorySlots;
        this.selectedID = -1;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partial, final int mouseX, final int mouseY)
    {
        this.mc.getTextureManager()
                .bindTexture(this.container.getTabID() == 0 ? GuiWorkbench.WORKBENCH : GuiWorkbench.UPGRADE);
        this.drawTexturedModalRect(this.calcPosX(this.xSize), this.calcPosY(this.ySize), 0, 0, this.xSize, this.ySize);
        if (this.toDisplay != null && this.container.getTabID() == 0)
        {
            final int max = this.toDisplay.size() < 4 ? this.toDisplay.size() : 4;

            for (int i = max + this.workbenchPosID; i != this.workbenchPosID; i--)
            {
                final int x = this.calcPosX(153) - 3;
                final int y = this.calcPosY(18) + ((i + 2) * 18) - 114;
                final int boxX = mouseX - x;
                final int boxY = mouseY - y;
                final IWorkbenchEntry.Render render = this.toDisplay.get(i - 1);
                if (render == null)
                    break;
                this.mc.getTextureManager().bindTexture(GuiWorkbench.WORKBENCH);
                GL11.glPushMatrix();
                if ((boxX > 0 && boxY > 0 && boxX < 154 && boxY < 19) || this.selectedID == (i - 1))
                    GL11.glColor4f(0.89F, 0.88F, 0.79F, 1.0F);
                else
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(x, y, 0, 182, 153, 18); // START
                this.drawCenteredString(this.fontRendererObj, render.getName(), x + 76, y + 4,
                        new Color(198, 196, 200).getRGB());
                GL11.glPopMatrix();
                this.drawItemStack(render.getIcon(), x, y, render.getName());
            }
            final int maxSize = this.toDisplay.size() < 4 ? this.toDisplay.size() : 3;
            final float f = ((max - 3) / 4F);
            this.renderScrollBar(4.0F / maxSize);
        }
        else if (this.container.getTabID() == 1)
        {
            final SlotOutput output = (SlotOutput) this.container.getUpgradeSlot(3);
            if (output.getOpaqueStack() != null && this.container.getTabID() == 1)
            {
                final ItemStack stack = output.getOpaqueStack();
                final int i = output.xDisplayPosition + this.calcPosX(this.xSize);
                final int j = output.yDisplayPosition + this.calcPosY(this.ySize);
                this.drawItemStack(stack, i, j, "", 0.5);
            }
        }

    }

    private void renderScrollBar(final float ratio)
    {
        // )System.out.println(ratio);
        this.mc.getTextureManager().bindTexture(GuiWorkbench.WORKBENCH);
        this.drawTexturedModalRect(this.calcPosX(175) + 175 - 13, (this.calcPosY(this.ySize) + 21), 0, 200, 6, 21);
    }

    private void drawItemStack(final ItemStack stack, final int x, final int y, final String name)
    {
        this.drawItemStack(stack, x, y, name, 0.0);
    }

    private void drawItemStack(final ItemStack stack, final int x, final int y, final String name, final double opacity)
    {
        GL11.glPushMatrix();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        final short short1 = 240;
        final short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        final float oldZLevel = this.zLevel;
        final float oldItemZLevel = GuiScreen.itemRender.zLevel;
        this.zLevel = 200.0F;
        GuiScreen.itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null)
            font = stack.getItem().getFontRenderer(stack);
        if (font == null)
            font = this.fontRendererObj;
        GuiScreen.itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), stack, x, y);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Gui.drawRect(x, y, x + 16, y + 16, new Color(38, 38, 38, (int) (255 * opacity)).getRGB());
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        this.zLevel = oldZLevel;
        GuiScreen.itemRender.zLevel = oldItemZLevel;
        GL11.glPopMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        this.drawCenteredString(this.fontRendererObj, "Crafting", 43, 4, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Upgrade", 86 + 43, 4, 0xFFFFFF);
    }

    private int calcPosX(final int x)
    {
        return (this.width - x) / 2;
    }

    private int calcPosY(final int y)
    {
        return (this.height - y) / 2;
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int which)
    {
        super.mouseClicked(mouseX, mouseY, which);
        int modalX;
        int modalY;
        int id;

        if (this.container.getTabID() == 0 && this.toDisplay != null)
        {
            modalX = this.calcPosX(153) - 3;
            modalY = this.calcPosY(18) - 6;
            for (id = 4; id != 0 && this.toDisplay.size() > (4 - id); id--)
            {
                final int x = mouseX - (modalX - 3);
                final int y = mouseY - (modalY - ((id - 1) * 18));
                if (x > 0 && y > 0 && x < 154 && y < 19)
                {
                    System.out.println("(CLICK): x:" + x + ", y:" + y);
                    final int realID = (4 - id) + this.workbenchPosID;
                    // TODO: Something more generic?
                    if (this.selectedID == realID)
                    {
                        CraftechCraft.getProxy().pipeline()
                                .sendToServer(new PacketWorkbench((byte) 1, this.toDisplay.get(realID).getIcon()));
                        this.selectedID = -1;
                    }
                    else
                        this.selectedID = realID;

                    break;
                }
            }
        }
        else if (this.container.getTabID() == 1)
        {
            final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
            if (slot instanceof SlotOutput)
            {
                final SlotOutput out = (SlotOutput) slot;
                if (out.getOpaqueStack() != null)
                    CraftechCraft.getProxy().pipeline().sendToServer(new PacketWorkbench((byte) 3));
            }
        }

        modalX = this.calcPosX(88);
        modalY = this.calcPosY(16) - 83;

        for (id = 0; id < 2; id++)
        {
            final int x = mouseX - (modalX - (44 * (id == 0 ? 1 : -1)));
            final int y = mouseY - modalY;
            if (x >= 0 && y >= 0 && x < 89 && y < 17)
            {
                this.container.setTabID(id);
                break;
            }
        }

        modalX = this.calcPosX(175) + 175 - 13;
        modalY = (this.calcPosY(this.ySize) + 21);

        final int x = mouseX - modalX;
        final int y = mouseY - modalY;
        if (x >= 0 && y >= 0 && x < 6 && y < 21)
        {
            this.scrollBarSelected = true;
            System.out.println("Scroll bar selected");
        }

    }

    @Override
    protected void mouseClickMove(final int x, final int y, final int lastButtonClicked, final long timeSinceMouseClick)
    {
        super.mouseClickMove(x, y, lastButtonClicked, timeSinceMouseClick);
        System.out.printf("(MOVE): x: %d, y: %d%n", x, y);
        if (this.scrollBarSelected)
        {

        }
    }

    @Override
    public void updateScreen()
    {
        final byte flag = this.handler.getFlag();
        final SlotOutput output = (SlotOutput) this.container.getUpgradeSlot(3);
        if (flag == 1)
            this.refreshWorkbench((List<IWorkbenchEntry.Render>) this.handler.getData());
        else if (flag == 2)
            output.setOpaqueStack(null, (ItemStack) this.handler.getData());
        super.updateScreen();
    }

    public void refreshWorkbench(final List<IWorkbenchEntry.Render> toDisplay)
    {
        this.workbenchPosID = 0;
        this.toDisplay = toDisplay;
    }

    private Slot getSlotAtPosition(final int p_146975_1_, final int p_146975_2_)
    {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
        {
            final Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);
            if (this.isMouseOverSlot(slot, p_146975_1_, p_146975_2_))
                return slot;
        }

        return null;
    }

    private boolean isMouseOverSlot(final Slot slot, final int p_146981_2_, final int p_146981_3_)
    {
        return this.isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, p_146981_2_,
                p_146981_3_);
    }
}
