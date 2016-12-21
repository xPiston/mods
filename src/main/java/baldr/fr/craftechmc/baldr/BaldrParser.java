package fr.craftechmc.baldr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import fr.craftechmc.baldr.client.BaldrResourcePack;
import fr.craftechmc.baldr.resource.objects.BaldrContentDescriptor;
import fr.craftechmc.baldr.resource.objects.BaldrIndex;

@SuppressWarnings("deprecation")
public class BaldrParser
{
    private static volatile BaldrParser instance = null;
    private final BaldrStorage          storage;
    private final Gson                  GSON     = new Gson();

    public int                          maxContents;
    public int                          maxResources;

    private BaldrParser()
    {
        this.storage = BaldrStorage.getInstance();
    }

    public static final BaldrParser getInstance()
    {
        if (BaldrParser.instance == null)
            synchronized (BaldrParser.class)
            {
                if (BaldrParser.instance == null)
                    BaldrParser.instance = new BaldrParser();
            }
        return BaldrParser.instance;
    }

    public void parseDataClient() throws JsonSyntaxException, MalformedURLException, IOException
    {
        final BaldrIndex index = this.GSON
                .fromJson(IOUtils.toString(new URL(BaldrVars.getResourceURL() + "index.json")), BaldrIndex.class);

        for (final String name : index.getContentDescriptors())
        {
            final BaldrContentDescriptor descriptor = this.GSON.fromJson(
                    IOUtils.toString(new URL(BaldrVars.getResourceURL() + name + ".json")),
                    BaldrContentDescriptor.class);
            if (Loader.isModLoaded(descriptor.getRequiredMod()))
                this.storage.getDescriptors().add(descriptor);
        }

        ProgressBar bar = null;
        final ArrayList<Pair<String, String>> contentToDownload = Lists.newArrayList();
        final ArrayList<Pair<String, String>> resourcesToDownload = Lists.newArrayList();
        bar = ProgressManager.push("Descriptor Downloading", this.storage.getDescriptors().size());
        for (final BaldrContentDescriptor descriptor : this.storage.getDescriptors())
        {
            bar.step(descriptor.getFolderName());

            for (final String cFile : descriptor.getContentFiles().keySet())
                if (!this.storage.isContentFileValid(descriptor.getFolderName(), cFile,
                        descriptor.getContentFiles().get(cFile)))
                    contentToDownload.add(Pair.of(descriptor.getFolderName(), cFile));
            for (final String rFile : descriptor.getResourceFiles().keySet())
                if (!this.storage.isResourceFileValid(descriptor.getFolderName(), rFile,
                        descriptor.getResourceFiles().get(rFile)))
                    resourcesToDownload.add(Pair.of(descriptor.getFolderName(), rFile));
        }

        this.maxContents = contentToDownload.size();
        this.maxResources = resourcesToDownload.size();

        final ExecutorService contentExecutor = Executors.newFixedThreadPool(10);

        ProgressManager.pop(bar);

        final ProgressBar filesBar = ProgressManager.push("Files downloading", 2);

        contentToDownload.forEach(pair -> contentExecutor
                .execute(this.storage.getDownloadRunnable(EBaldrFile.CONTENT, pair.getLeft(), pair.getRight())));

        contentExecutor.shutdown();
        try
        {
            contentExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (final InterruptedException e)
        {
            e.printStackTrace();
        }

        filesBar.step("Content files");
        final ExecutorService resourceExecutor = Executors.newFixedThreadPool(10);

        resourcesToDownload.forEach(pair -> resourceExecutor
                .execute(this.storage.getDownloadRunnable(EBaldrFile.RESOURCE, pair.getLeft(), pair.getRight())));

        resourceExecutor.shutdown();
        try
        {
            resourceExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (final InterruptedException e)
        {
            e.printStackTrace();
        }
        filesBar.step("Resource files");

        ProgressManager.pop(filesBar);
        this.storage.getDescriptors()
                .forEach(descriptor -> BaldrResourcePack.resourceDomains.add(descriptor.getFolderName()));
    }
}