package fr.craftechmc.baldr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

import fr.craftechmc.baldr.resource.objects.BaldrContentDescriptor;
import fr.craftechmc.core.common.data.DataStorage;

public class BaldrStorage
{
    private static volatile BaldrStorage instance = null;

    public static final BaldrStorage getInstance()
    {
        if (BaldrStorage.instance == null)
            synchronized (BaldrStorage.class)
            {
                if (BaldrStorage.instance == null)
                    BaldrStorage.instance = new BaldrStorage();
            }
        return BaldrStorage.instance;
    }

    private final ArrayList<BaldrContentDescriptor> descriptors;

    private final DataStorage                       storage;

    private BaldrStorage()
    {
        this.descriptors = Lists.newArrayList();
        this.storage = DataStorage.getInstance();
    }

    public boolean contentExist(final String domain, final String contentFile)
    {
        return this.storage.cachedFileExist("baldr",
                BaldrVars.BASE_PROVIDER + "/" + domain + "/content/" + contentFile + ".json");
    }

    // REALLY REALLY HACKY
    // TODO: Remove this ASAP
    private void download(final String host, final String path)
    {
        InputStream input = null;
        FileOutputStream writeFile = null;

        try
        {
            final File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            final URL url = new URL(host.replace(" ", "%20"));
            final URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/43.0.2357.81 Chrome/43.0.2357.81 Safari/537.36");
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            input = connection.getInputStream();
            writeFile = new FileOutputStream(path);
            final byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) > 0)
                writeFile.write(buffer, 0, read);
            writeFile.flush();
        } catch (final IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (writeFile != null)
                    writeFile.close();
                if (input != null)
                    input.close();
            } catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Runnable getDownloadRunnable(final EBaldrFile type, final String domain, final String file)
    {
        Runnable runnable = null;

        switch (type)
        {
            case CONTENT:
                runnable = () ->
                {
                    this.downloadContentFile(domain, file);
                    System.out.println("Downloaded content : " + file);
                };
                break;
            case RESOURCE:
                runnable = () ->
                {
                    this.downloadResourceFile(domain, file);
                    System.out.println("Downloaded resource : " + file);
                };
                break;
            default:
                break;
        }
        return runnable;
    }

    public void downloadContentFile(final String domain, final String contentFile)
    {
        this.download(BaldrVars.getResourceURL() + domain + "/content/" + contentFile + ".json",
                this.storage.MAIN_FOLDER + "/baldr/" + BaldrVars.BASE_PROVIDER + "/" + domain + "/content/"
                        + contentFile + ".json");
    }

    public void downloadResourceFile(final String domain, final String resourceFile)
    {
        this.download(BaldrVars.getResourceURL() + domain + "/resources/" + resourceFile, this.storage.MAIN_FOLDER
                + "/baldr/" + BaldrVars.BASE_PROVIDER + "/" + domain + "/resources/" + resourceFile);
    }

    public BaldrContentDescriptor getContentDescriptor(final String domain)
    {
        for (final BaldrContentDescriptor descriptor : this.getDescriptors())
            if (descriptor.getFolderName().equals(domain))
                return descriptor;
        Baldr.logger.log(Level.SEVERE, "Beware ! Unknown domain as been requested : " + domain);
        return null;
    }

    public String getContentFile(final BaldrContentDescriptor domain, final String contentFile)
    {
        final String rtn = this.storage.getCachedFile("baldr",
                BaldrVars.BASE_PROVIDER + "/" + domain.getFolderName() + "/content/" + contentFile + ".json");
        if (StringUtils.isEmpty(rtn))
            Baldr.logger.log(Level.SEVERE,
                    "Beware ! Error loading content file : " + domain.getFolderName() + "/" + contentFile);
        return rtn;
    }

    public String getContentFile(final String domain, final String contentFile)
    {
        return this.getContentFile(this.getContentDescriptor(domain), contentFile);
    }

    public InputStream getResourceStream(final String domain, final String resourceFile)
    {
        return this.storage.getCachedStream("baldr",
                BaldrVars.BASE_PROVIDER + "/" + domain + "/resources/" + resourceFile);
    }

    public boolean isContentFileValid(final String domain, final String contentFile, final String hash)
    {
        if (!this.contentExist(domain, contentFile))
            return false;
        final String md5 = Hashing.murmur3_128().hashBytes(this.storage.getCachedFileBytes("baldr",
                BaldrVars.BASE_PROVIDER + "/" + domain + "/content/" + contentFile + ".json")).toString();
        if (md5.equals(hash))
            return true;
        return false;
    }

    public boolean isResourceFileValid(final String domain, final String resourceFile, final String hash)
    {
        if (!this.resourceExist(domain, resourceFile))
            return false;
        final String md5 = Hashing.murmur3_128().hashBytes(this.storage.getCachedFileBytes("baldr",
                BaldrVars.BASE_PROVIDER + "/" + domain + "/resources/" + resourceFile)).toString();
        if (md5.equals(hash))
            return true;
        return false;
    }

    public boolean resourceExist(final String domain, final String resourceFile)
    {
        return this.storage.cachedFileExist("baldr",
                BaldrVars.BASE_PROVIDER + "/" + domain + "/resources/" + resourceFile);
    }

    public ArrayList<BaldrContentDescriptor> getDescriptors()
    {
        return this.descriptors;
    }
}