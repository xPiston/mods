package fr.craftechmc.zombie.common.entity.ai;

import java.util.List;

import fr.craftechmc.zombie.common.entity.EntityZombieCraftech;
import fr.craftechmc.zombie.common.entity.EntityZombieCraftechBase;
import fr.craftechmc.zombie.common.sound.SoundEntry;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

/**
 * Created by Phenix246 on 15/07/2016.
 */
public class CraftechEntityAINearestAttackableTarget extends EntityAITarget
{
    private float                 warningStep;
    private float                 locateStep;
    private final Class           targetClass;
    private final int             targetChance;

    /**
     * This filter is applied to the Entity search. Only matching entities will
     * be targetted. (null -> no restrictions)
     */
    private final IEntitySelector targetEntitySelector;
    private EntityLivingBase      targetEntity;

    public CraftechEntityAINearestAttackableTarget(EntityZombieCraftechBase entity, Class target, int targetChance,
            boolean shouldCheckSight, float warningStep, float locateStep)
    {
        this(entity, target, targetChance, shouldCheckSight, false, warningStep, locateStep);
    }

    public CraftechEntityAINearestAttackableTarget(EntityZombieCraftechBase entity, Class target, int targetChance,
            boolean shouldCheckSight, boolean nearbyOnly, float warningStep, float locateStep)
    {
        this(entity, target, targetChance, shouldCheckSight, nearbyOnly, (IEntitySelector) null, warningStep,
                locateStep);
    }

    public CraftechEntityAINearestAttackableTarget(EntityZombieCraftechBase entity, Class target, int targetChance,
            boolean shouldCheckSight, boolean nearbyOnly, IEntitySelector selector, float warningStep, float locateStep)
    {
        super(entity, shouldCheckSight, nearbyOnly);
        this.targetClass = target;
        this.targetChance = targetChance;
        this.setMutexBits(1);
        this.targetEntitySelector = new IEntitySelector()
        {
            /**
             * Return whether the specified entity is applicable to this filter.
             */
            public boolean isEntityApplicable(Entity entity)
            {
                return !(entity instanceof EntityLivingBase) ? false
                        : (selector != null && !selector.isEntityApplicable(entity) ? false
                                : CraftechEntityAINearestAttackableTarget.this
                                        .isSuitableTarget((EntityLivingBase) entity, false));
            }
        };
        this.warningStep = warningStep;
        this.locateStep = locateStep;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
            List<SoundEntry> list = ((EntityZombieCraftech) this.taskOwner).getHeadsSound();
            list.removeIf((entry) -> !targetEntitySelector.isEntityApplicable(entry.sender));
            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                this.targetEntity = (EntityLivingBase) list.get(0).sender;
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
