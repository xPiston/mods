package fr.craftechmc.core.common.data;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftechmc.core.common.CraftechCore;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class DataStorage
{
    private static volatile DataStorage instance = null;

    public static final DataStorage getInstance()
    {
        if (DataStorage.instance == null)
            synchronized (DataStorage.class)
            {
                if (DataStorage.instance == null)
                    DataStorage.instance = new DataStorage();
            }
        return DataStorage.instance;
    }

    public final File MAIN_FOLDER;

    private DataStorage()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            this.MAIN_FOLDER = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir + "/cache/");
        else
            this.MAIN_FOLDER = new File("cache/");
        this.MAIN_FOLDER.mkdirs();
    }

    public boolean cachedFileExist(final String domain, final String identifier)
    {
        return new File(this.MAIN_FOLDER, domain + "/" + identifier).exists();
    }

    public List<String> getAllCachedFiles(final String domain, final String parentIdentifier)
    {
        final File dir = new File(this.MAIN_FOLDER, domain + "/" + parentIdentifier);
        final ArrayList<String> rtn = Lists.newArrayList();

        if (dir.isDirectory())
            Arrays.asList(dir.listFiles()).forEach(file ->
            {
                try
                {
                    rtn.add(FileUtils.readFileToString(file));
                } catch (final IOException e)
                {
                    CraftechCore.logger.log(Level.SEVERE, "Failed to read all files in  " + parentIdentifier, e);
                }
            });
        return rtn;
    }

    public String getCachedFile(final String domain, final String identifier)
    {
        try
        {
            return FileUtils.readFileToString(new File(this.MAIN_FOLDER, domain + "/" + identifier));
        } catch (final IOException e)
        {
            CraftechCore.logger.log(Level.SEVERE, "Failed to read  " + identifier, e);
        }
        return "";
    }

    public byte[] getCachedFileBytes(final String domain, final String identifier)
    {
        try
        {
            return FileUtils.readFileToByteArray(new File(this.MAIN_FOLDER, domain + "/" + identifier));
        } catch (final IOException e)
        {
            CraftechCore.logger.log(Level.SEVERE, "Failed to read  " + identifier, e);
        }
        return null;
    }

    public InputStream getCachedStream(final String domain, final String identifier)
    {
        try
        {
            return new FileInputStream(new File(this.MAIN_FOLDER, domain + "/" + identifier));
        } catch (final IOException e)
        {
            CraftechCore.logger.log(Level.SEVERE, "Failed to read  " + identifier, e);
        }
        return null;
    }

    public int listCachedFiles(final String domain, final String parentIdentifier)
    {
        final File dir = new File(this.MAIN_FOLDER, domain + "/" + parentIdentifier);

        if (dir.isDirectory())
            return dir.listFiles().length;
        return 0;
    }

    public boolean setCachedFile(final String domain, final String identifier, final byte[] data)
    {
        try
        {
            final File dest = new File(this.MAIN_FOLDER, domain + "/" + identifier);
            if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs())
            {
                CraftechCore.logger.log(Level.SEVERE, "Mkdirs failed for " + identifier);
                return false;
            }
            FileUtils.writeByteArrayToFile(dest, data);
            return true;
        } catch (final IOException e)
        {
            CraftechCore.logger.log(Level.SEVERE, "Failed to write  " + identifier, e);
        }
        return false;
    }

    public boolean setCachedFile(final String domain, final String identifier, final String data)
    {
        return this.setCachedFile(domain, identifier, data.getBytes(Charsets.UTF_8));
    }
}