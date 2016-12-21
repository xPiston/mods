package fr.craftechmc.weaponry.server;

import java.util.ArrayList;

public class WeaponFireSystem
{
    private static final ArrayList<String> userFiring = new ArrayList<String>();
    private static final ArrayList<Long>   lastFire   = new ArrayList<Long>();
    private static final ArrayList<String> userReloading = new ArrayList<String>();
    private static final ArrayList<Long>   lastReload   = new ArrayList<Long>();

    public static final void setFiring(String user)
    {
        WeaponFireSystem.userFiring.add(user);
        WeaponFireSystem.lastFire.add(0L);
    }

    public static final void removeFiring(String user)
    {
        WeaponFireSystem.lastFire.remove(WeaponFireSystem.userFiring.indexOf(user));
        WeaponFireSystem.userFiring.remove(user);
    }

    public static final boolean isFiring(String user)
    {
        return WeaponFireSystem.userFiring.contains(user);
    }

    public static final long getLastFire(String user)
    {
        return WeaponFireSystem.lastFire.get(WeaponFireSystem.userFiring.indexOf(user));
    }

    public static final void setLastFire(String user, long time)
    {
        WeaponFireSystem.lastFire.set(WeaponFireSystem.userFiring.indexOf(user), time);
    }
    
    public static final void setReload(String user)
    {
        WeaponFireSystem.userReloading.add(user);
        WeaponFireSystem.lastReload.add(0L);
    }
    
    public static final void removeReloading(String user)
    {
        WeaponFireSystem.lastReload.remove(WeaponFireSystem.userReloading.indexOf(user));
        WeaponFireSystem.userReloading.remove(user);
    }
    
    public static final boolean isReload(String user)
    {
        return WeaponFireSystem.userReloading.contains(user);
    }
    
    public static final long getLastReload(String user)
    {
        return WeaponFireSystem.lastReload.get(WeaponFireSystem.userReloading.indexOf(user));
    }
    
    public static final void setLastReload(String user, long time)
    {
        WeaponFireSystem.lastReload.set(WeaponFireSystem.userReloading.indexOf(user), time);
    }
}