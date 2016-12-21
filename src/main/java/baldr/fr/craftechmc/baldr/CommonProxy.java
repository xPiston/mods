package fr.craftechmc.baldr;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.Loader;
import fr.craftechmc.baldr.client.BaldrResourcePack;
import fr.craftechmc.baldr.resource.objects.BaldrContentDescriptor;
import fr.craftechmc.baldr.resource.objects.BaldrIndex;
import fr.craftechmc.core.common.data.DataStorage;
import net.minecraft.launchwrapper.Launch;

public class CommonProxy
{
    public void makeSidedOps()
    {
        // Multi Providers
        @SuppressWarnings("unchecked")
        final List<String> args = (ArrayList<String>) Launch.blackboard.get("ArgumentList");
        if (args.contains("--baldr-provider"))
            BaldrVars.BASE_PROVIDER = args.get(args.indexOf("--baldr-provider") + 1);

        Baldr.logger.log(Level.INFO, "[Check] Current URL : " + BaldrVars.getResourceURL());

        final BaldrStorage provider = BaldrStorage.getInstance();
        final Gson GSON = new Gson();
        BaldrIndex index;
        try
        {
            index = GSON.fromJson(
                    DataStorage.getInstance().getCachedFile("baldr", BaldrVars.BASE_PROVIDER + "/index.json"),
                    BaldrIndex.class);
            for (final String name : index.getContentDescriptors())
            {
                final BaldrContentDescriptor descriptor = GSON.fromJson(DataStorage.getInstance().getCachedFile("baldr",
                        BaldrVars.BASE_PROVIDER + "/" + name + ".json"), BaldrContentDescriptor.class);
                if (Loader.isModLoaded(descriptor.getRequiredMod()))
                    provider.getDescriptors().add(descriptor);
                BaldrResourcePack.resourceDomains.add(descriptor.getFolderName());
            }
        } catch (final JsonSyntaxException e)
        {
            e.printStackTrace();
        }
    }
}
