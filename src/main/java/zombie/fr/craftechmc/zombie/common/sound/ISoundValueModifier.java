package fr.craftechmc.zombie.common.sound;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface ISoundValueModifier
{
    double modify(Entity sender, String name, double value);
}
