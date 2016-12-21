package fr.craftechmc.zombie.common.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftechmc.zombie.common.CraftechZombie;
import fr.craftechmc.zombie.common.sound.SoundValueHandler;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class SoundEvent
{
    @SubscribeEvent
    public void onPlaySound(PlaySoundAtEntityEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            CraftechZombie.soundManager.sendSound(event.entity, event.name, event.volume, event.pitch);
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            double value = SoundValueHandler.getFallValue(event.entityLiving, event.distance);
            CraftechZombie.soundManager.sendSound(event.entity, value);
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            double value = SoundValueHandler.getJumpValue(event.entityLiving);
            CraftechZombie.soundManager.sendSound(event.entity, value);
        }
    }

}
