package fr.craftechmc.weaponry.client.settings;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.weaponry.CraftechWeaponry;
import fr.craftechmc.weaponry.network.ReloadPacket;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class WeaponryModSettings
{
    public static final KeyBinding keyReload = new KeyBinding("Recharger", Keyboard.KEY_R, "Weapon");
    private static WeaponryModSettings INSTANCE;

    private WeaponryModSettings() {}

    public static WeaponryModSettings getSettings() {
        if(INSTANCE == null) {
            INSTANCE = new WeaponryModSettings();
        }
        return INSTANCE;
    }
    
    public void init()
    {
        ClientRegistry.registerKeyBinding(keyReload);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(keyReload.isPressed()) 
        {  
            CraftechWeaponry.packetPipeline.sendToServer(new ReloadPacket());
        }
    }

}
