package fr.craftechmc.core.math;

/**
 * Vector3d is an implementation of a three dimensionnal vectors using double as
 * values. This object is mutable. Every operations can be chained and the same
 * object will be returned.
 *
 * @author Ourten
 */
public class Vector3d
{
    public static Vector3d from(final Vector3d vector3d)
    {
        return new Vector3d(vector3d);
    }

    public static Vector3d from(final double x, final double y, final double z)
    {
        return new Vector3d(x, y, z);
    }

    private double x, y, z;

    public Vector3d(final double x, final double y, final double z)
    {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(final Vector3d vector3d)
    {
        this(vector3d.x, vector3d.y, vector3d.z);
    }

    public Vector3d add(final double x, final double y, final double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3d add(final Vector3d vector3d)
    {
        return this.add(vector3d.x, vector3d.y, vector3d.z);
    }

    public Vector3d substract(final double x, final double y, final double z)
    {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3d substract(final Vector3d vector3d)
    {
        return this.substract(vector3d.x, vector3d.y, vector3d.z);
    }

    public Vector3d normalize()
    {
        final double length = this.getLength();

        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public Vector3d crossprod(final double x, final double y, final double z)
    {
        this.x = this.y * z - this.z * y;
        this.y = this.z * x - this.x * z;
        this.z = this.x * y - this.y * x;
        return this;
    }

    public Vector3d crossprod(final Vector3d vector3d)
    {
        return this.crossprod(vector3d.x, vector3d.y, vector3d.z);
    }

    public Vector3d scale(final double scale)
    {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }

    public double getLength()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double getMagnitude()
    {
        return this.getLength();
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public void setX(final double x)
    {
        this.x = x;
    }

    public void setY(final double y)
    {
        this.y = y;
    }

    public void setZ(final double z)
    {
        this.z = z;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = prime * result + (int) (temp ^ temp >>> 32);
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
        final Vector3d other = (Vector3d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Vector3d [x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
    }
}