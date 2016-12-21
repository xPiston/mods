package fr.craftechmc.needs.common.items;

/**
 * Created by arisu on 25/06/2016.
 */
public class ItemDescriptor
{
    private String name;
    private String textureName;
    private String modelName;
    private double saturation;
    private double level;

    public ItemDescriptor()
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

    public double getSaturation()
    {
        return this.saturation;
    }

    public void setSaturation(final double saturation)
    {
        this.saturation = saturation;
    }

    public double getLevel()
    {
        return this.level;
    }

    public void setLevel(final double level)
    {
        this.level = level;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.level);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (this.modelName == null ? 0 : this.modelName.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        temp = Double.doubleToLongBits(this.saturation);
        result = prime * result + (int) (temp ^ temp >>> 32);
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
        final ItemDescriptor other = (ItemDescriptor) obj;
        if (Double.doubleToLongBits(this.level) != Double.doubleToLongBits(other.level))
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
        if (Double.doubleToLongBits(this.saturation) != Double.doubleToLongBits(other.saturation))
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
        return "ItemDescriptor [name=" + this.name + ", textureName=" + this.textureName + ", modelName="
                + this.modelName + ", saturation=" + this.saturation + ", level=" + this.level + "]";
    }
}