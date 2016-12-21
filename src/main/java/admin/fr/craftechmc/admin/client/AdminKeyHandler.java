package fr.craftechmc.admin.client;

import org.lwjgl.input.Keyboard;
import org.yggard.brokkgui.wrapper.BrokkGuiManager;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author Ourten 28 oct. 2016
 */
public class AdminKeyHandler
{
    private static final String[] desc      = { "key.button.admin.center" };
    private static final int[]    keyValues = { Keyboard.KEY_U };
    private final KeyBinding[]    keys;

    public AdminKeyHandler()
    {
        this.keys = new KeyBinding[AdminKeyHandler.desc.length];
        for (int i = 0; i < AdminKeyHandler.desc.length; ++i)
        {
            this.keys[i] = new KeyBinding(AdminKeyHandler.desc[i], AdminKeyHandler.keyValues[i],
                    "key.button.categorie");
            ClientRegistry.registerKeyBinding(this.keys[i]);
        }
    }

    @SubscribeEvent
    public void onKeyInput(final KeyInputEvent event)
    {
        if (Minecraft.getMinecraft().inGameHasFocus)
        {
            if (GameSettings.isKeyDown(this.keys[0]))
            {
                Minecraft.getMinecraft().setIngameFocus();
                Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiAdmin()));
            }
        }
    }
}