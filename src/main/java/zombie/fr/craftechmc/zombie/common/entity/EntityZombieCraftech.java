package fr.craftechmc.zombie.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

/**
 * Created by Phenix246 on 20/06/2016.
 */
public class EntityZombieCraftech extends EntityZombieCraftechBase
{

    public EntityZombieCraftech(World world)
    {
        super(world, 1, 2);

        this.setSize(0.6F, 1.8F);
    }

    @Override
    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    }

}
