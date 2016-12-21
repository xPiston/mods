package fr.craftechmc.environment.common.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by arisu on 04/08/2016.
 */
public class WorldProviderCT extends WorldProviderSurface
{
    private long daynightCycleDuration = 120000L;
    private long dayDuration           = 72000L;
    private long nightDuration         = 48000L;

    private long currentTotalTime = 0L;

    public static void overrideDefault()
    {
        DimensionManager.unregisterProviderType(DimensionManager.getProviderType(0));
        DimensionManager.registerProviderType(0, WorldProviderCT.class, true);
    }

    @Override
    public void setWorldTime(long time)
    {
        this.currentTotalTime = time;
        this.worldObj.getWorldInfo().setWorldTime(time);
    }

    @Override
    public float calculateCelestialAngle(long totalWorldTime, float partialTicks)
    {
        float progress = (float) f(currentTotalTime, dayDuration, nightDuration, partialTicks) - 0.25f;
        if (progress < 0)
            progress++;
        return progress;
    }

    @Override
    public int getMoonPhase(long worldTime)
    {
        float progress = (float) (currentTotalTime % (8 * daynightCycleDuration)) / daynightCycleDuration;
        if (progress < 0)
            progress = 7;
        if (progress > 7)
            progress = 0;
        return (int) progress;
    }

    public double f(long currentWorldTotalTime, long dayDuration, long nightDuration, float partialTicks)
    {
        long totalTime = dayDuration + nightDuration;
        double currentDayTime = currentWorldTotalTime % totalTime + partialTicks;
        if (currentDayTime < dayDuration)
            return (currentDayTime / dayDuration) / 2;
        else
            return ((currentDayTime - dayDuration) / nightDuration) / 2 + 0.5;
    }

    // Handle the colors
    @SideOnly(Side.CLIENT)
    @Override
    public float[] calcSunriseSunsetColors(float angleRatio, float p_76560_2_)
    {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(angleRatio * (float) Math.PI * 2.0F) - 0.0F;
        float f4 = -0.0F;

        if (f3 >= f4 - f2 && f3 <= f4 + f2)
        {
            float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float) Math.PI)) * 0.99F;
            f6 *= f6;
            return new float[] { 156 / 255f, 201 / 255f, 141 / 255f, f6 };
        }
        else
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
    {
        float f2 = MathHelper.cos(p_76562_1_ * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        float f3 = 156 / 255f;
        float f4 = 201 / 255f;
        float f5 = 141 / 255f;
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return Vec3.createVectorHelper(f3, f4, f5);
    }
}
