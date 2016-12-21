package fr.craftechmc.handweapons.items;

import com.google.common.collect.Multimap;
import fr.craftechmc.handweapons.objects.HandWeaponsDescriptor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWeaponDescribed extends Item
{
    private HandWeaponsDescriptor descriptor;

    public ItemWeaponDescribed()
    {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    public HandWeaponsDescriptor getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(HandWeaponsDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return descriptor.getDurability();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase entity1)
    {
        if (entity.worldObj.isRemote)
            return true;
        stack.damageItem(1, entity1);
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    @Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
                new AttributeModifier(field_111210_e, "Weapon modifier", descriptor.getDamage(), 0));
        return multimap;
    }
}
