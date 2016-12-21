package fr.craftechmc.baldr;

public class BaldrVars
{
    public static final String MODID         = "baldr";
    public static final String MODNAME       = "Baldr";
    public static final String MODASSETS     = BaldrVars.MODID;
    public static final String MODVERSION    = "0.2-alpha";

    public static String       BASE_URL      = "http://baldr.craftechmc.fr/";
    public static String       BASE_PROVIDER = "master";

    public static String getResourceURL()
    {
        return BaldrVars.BASE_URL + BaldrVars.BASE_PROVIDER + "/";
    }
}