package fr.craftechmc.needs.common.properties;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.environment.common.biomes.BiomeGenCTBase;
import fr.craftechmc.needs.common.CraftechNeeds;
import fr.craftechmc.needs.common.messages.NeedsMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Created by arisu on 23/06/2016.
 */
public class NeedsProperties implements IExtendedEntityProperties
{
    public static final String         NBT_TAG_NAME           = "CraftechNeeds";
    public static final DamageSource   THIRST_DAMAGE_SOURCE   = new DamageSource("craftechneeds.thirst");
    public static final DamageSource   BLEEDING_DAMAGE_SOURCE = new DamageSource("craftechneeds.bleeding");
    public static final DamageSource   TOXICITY_DAMAGE_SOURCE = new DamageSource("craftechneeds.toxicity");

    private final SaturatedProperty    hunger;
    private final SaturatedProperty    thirst;
    private final StateProperty        legFracture;
    private final StateProperty        bleeding;
    private final LeveledStateProperty toxicity;

    // Related player
    private final EntityPlayer         player;

    private NeedsProperties(final EntityPlayer player)
    {
        this.player = player;
        this.hunger = new SaturatedProperty(player, DamageSource.starve, 20.0, 20.0, 0.00083333333333333, 40);
        this.thirst = new SaturatedProperty(player, NeedsProperties.THIRST_DAMAGE_SOURCE, 20.0, 20.0,
                0.00111111111111111, 40);
        this.legFracture = new StateProperty(player).addEffect(new PotionEffect(Potion.moveSlowdown.getId(), 0, 1));
        this.bleeding = new StateProperty(player);
        this.toxicity = new LeveledStateProperty(player, 5.0).addEffect(3,
                new PotionEffect(Potion.moveSlowdown.getId(), 0, 1));
    }

    /**
     * Ticks and calculates when to decay Must be called each game tick
     * (PlayerTickEvent)
     *
     * @return
     */
    public void tick()
    {
        double hungerAdditionalAmount = 0;
        double thirstAdditionalAmount = 0;

        // Sprinting
        if (this.player.isSprinting())
        {
            hungerAdditionalAmount += 0.0125;
            thirstAdditionalAmount += 0.016666666666666;
        }

        // Swimming
        if (this.player.isInWater() && this.player.distanceWalkedModified - this.player.prevDistanceWalkedModified > 0)
        {
            hungerAdditionalAmount += 0.003125;
            thirstAdditionalAmount += 0.002222222222222;
        }

        final BiomeGenBase biome = this.player.worldObj.getBiomeGenForCoords((int) this.player.posX,
                (int) this.player.posZ);
        // Biome temperature
        final float temperature = biome.temperature;
        if (temperature < 0.5)
            hungerAdditionalAmount += (1 - (temperature - 0.5)) / 0.1 * 8.3333333334E-4;
        else if (temperature > 0.5)
            thirstAdditionalAmount += (temperature - 0.5) / 0.1 * 8.3333333334E-4;

        // Toxicity
        double targetLevel = 1.0;
        if (biome instanceof BiomeGenCTBase)
            targetLevel = ((BiomeGenCTBase) biome).getToxicityLevel();

        final double diff = this.toxicity.getLevel() - targetLevel;
        double modifier = diff * 0.0016666666666666668;
        if (modifier > 0 && modifier < 2.0833333333333335E-4)
        {
            modifier = 2.0833333333333335E-4;
            if (this.toxicity.getLevel() - modifier < targetLevel)
                modifier = diff;
        }
        else if (modifier <= 0 && modifier > -2.0833333333333335E-4)
        {
            modifier = -2.0833333333333335E-4;
            if (this.toxicity.getLevel() - modifier > targetLevel)
                modifier = diff;
        }
        this.toxicity.setLevel(this.toxicity.getLevel() - modifier);

        this.hunger.tick();
        this.hunger.decay(hungerAdditionalAmount);
        this.thirst.tick();
        this.thirst.decay(thirstAdditionalAmount);

        this.legFracture.tick();
        this.bleeding.tick();
        this.toxicity.tick();

        if (this.bleeding.getRemainingTicks() % 150 == 1)
            this.player.attackEntityFrom(NeedsProperties.BLEEDING_DAMAGE_SOURCE, 0.25f);
        if (this.toxicity.getLevel() >= 4.0 && this.toxicity.getRemainingTicks() == 0)
            this.player.attackEntityFrom(NeedsProperties.TOXICITY_DAMAGE_SOURCE, 0.5f);

        if (this.legFracture.isActive() || this.toxicity.getLevel() >= 2.0)
            this.player.getFoodStats().addStats(1 - this.player.getFoodStats().getFoodLevel(), 20); // Stops
                                                                                                    // the
                                                                                                    // player
                                                                                                    // from
                                                                                                    // sprinting
        else
            this.player.getFoodStats().addStats(10 - this.player.getFoodStats().getFoodLevel(), 20);// Prevents
                                                                                                    // the
                                                                                                    // player
                                                                                                    // from
                                                                                                    // starving
                                                                                                    // from
                                                                                                    // vanilla
                                                                                                    // hunger

        // if (legFracture.isActive() && bleeding.getRemainingTicks() == 0)
        // bleeding.setRemainingTicks(150);
        if (this.toxicity.getRemainingTicks() <= 0 && this.toxicity.getLevel() >= 4.0)
            this.toxicity.setRemainingTicks((int) (200 - 195 * (-4.0 + this.toxicity.getLevel())));
    }

    public static NeedsProperties get(final EntityPlayer player)
    {
        IExtendedEntityProperties props;
        if ((props = player.getExtendedProperties(NeedsProperties.NBT_TAG_NAME)) == null)
        {
            props = new NeedsProperties(player);
            player.registerExtendedProperties(NeedsProperties.NBT_TAG_NAME, props);
        }
        return (NeedsProperties) props;
    }

    /**
     * Called to send changes from server to client
     */
    @SideOnly(Side.SERVER)
    public void sync()
    {
        CraftechNeeds.network.sendTo(new NeedsMessage(this.player), (EntityPlayerMP) this.player);
    }

    @Override
    public void saveNBTData(final NBTTagCompound compound)
    {
        final NBTTagCompound properties = new NBTTagCompound();
        compound.setTag(NeedsProperties.NBT_TAG_NAME, properties);

        this.hunger.save(properties, "hunger");
        this.thirst.save(properties, "thirst");
        this.legFracture.save(properties, "legFracture");
        this.bleeding.save(properties, "bleeding");
        this.toxicity.save(properties, "toxicity");
    }

    @Override
    public void loadNBTData(final NBTTagCompound compound)
    {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(NeedsProperties.NBT_TAG_NAME);

        if (properties == null)
        {
            this.saveNBTData(compound);
            properties = (NBTTagCompound) compound.getTag(NeedsProperties.NBT_TAG_NAME);
        }

        this.hunger.load(properties, "hunger");
        this.thirst.load(properties, "thirst");
        this.legFracture.load(properties, "legFracture");
        this.bleeding.load(properties, "bleeding");
        this.toxicity.load(properties, "toxicity");
    }

    @Override
    public void init(final Entity entity, final World world)
    {
    }

    public SaturatedProperty getHunger()
    {
        return this.hunger;
    }

    public SaturatedProperty getThirst()
    {
        return this.thirst;
    }

    public StateProperty getLegFracture()
    {
        return this.legFracture;
    }

    public StateProperty getBleeding()
    {
        return this.bleeding;
    }

    public LeveledStateProperty getToxicity()
    {
        return this.toxicity;
    }

    public EntityPlayer getPlayer()
    {
        return this.player;
    }
}