package fr.craftechmc.environment.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;

/**
 * Created by arisu on 03/08/2016.
 */
public class ItemNonDrinkableWaterBucket extends ItemBucket
{
    public ItemNonDrinkableWaterBucket(Block block)
    {
        super(block);
        this.setTextureName("craftechenvironment:non_drinkable_water_bucket");
        this.setUnlocalizedName("nonDrinkableWaterBucket");
    }
}
