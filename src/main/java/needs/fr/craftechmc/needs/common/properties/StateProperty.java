package fr.craftechmc.needs.common.properties;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

/**
 * Created by arisu on 28/06/2016.
 */
public class StateProperty extends TimedProperty
{
    private ArrayList<PotionEffect> effects = new ArrayList<>();

    protected StateProperty(EntityPlayer player)
    {
        super(player);
    }

    // BUILDER //
    public StateProperty addEffect(PotionEffect effect)
    {
        this.effects.add(effect);
        return this;
    }
    // END BUILDER //

    @Override
    void update()
    {
        if (isActive())
            effects.forEach(player::addPotionEffect);
        else
            effects.forEach(effect -> player.removePotionEffect(effect.getPotionID()));
    }
}
