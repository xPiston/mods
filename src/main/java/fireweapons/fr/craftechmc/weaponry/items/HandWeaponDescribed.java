package fr.craftechmc.weaponry.items;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.needs.common.properties.NeedsProperties;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

/**
 * Created by arisu on 04/09/2016.
 */
public class HandWeaponDescribed extends ItemSword
{
    private final double         damages;
    private final int            actionTime;
    private final int            reloadTime;
    private final EWeaponEffects effects;
    private final boolean        canParade;

    public HandWeaponDescribed(final String unlocalizedName, final String textureName, final double damages,
            final int actionTime, final int reloadTime, final EWeaponEffects effects, final boolean canParade)
    {
        super(ToolMaterial.WOOD);
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(textureName);
        this.damages = damages;
        this.actionTime = actionTime;
        this.reloadTime = reloadTime;
        this.effects = effects;
        this.canParade = canParade;

        this.setCreativeTab(CreativeTabs.tabCombat);
        this.maxStackSize = 1;
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Multimap getItemAttributeModifiers()
    {
        final Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
                new AttributeModifier(Item.itemModifierUUID, "Weapon modifier", this.damages, 0));
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack p_77659_1_, final World p_77659_2_, final EntityPlayer p_77659_3_)
    {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack itemStack)
    {
        return this.canParade ? EnumAction.block : EnumAction.none;
    }

    @Override
    public boolean hitEntity(final ItemStack p_77644_1_, final EntityLivingBase p_77644_2_,
            final EntityLivingBase p_77644_3_)
    {
        if (p_77644_2_ instanceof EntityPlayer)
        {
            final EntityPlayer player = (EntityPlayer) p_77644_2_;
            final NeedsProperties properties = NeedsProperties.get(player);
            switch (this.effects)
            {
                case BLEEDING:
                    properties.getBleeding().setRemainingTicks(6000);
                    break;
                case LEG_FRACTURE:
                    properties.getLegFracture().setRemainingTicks(-1);
                    break;
                case BLEEDING_FRACTURE:
                    properties.getBleeding().setRemainingTicks(6000);
                    properties.getLegFracture().setRemainingTicks(-1);
                    break;
                default:
                    break;
            }
            if (FMLCommonHandler.instance().getEffectiveSide().isServer())
                properties.sync();
        }
        return super.hitEntity(p_77644_1_, p_77644_2_, p_77644_3_);
    }

    public double getDamages()
    {
        return this.damages;
    }

    public int getActionTime()
    {
        return this.actionTime;
    }

    public int getReloadTime()
    {
        return this.reloadTime;
    }

    public EWeaponEffects getEffects()
    {
        return this.effects;
    }

    public boolean isCanParade()
    {
        return this.canParade;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.actionTime;
        result = prime * result + (this.canParade ? 1231 : 1237);
        long temp;
        temp = Double.doubleToLongBits(this.damages);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (this.effects == null ? 0 : this.effects.hashCode());
        result = prime * result + this.reloadTime;
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final HandWeaponDescribed other = (HandWeaponDescribed) obj;
        if (this.actionTime != other.actionTime)
            return false;
        if (this.canParade != other.canParade)
            return false;
        if (Double.doubleToLongBits(this.damages) != Double.doubleToLongBits(other.damages))
            return false;
        if (this.effects != other.effects)
            return false;
        if (this.reloadTime != other.reloadTime)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "HandWeaponDescribed [damages=" + this.damages + ", actionTime=" + this.actionTime + ", reloadTime="
                + this.reloadTime + ", effects=" + this.effects + ", canParade=" + this.canParade + "]";
    }
}