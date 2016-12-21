package fr.craftechmc.baldr.client;

import java.lang.reflect.Field;
import java.util.List;

import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftechmc.baldr.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;

public class ClientProxy extends CommonProxy
{
    @Override
    public void makeSidedOps()
    {
        super.makeSidedOps();

        /*
         * Disabled since the project is no longer hosted on our machines.
         */
        // Multi Providers
        // @SuppressWarnings("unchecked")
        // final List<String> args = (ArrayList<String>)
        // Launch.blackboard.get("ArgumentList");
        // if (args.contains("--baldr-provider"))
        // BaldrVars.BASE_PROVIDER = args.get(args.indexOf("--baldr-provider") +
        // 1);
        //
        // Baldr.logger.log(Level.INFO, "[Check] Current URL : " +
        // BaldrVars.getResourceURL());
        //
        // try
        // {
        // BaldrParser.getInstance().parseDataClient();
        // } catch (JsonSyntaxException | IOException e)
        // {
        // e.printStackTrace();
        // }
        this.addFakePack();
    }

    @SuppressWarnings("unchecked")
    public void addFakePack()
    {
        final Field f = ReflectionHelper.findField(Minecraft.class, "field_110449_ao", "ao", "defaultResourcePacks");
        List<IResourcePack> res = null;

        try
        {
            res = (List<IResourcePack>) f.get(Minecraft.getMinecraft());
        } catch (final IllegalArgumentException e)
        {
            e.printStackTrace();
        } catch (final IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if (res != null)
            res.add(new BaldrResourcePack());
    }
}