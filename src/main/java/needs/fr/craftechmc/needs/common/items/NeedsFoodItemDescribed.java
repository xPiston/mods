package fr.craftechmc.needs.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

/**
 * Created by arisu on 25/06/2016.
 */
public class NeedsFoodItemDescribed extends ItemFood implements INeedsFoodItem
{
    public double         saturation;
    public double         level;
    public ENeedsItemType type;

    public NeedsFoodItemDescribed(final String unlocalizedName, final double saturation, final double level,
            final ENeedsItemType type)
    {
        super((int) level, (int) saturation, false);
        this.saturation = saturation;
        this.level = level;
        this.type = type;
        this.setUnlocalizedName(unlocalizedName);

        // This is to bypass minecraft's default hunger / food system
        this.setAlwaysEdible();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumAction getItemUseAction(final ItemStack item)
    {
        return this.type.equals(ENeedsItemType.FOOD) ? EnumAction.eat : EnumAction.drink;
    }

    @Override
    public double getSaturation()
    {
        return this.saturation;
    }

    @Override
    public double getLevel()
    {
        return this.level;
    }

    @Override
    public ENeedsItemType getType()
    {
        return this.type;
    }
}