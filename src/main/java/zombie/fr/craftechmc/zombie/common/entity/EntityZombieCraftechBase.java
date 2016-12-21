package fr.craftechmc.zombie.common.entity;

import java.util.List;

import com.google.common.collect.Lists;

import fr.craftechmc.zombie.common.entity.ai.CraftechEntityAINearestAttackableTarget;
import fr.craftechmc.zombie.common.sound.ISoundListener;
import fr.craftechmc.zombie.common.sound.SoundEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by Phenix246 on 24/07/2016.
 */
public abstract class EntityZombieCraftechBase extends EntityMob implements ISoundListener, ISpawnable
{
    private float            warningStep;
    private float            locateStep;

    private List<SoundEntry> heardSound;

    public EntityZombieCraftechBase(World world, float warningStep, float locateStep)
    {
        super(world);
        this.heardSound = Lists.newArrayList();
        this.warningStep = warningStep;
        this.locateStep = locateStep;

        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new CraftechEntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true,
                warningStep, locateStep));
        this.setSize(0.6F, 1.8F);
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    protected Entity findPlayerToAttack()
    {
        return null;
    }

    public float getWarningStep()
    {
        return this.warningStep;
    }

    public float getLocateStep()
    {
        return this.locateStep;
    }

    @Override
    public void processSound(Entity sender, double soundValue)
    {

        if (heardSound.size() == 0 || heardSound.get(0).getInterest() < soundValue)
        {
            heardSound.add(0, new SoundEntry(sender, soundValue));
        }
        else
        {
            heardSound.add(new SoundEntry(sender, soundValue));
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        int index = 0;

        for (int i = 0; i < heardSound.size(); i++)
        {
            SoundEntry entry = heardSound.get(index);

            // update interest
            entry.update();

            // remove if interest is negative
            if (entry.getInterest() < 0)
                heardSound.remove(index);
            else
                index++;
        }

    }

    public List<SoundEntry> getHeadsSound()
    {
        return this.heardSound;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damage, float amount)
    {
        if (!super.attackEntityFrom(damage, amount))
            return false;
        else
        {
            EntityLivingBase entitylivingbase = this.getAttackTarget();

            if (entitylivingbase == null && this.getEntityToAttack() instanceof EntityLivingBase)
                entitylivingbase = (EntityLivingBase) this.getEntityToAttack();

            if (entitylivingbase == null && damage.getEntity() instanceof EntityLivingBase)
                entitylivingbase = (EntityLivingBase) damage.getEntity();

            return true;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        final boolean flag = super.attackEntityAsMob(entity);

        if (flag)
        {
            final int i = this.worldObj.difficultySetting.getDifficultyId();

            if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < i * 0.3F)
                entity.setFire(2 * i);
        }

        return flag;
    }

}
