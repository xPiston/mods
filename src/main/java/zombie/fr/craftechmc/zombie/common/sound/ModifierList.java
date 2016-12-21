package fr.craftechmc.zombie.common.sound;

import fr.craftechmc.zombie.common.CraftechZombie;

public class ModifierList
{
    public static void registerModifier()
    {
        CraftechZombie.soundManager.registerModifier(SPRINT);
    }

    public static final ISoundValueModifier SPRINT = (sender, name, value) ->
    {
        if (name.startsWith("step") && sender.isSprinting())
            value = SoundModifierHandler.getSprintValue(value);
        return value;
    };

}
