package fr.craftechmc.needs.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.wrapper.GuiRenderer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftechmc.needs.common.properties.LeveledStateProperty;
import fr.craftechmc.needs.common.properties.NeedsProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Created by arisu on 22/06/2016.
 */
public class NeedsHud extends Gui
{
    private final Minecraft        mc;
    private EntityPlayer           player;
    private NeedsProperties        props;
    private ScaledResolution       res         = null;

    private final ResourceLocation hotbarSlot;
    private final ResourceLocation hotbarSlotActiveBar;

    private final ResourceLocation healthIcon;
    private final ResourceLocation healthBarEmpty;
    private final ResourceLocation healthBarFull;
    private final ResourceLocation healthBarWarning;

    private final ResourceLocation hungerIcon;
    private final ResourceLocation hungerBarEmpty;
    private final ResourceLocation hungerBarFull;
    private final ResourceLocation hungerBarWarning;

    private final ResourceLocation thirstIcon;
    private final ResourceLocation thirstBarEmpty;
    private final ResourceLocation thirstBarFull;
    private final ResourceLocation thirstBarWarning;

    private final ResourceLocation toxicityIcon;
    private final ResourceLocation toxicityBarEmpty;
    private final ResourceLocation toxicityBarFull;
    private final ResourceLocation toxicityBarWarning;

    private final ResourceLocation weaponBarEmpty;
    private final ResourceLocation weaponBarFull;
    private final ResourceLocation weaponBarWarning;

    private final ResourceLocation statusEffectBackground;
    private final ResourceLocation statusBleedingIcon;
    private final ResourceLocation statusBrokenLegIcon;

    private final ResourceLocation smallBarSaturated;
    private final GuiRenderer      guiRenderer = new GuiRenderer(Tessellator.instance);

    public NeedsHud()
    {
        this.mc = Minecraft.getMinecraft();

        this.hotbarSlot = new ResourceLocation("craftechneeds:images/slot.png");
        this.hotbarSlotActiveBar = new ResourceLocation("craftechneeds:images/active_slot_bar.png");

        this.healthIcon = new ResourceLocation("craftechneeds:images/health_icon.png");
        this.healthBarEmpty = new ResourceLocation("craftechneeds:images/health_bar_empty.png");
        this.healthBarFull = new ResourceLocation("craftechneeds:images/health_bar_full.png");
        this.healthBarWarning = new ResourceLocation("craftechneeds:images/health_bar_warning.png");

        this.hungerIcon = new ResourceLocation("craftechneeds:images/hunger_icon.png");
        this.hungerBarEmpty = new ResourceLocation("craftechneeds:images/hunger_bar_empty.png");
        this.hungerBarFull = new ResourceLocation("craftechneeds:images/hunger_bar_full.png");
        this.hungerBarWarning = new ResourceLocation("craftechneeds:images/hunger_bar_warning.png");

        this.thirstIcon = new ResourceLocation("craftechneeds:images/thirst_icon.png");
        this.thirstBarEmpty = new ResourceLocation("craftechneeds:images/thirst_bar_empty.png");
        this.thirstBarFull = new ResourceLocation("craftechneeds:images/thirst_bar_full.png");
        this.thirstBarWarning = new ResourceLocation("craftechneeds:images/thirst_bar_warning.png");

        this.toxicityIcon = new ResourceLocation("craftechneeds:images/toxic_icon.png");
        this.toxicityBarEmpty = new ResourceLocation("craftechneeds:images/toxicity_bar_empty.png");
        this.toxicityBarFull = new ResourceLocation("craftechneeds:images/toxicity_bar_full.png");
        this.toxicityBarWarning = new ResourceLocation("craftechneeds:images/toxicity_bar_warning.png");

        this.weaponBarEmpty = new ResourceLocation("craftechneeds:images/weapon_load_bar_empty.png");
        this.weaponBarFull = new ResourceLocation("craftechneeds:images/weapon_load_bar_full.png");
        this.weaponBarWarning = new ResourceLocation("craftechneeds:images/weapon_load_bar_warning.png");

        this.statusEffectBackground = new ResourceLocation("craftechneeds:images/status_effect_background.png");
        this.statusBleedingIcon = new ResourceLocation("craftechneeds:images/bleeding_status_icon.png");
        this.statusBrokenLegIcon = new ResourceLocation("craftechneeds:images/broken_leg_status_icon.png");

        this.smallBarSaturated = new ResourceLocation("craftechneeds:images/small_bar_shiny.png");
    }

    @SubscribeEvent
    public void onPlayerJoin(final EntityJoinWorldEvent event)
    {
        if (event.entity.equals(this.mc.thePlayer))
        {
            final NeedsProperties props = NeedsProperties.get((EntityPlayer) event.entity);
            this.props = props;
            this.player = (EntityPlayer) event.entity;
        }
    }

    @SubscribeEvent
    public void onOverlayUpdate(final RenderGameOverlayEvent event)
    {
        if (!this.mc.thePlayer.capabilities.isCreativeMode)
            switch (event.type)
            {
                case HEALTH:
                    this.mc.gameSettings.heldItemTooltips = false;
                    this.drawHud();
                    // fall through (we cancel those 4 draws, but also health.
                    // The goal is to draw our hud at the
                    // original vanilla health drawing time)
                case FOOD:
                case EXPERIENCE:
                case AIR:
                case HOTBAR:
                    event.setCanceled(true);
                    break;
                default:
                    break;
            }
    }

    private void drawHud()
    {
        this.res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int width = this.res.getScaledWidth();
        final int height = this.res.getScaledHeight();
        final LeveledStateProperty toxicity = this.props.getToxicity();

        // Don't let the light destroy our splendid textures
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        // Textures scale. We have to adapt to the default minecraft resolution
        // scale.
        final double scale = 0.45f;

        // Draw effects (State properties)
        int i = 0;
        if (this.props.getBleeding().isActive())
        {
            this.drawEffect(width, height, 16, 24, this.statusBleedingIcon, "Bleeding", i, 0.65f);
            i++;
        }
        if (this.props.getLegFracture().isActive())
            this.drawEffect(width, height, 36, 30, this.statusBrokenLegIcon, "Fracture", i, 0.65f);
        // i++;

        // Animate function TODO: replace with some great framework functions
        final long modulo = System.currentTimeMillis() % 1000;
        final float warningAnimate = modulo >= 500 ? (1000 - modulo) / 500f : modulo / 500f;

        // Draw life
        this.drawProperty(width, 0, height - 100 * scale, 526, 30, scale,
                this.player.getHealth() / this.player.getMaxHealth(), 0f,
                this.player.getHealth() <= 0.25f * this.player.getMaxHealth() ? warningAnimate : 0f, 18, 4f, 0f, 9,
                this.healthIcon, this.healthBarEmpty, this.healthBarFull, this.healthBarWarning, null);

        // Draw Hunger
        this.drawProperty(width, -(258 + 5) * scale / 2, height - 100 * scale - (8 + 11) * scale, 258, 8, scale,
                this.props.getHunger().getLevel() / this.props.getHunger().getMaxLevel(),
                this.props.getHunger().getSaturation() / this.props.getHunger().getMaxSaturation(),
                this.props.getHunger().getLevel() <= 0.25f * this.props.getHunger().getMaxLevel() ? warningAnimate : 0,
                16, 4, 2, 4, this.hungerIcon, this.hungerBarEmpty, this.hungerBarFull, this.hungerBarWarning,
                this.smallBarSaturated);

        // Draw Thirst
        this.drawProperty(width, (258 + 5) * scale / 2, height - 100 * scale - (8 + 11) * scale, 258, 8, scale,
                (float) (this.props.getThirst().getLevel() / this.props.getThirst().getMaxLevel()),
                (float) (this.props.getThirst().getSaturation() / this.props.getThirst().getMaxSaturation()),
                this.props.getThirst().getLevel() <= 0.25f * this.props.getThirst().getMaxLevel() ? warningAnimate : 0,
                16, 4, 2, 4, this.thirstIcon, this.thirstBarEmpty, this.thirstBarFull, this.thirstBarWarning,
                this.smallBarSaturated);

        // Draw toxicity
        this.drawVerticalProperty(width, (526 / 2f + 17) * scale, height - (96 + 5) * scale, 8, 96, scale,
                toxicity.getLevel() / toxicity.getMaxLevel(), toxicity.getLevel() >= 4.0f ? warningAnimate : 0, 4, 16,
                16, this.toxicityIcon, this.toxicityBarEmpty, this.toxicityBarFull, this.toxicityBarWarning);

        // Draw weapon thing
        this.drawVerticalProperty(width, (526 / 2f + 41) * scale, height - (96 + 5) * scale, 8, 96, scale, 0.5, 0, 4,
                16, 16, null, this.weaponBarEmpty, this.weaponBarFull, this.weaponBarWarning);

        // Draw hotbar
        this.drawHotbar(width, height - 26.5, scale);
    }

    private void drawEffect(final int screenWidth, final int screenHeight, final int iconWidth, final int iconHeight,
            final ResourceLocation icon, final String label, final int offset, final double scale)
    {
        final IGuiHelper guiHelper = BrokkGuiPlatform.getInstance().getGuiHelper();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.mc.renderEngine.bindTexture(this.statusEffectBackground);
        guiHelper.drawTexturedModalRect(this.guiRenderer, screenWidth - 128 * scale,
                screenHeight - (125 + 64 * offset) * scale, 0, 0, 1, 1, 128 * scale, 48 * scale, 0);

        this.mc.renderEngine.bindTexture(icon);
        guiHelper.drawTexturedModalRect(this.guiRenderer, screenWidth - (128 - 24 + iconWidth / 2) * scale,
                screenHeight - (125 + 64 * offset - 24 + iconHeight / 2) * scale, 0, 0, 1, 1, iconWidth * scale,
                iconHeight * scale, 0);

        this.drawString(this.mc.fontRendererObj, label,
                (int) (screenWidth - (128 - 86) * scale - this.mc.fontRendererObj.getStringWidth(label) / 2),
                (int) (screenHeight - (125 + 64 * offset - 24) * scale - this.mc.fontRendererObj.FONT_HEIGHT / 2),
                0xffffffff);

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawVerticalProperty(final int screenWidth, final double xOffset, final double y, final double width,
            final double height, final double scale, final double propertyLevel, final float warning,
            final double warningOffset, final int iconSize, final double iconYOffset, final ResourceLocation icon,
            final ResourceLocation barEmpty, final ResourceLocation barFull, final ResourceLocation barWarning)
    {
        final IGuiHelper guiHelper = BrokkGuiPlatform.getInstance().getGuiHelper();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (propertyLevel < 1.0f)
        {
            this.mc.renderEngine.bindTexture(barEmpty);
            guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - width * scale / 2, y, 0, 0, 1,
                    1, width * scale, height * scale, 0);
        }

        if (propertyLevel > 0)
        {
            this.mc.renderEngine.bindTexture(barFull);
            guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - width * scale / 2,
                    y + height * (1 - propertyLevel) * scale, 0, 1 - propertyLevel, 1, 1, width * scale,
                    height * propertyLevel * scale, 0);
        }

        if (warning > 0)
        {
            this.mc.renderEngine.bindTexture(barWarning);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, warning);
            guiHelper.drawTexturedModalRect(this.guiRenderer,
                    xOffset + screenWidth / 2 - width * scale / 2 - warningOffset * scale, y - warningOffset * scale, 0,
                    0, 1, 1, (width + 2 * warningOffset) * scale, (height + 2 * warningOffset) * scale, 0);
        }

        if (icon != null)
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.renderEngine.bindTexture(icon);
            guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - iconSize * scale / 2,
                    y + iconYOffset * scale - iconSize * scale / 2, 0, 0, 1, 1, iconSize * scale, iconSize * scale, 0);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawProperty(final int screenWidth, final double xOffset, final double y, final double width,
            final double height, final double scale, final double propertyLevel, final double propertySaturation,
            final float warning, final int iconSize, final double iconXOffset, final double saturationOffset,
            final double warningOffset, final ResourceLocation icon, final ResourceLocation barEmpty,
            final ResourceLocation barFull, final ResourceLocation barWarning, final ResourceLocation barSaturated)
    {
        final IGuiHelper guiHelper = BrokkGuiPlatform.getInstance().getGuiHelper();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (propertyLevel < 1.0f)
        {
            this.mc.renderEngine.bindTexture(barEmpty);
            guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - width * scale / 2, y, 0, 0, 1,
                    1, width * scale, height * scale, 0);
        }

        if (propertyLevel > 0)
        {
            this.mc.renderEngine.bindTexture(barFull);
            guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - width * scale / 2, y, 0, 0,
                    propertyLevel, 1, width * propertyLevel * scale, height * scale, 0);
        }

        if (propertySaturation > 0)
        {
            this.mc.renderEngine.bindTexture(barSaturated);
            guiHelper.drawTexturedModalRect(this.guiRenderer,
                    xOffset + screenWidth / 2 - width * scale / 2 - saturationOffset * scale,
                    y - saturationOffset * scale, 0, 0, propertySaturation, 1,
                    (width + 2 * saturationOffset) * propertySaturation * scale,
                    (height + 2 * saturationOffset) * scale, 0);
        }

        if (warning > 0)
        {
            this.mc.renderEngine.bindTexture(barWarning);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, warning);
            guiHelper.drawTexturedModalRect(this.guiRenderer,
                    xOffset + screenWidth / 2 - width * scale / 2 - warningOffset * scale, y - warningOffset * scale, 0,
                    0, 1, 1, (width + 2 * warningOffset) * scale, (height + 2 * warningOffset) * scale, 0);
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(icon);
        guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - width * scale / 2 + iconXOffset,
                y + height * scale / 2 - iconSize * scale / 2, 0, 0, 1, 1, iconSize * scale, iconSize * scale, 0);

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawHotbar(final int screenWidth, final double y, final double scale)
    {
        for (int i = 0; i < 9; i++)
            this.drawHotbarSlot(screenWidth, i * (51 + 10) * scale, y, 53, 51, scale, i);
    }

    private void drawHotbarSlot(final int screenWidth, final double xOffset, final double y, final double width,
            final double height, final double scale, final int slot)
    {
        final IGuiHelper guiHelper = BrokkGuiPlatform.getInstance().getGuiHelper();
        // Don't let the light destroy our splendid textures
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.mc.renderEngine.bindTexture(this.hotbarSlot);
        guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - 269.5 * scale, y, 0, 0, 1, 1,
                width * scale, height * scale, 0);
        if (this.player.inventory.currentItem == slot)
        {
            this.mc.renderEngine.bindTexture(this.hotbarSlotActiveBar);
            guiHelper.drawTexturedModalRect(this.guiRenderer, xOffset + screenWidth / 2 - 269.5 * scale - 2 * scale,
                    y + 52 * scale, 0, 0, 1, 1, (width + 4) * scale, 2 * scale, 0);
        }

        final ItemStack item = this.player.inventory.getStackInSlot(slot);

        GL11.glDisable(GL11.GL_BLEND);

        if (item != null)
        {
            GL11.glPushMatrix();

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240 / 1.0F, 240 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            RenderItem.getInstance().renderItemAndEffectIntoGUI(item.getItem().getFontRenderer(item),
                    this.mc.getTextureManager(), item,
                    (int) (xOffset + screenWidth / 2 - 269.5 * scale + 25.5 * scale - 16 * scale),
                    (int) (y + 25.5 * scale - 16 * scale));

            RenderHelper.disableStandardItemLighting();
            GL11.glPopMatrix();

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            final FontRenderer font = this.mc.fontRendererObj;
            final String string = "" + item.stackSize;
            font.drawStringWithShadow(string,
                    (int) (xOffset + screenWidth / 2 - 269.5 * scale + 51 * scale) - font.getStringWidth(string),
                    (int) (y + 51 * scale) - font.FONT_HEIGHT, 0xffffff);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }
}
