package fr.craftechmc.weaponry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.init.Items;

public class CommonProxy
{
    public void preInit(final FMLPreInitializationEvent e)
    {
        CTabManager.addTab("WEAPONRY_FIRE", Items.arrow);
    }

    public void registerRenders()
    {

    }

    public void loadSide()
    {
    }
}