package fr.craftechmc.tracking.client;

import org.lwjgl.input.Keyboard;
import org.yggard.brokkgui.wrapper.BrokkGuiManager;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import fr.craftechmc.tracking.client.gui.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class TrackingKeyHandler
{
    /**
     * Key descriptions; use a language file to localize the description later
     */
    private static final String[] desc      = { "key.button.tracking.stats" };
    /** Default key values */
    private static final int[]    keyValues = { Keyboard.KEY_K };
    private final KeyBinding[]    keys;

    public TrackingKeyHandler()
    {
        this.keys = new KeyBinding[TrackingKeyHandler.desc.length];
        for (int i = 0; i < TrackingKeyHandler.desc.length; ++i)
        {
            this.keys[i] = new KeyBinding(TrackingKeyHandler.desc[i], TrackingKeyHandler.keyValues[i],
                    "key.button.categorie");
            ClientRegistry.registerKeyBinding(this.keys[i]);
        }
    }

    /**
     * KeyInputEvent is in the FML package, so we must register to the FML event
     * bus
     */
    @SubscribeEvent
    public void onKeyInput(final KeyInputEvent event)
    {
        if (Minecraft.getMinecraft().inGameHasFocus)
        {
            if (GameSettings.isKeyDown(this.keys[0]))
            {
                Minecraft.getMinecraft().setIngameFocus();
                Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiStats()));
            }
        }
    }
}
