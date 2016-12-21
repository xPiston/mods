package fr.craftechmc.zombie.common.sound;

import fr.craftechmc.zombie.common.CraftechZombie;

public class SoundList
{
    public static void registerSounds()
    {
        CraftechZombie.soundManager.registerSound("step.stone", 1.5);
        CraftechZombie.soundManager.registerSound("step.wood", 1.5);
    }
}
