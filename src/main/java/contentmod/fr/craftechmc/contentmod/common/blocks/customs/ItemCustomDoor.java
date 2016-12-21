package fr.craftechmc.contentmod.common.blocks.customs;

import fr.craftechmc.core.common.tab.CTabManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Custom Door Item placer Created by Thog the 10/07/2016
 */
public class ItemCustomDoor extends ItemDoor
{
    private final Block theDoor;

    public ItemCustomDoor(final BlockCustomDoor theDoor)
    {
        super(theDoor.getMaterial());
        this.theDoor = theDoor;
        this.setTextureName(theDoor.getTextureName());
        this.setCreativeTab(CTabManager.getTab("CONTENT_DECLINAISON"));
    }

    @Override
    public boolean onItemUse(final ItemStack itemStack, final EntityPlayer entityPlayer, final World world, final int x,
            int y, final int z, final int side, final float hitX, final float hitY, final float hitZ)
    {
        if (side != 1)
        {
            return false;
        }
        else
        {
            ++y;

            if (entityPlayer.canPlayerEdit(x, y, z, side, itemStack)
                    && entityPlayer.canPlayerEdit(x, y + 1, z, side, itemStack))
            {
                if (!this.theDoor.canPlaceBlockAt(world, x, y, z))
                {
                    return false;
                }
                else
                {
                    final int i1 = MathHelper.floor_double((entityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D)
                            & 3;
                    ItemDoor.placeDoorBlock(world, x, y, z, i1, this.theDoor);
                    --itemStack.stackSize;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
