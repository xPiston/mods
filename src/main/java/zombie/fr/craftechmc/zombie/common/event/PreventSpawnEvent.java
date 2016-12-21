package fr.craftechmc.zombie.common.event;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftechmc.zombie.common.entity.ISpawnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * Created by Phenix246 on 20/06/2016.
 */
public class PreventSpawnEvent
{
    @SubscribeEvent
    public void preventSpawn(LivingSpawnEvent event)
    {
        if (!(event.entityLiving instanceof EntityPlayer || event.entityLiving instanceof ISpawnable))
            event.setResult(Result.DENY);
    }
}
