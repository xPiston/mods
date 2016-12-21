package fr.craftechmc.weaponry.items;

/**
 * Created by arisu on 04/09/2016.
 */
public class HandWeaponDescriptor
{
    private String         name;
    private String         textureName;
    private String         modelName;
    private double         damages;
    private int            actionTime;
    private int            reloadTime;
    private EWeaponEffects effects;
    private boolean        canParade;

    public HandWeaponDescriptor()
    {
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getTextureName()
    {
        return this.textureName;
    }

    public void setTextureName(final String textureName)
    {
        this.textureName = textureName;
    }

    public String getModelName()
    {
        return this.modelName;
    }

    public void setModelName(final String modelName)
    {
        this.modelName = modelName;
    }

    public double getDamages()
    {
        return this.damages;
    }

    public void setDamages(final double damages)
    {
        this.damages = damages;
    }

    public int getActionTime()
    {
        return this.actionTime;
    }

    public void setActionTime(final int actionTime)
    {
        this.actionTime = actionTime;
    }

    public int getReloadTime()
    {
        return this.reloadTime;
    }

    public void setReloadTime(final int reloadTime)
    {
        this.reloadTime = reloadTime;
    }

    public EWeaponEffects getEffects()
    {
        return this.effects;
    }

    public void setEffects(final EWeaponEffects effects)
    {
        this.effects = effects;
    }

    public boolean isCanParade()
    {
        return this.canParade;
    }

    public void setCanParade(final boolean canParade)
    {
        this.canParade = canParade;
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
        result = prime * result + (this.modelName == null ? 0 : this.modelName.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + this.reloadTime;
        result = prime * result + (this.textureName == null ? 0 : this.textureName.hashCode());
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
        final HandWeaponDescriptor other = (HandWeaponDescriptor) obj;
        if (this.actionTime != other.actionTime)
            return false;
        if (this.canParade != other.canParade)
            return false;
        if (Double.doubleToLongBits(this.damages) != Double.doubleToLongBits(other.damages))
            return false;
        if (this.effects != other.effects)
            return false;
        if (this.modelName == null)
        {
            if (other.modelName != null)
                return false;
        }
        else if (!this.modelName.equals(other.modelName))
            return false;
        if (this.name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.reloadTime != other.reloadTime)
            return false;
        if (this.textureName == null)
        {
            if (other.textureName != null)
                return false;
        }
        else if (!this.textureName.equals(other.textureName))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "HandWeaponDescriptor [name=" + this.name + ", textureName=" + this.textureName + ", modelName="
                + this.modelName + ", damages=" + this.damages + ", actionTime=" + this.actionTime + ", reloadTime="
                + this.reloadTime + ", effects=" + this.effects + ", canParade=" + this.canParade + "]";
    }
}