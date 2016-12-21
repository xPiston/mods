package fr.craftechmc.environment.common.utils;

import java.util.Collection;
import java.util.LinkedList;
import fr.craftechmc.core.math.Vector3d;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Generic util class; TODO: refactor into multiple util classes
 * Created by arisu on 24/07/2016.
 */
public class Util
{
    public static Vector3d readCoordinates(NBTTagCompound compound, String tagName)
    {
        return new Vector3d(compound.getDouble(tagName + "_X"), compound.getDouble(tagName + "_Y"),
                compound.getDouble(tagName + "_Z"));
    }

    public static void writeCoordinates(NBTTagCompound compound, String tagName, Vector3d coords)
    {
        writeCoordinates(compound, tagName, coords.getX(), coords.getY(), coords.getZ());
    }

    public static void writeCoordinates(NBTTagCompound compound, String tagName, double x, double y, double z)
    {
        compound.setDouble(tagName + "_X", x);
        compound.setDouble(tagName + "_Y", y);
        compound.setDouble(tagName + "_Z", z);
    }

    /**
     * @param a The first value
     * @param b The second value
     * @param d The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    public static double interpolate(double a, double b, double d)
    {
        return a + (b - a) * d;
    }

    public static boolean collectionHasVector(Collection<Vector3d> vectors, Vector3d v)
    {
        for (Vector3d cv : vectors)
            if (cv.getX() == v.getX() && cv.getY() == v.getY() && cv.getZ() == v.getZ())
                return true;
        return false;
    }

    public static int collectionIndexOfVector(LinkedList<Vector3d> vectors, Vector3d v)
    {
        for (Vector3d cv : vectors)
            if (cv.getX() == v.getX() && cv.getY() == v.getY() && cv.getZ() == v.getZ())
                return vectors.indexOf(cv);
        return -1;
    }
}
