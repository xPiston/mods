package fr.craftechmc.tracking.client.gui;

import org.lwjgl.input.Mouse;
import org.yggard.brokkgui.gui.BrokkGuiScreen;

import fr.craftechmc.tracking.CraftechTracking;
import fr.craftechmc.tracking.TrackingManager;
import fr.craftechmc.tracking.TrackingPacketConstants;
import net.minecraft.util.ResourceLocation;

public class GuiStats extends BrokkGuiScreen
{
    private final ResourceLocation BACKGROUND = new ResourceLocation(CraftechTracking.MODASSETS,
            "textures/gui/background.png");
    private final ResourceLocation BUTTON     = new ResourceLocation(CraftechTracking.MODASSETS,
            "textures/gui/button.png");

    private String                 selected   = "";
    private final int              size       = 0;
    private float                  currentScroll;
    private byte                   lastUpdate = 0;

    public GuiStats()
    {

    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float f)
    {
        this.lastUpdate++;
        if (this.lastUpdate >= 20)
        {
            this.lastUpdate = 0;
            CraftechTracking.network.send().packet(TrackingPacketConstants.TRACKING_REQUEST).toServer();
        }
        // this.mc.renderEngine.bindTexture(BACKGROUND);
        // this.drawTexturedModalRect(width / 4, height / 4, 0, 0, width / 2,
        // height / 2);

        // System.out.println(mouseX+" | "+mouseY);
        super.render(mouseX, mouseY, f);
        // int i = -1, j = 0;
        // if (selected == "")
        // {
        // size = TrackingManager.getInstance().players.size();
        // for (String key : TrackingManager.getInstance().players.keySet())
        // {
        // i++;
        // if (i < (size * currentScroll) || i > (size * currentScroll) + 6)
        // continue;
        // j++;
        // if (TrackingManager.getInstance().players.get(key).getLastJoinDate()
        // != 0)
        // {
        // this.mc.renderEngine.bindTexture(BUTTON);
        // GL11.glColor3f(.4f, 1, .4f);
        // this.drawTexturedModalRect(width / 4 + 4, (height / 4) - 8 + (16 *
        // j), 0, 0, width / 2 - 8, 16);
        // float bps = (float)
        // TrackingManager.getInstance().players.get(key).getCurrentSession()
        // / ((System.currentTimeMillis()
        // - TrackingManager.getInstance().players.get(key).getLastJoinDate()) /
        // 1000);
        // this.drawString(this.fontRendererObj,
        // key + " | "
        // + TrackingManager.getInstance()
        // .formatTime(System.currentTimeMillis()
        // - TrackingManager.getInstance().players.get(key).getLastJoinDate())
        // + " | " +
        // TrackingManager.getInstance().players.get(key).getCurrentSession() +
        // " blocks ["
        // + new DecimalFormat("##.##").format(bps) + " bps]", width / 4 + 8,
        // (height / 4) - 4 + (16 * j), 14737632);
        // }
        // else
        // {
        // Pair<Long, Integer> lastSession =
        // TrackingManager.getInstance().players.get(key).getSessions()
        // .get(TrackingManager.getInstance().players.get(key).getSessions().size()
        // - 1);
        // this.mc.renderEngine.bindTexture(BUTTON);
        // GL11.glColor3f(.6f, .6f, .6f);
        // this.drawTexturedModalRect(width / 4 + 4, (height / 4) - 8 + (16 *
        // j), 0, 0, width / 2 - 8, 16);
        // float bps = (float) lastSession.getRight() / ((lastSession.getLeft())
        // / 1000);
        // this.drawString(this.fontRendererObj,
        // key + " | " +
        // TrackingManager.getInstance().formatTime(lastSession.getLeft()) + " |
        // "
        // + lastSession.getRight() + " blocks [" + new
        // DecimalFormat("##.##").format(bps)
        // + " bps]",
        // width / 4 + 8, (height / 4) - 4 + (16 * j), 14737632);
        // }
        // }
        // }
        // else
        // {
        // if (TrackingManager.getInstance().players.containsKey(selected))
        // {
        // PlayerData data =
        // TrackingManager.getInstance().players.get(selected);
        //
        // this.mc.renderEngine.bindTexture(BUTTON);
        // GL11.glColor3f(.4f, .4f, 1);
        // this.drawTexturedModalRect(width / 4 + 4, (height / 4) + 8 + (16 *
        // j), 0, 0, width / 2 - 8, 16);
        // this.drawString(this.fontRendererObj, " < -- Back", width / 4 + 8,
        // (height / 4) + 12 + (16 * j),
        // 14737632);
        // i++;
        // if (data.getLastJoinDate() != 0)
        // {
        // j++;
        // this.mc.renderEngine.bindTexture(BUTTON);
        // GL11.glColor3f(.4f, 1, .4f);
        // this.drawTexturedModalRect(width / 4 + 4, (height / 4) + 8 + (16 *
        // j), 0, 0, width / 2 - 8, 16);
        // float bps = (float) data.getCurrentSession()
        // / ((System.currentTimeMillis() - data.getLastJoinDate()) / 1000);
        // this.drawString(this.fontRendererObj,
        // selected + " | "
        // + TrackingManager.getInstance().formatTime(
        // System.currentTimeMillis() - data.getLastJoinDate())
        // + " | " + data.getCurrentSession() + " blocks ["
        // + new DecimalFormat("##.##").format(bps) + " bps]",
        // width / 4 + 8, (height / 4) + 12 + (16 * j), 14737632);
        // i++;
        // }
        // size = data.getSessions().size();
        // for (Pair<Long, Integer> session : data.getSessions())
        // {
        // i++;
        // if ((i - 1) < (size * currentScroll) || i > (size * currentScroll) +
        // 6)
        // continue;
        // j++;
        // if (session.getRight() != 0)
        // {
        // this.mc.renderEngine.bindTexture(BUTTON);
        // GL11.glColor3f(1, 1, .4f);
        // this.drawTexturedModalRect(width / 4 + 4, (height / 4) + 8 + (16 *
        // j), 0, 0, width / 2 - 8, 16);
        // float bps = (float) session.getRight() / ((session.getLeft()) /
        // 1000);
        // this.drawString(this.fontRendererObj,
        // selected + " | " +
        // TrackingManager.getInstance().formatTime(session.getLeft()) + " | "
        // + session.getRight() + " blocks [" + new
        // DecimalFormat("##.##").format(bps)
        // + " bps]",
        // width / 4 + 8, (height / 4) + 12 + (16 * j), 14737632);
        // }
        // }
        // }
        // }
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            final int j = this.size;

            if (i > 0)
                i = 1;
            else
                i = -1;
            this.currentScroll = (float) (this.currentScroll - (double) i / (double) j);

            if (this.currentScroll < 0.0F)
                this.currentScroll = 0.0F;
            if (this.size != 0)
            {
                if (this.currentScroll > (float) (this.size - 6) / this.size)
                    this.currentScroll = (float) (this.size - 6) / this.size;
            }
        }
    }

    @Override
    public void onClick(final int mouseX, final int mouseY, final int i)
    {
        super.onClick(mouseX, mouseY, i);

        if (mouseX > this.getWidth() / 4 + 4 && mouseX < this.getWidth() / 4 + 4 + this.getWidth() / 2 - 8)
        {
            if (mouseY > this.getHeight() / 4 + 8
                    && mouseY < this.getHeight() / 4 + 8 + 16 * TrackingManager.getInstance().players.size())
            {
                if (this.selected.equals(""))
                    this.selected = (String) TrackingManager.getInstance().players.keySet()
                            .toArray()[(int) ((mouseY - this.getHeight() / 4 + 8) / 16 + this.size * this.currentScroll
                                    - 1)];
                else
                {
                    if ((mouseY - this.getHeight() / 4 + 8) / 16 - 1 == 0)
                        this.selected = "";
                }
                this.currentScroll = 0;
            }
        }
    }
}