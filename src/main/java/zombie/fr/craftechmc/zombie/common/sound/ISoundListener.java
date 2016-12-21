package fr.craftechmc.zombie.common.sound;

import net.minecraft.entity.Entity;

public interface ISoundListener
{
    void processSound(Entity sender, double soundValue);
}
