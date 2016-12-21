package fr.craftechmc.baldr.client;

import com.google.common.collect.Sets;
import fr.craftechmc.baldr.BaldrVars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class BaldrResourcePack implements IResourcePack
{
    public static final HashSet<String> resourceDomains = Sets.newHashSet();

    @Override
    public InputStream getInputStream(ResourceLocation res) throws IOException
    {
        if (!res.getResourcePath().contains(".mcmeta"))
        {
            if (BaldrResourcePack.resourceDomains.contains(res.getResourceDomain()))
            {
                return new FileInputStream(new File(Minecraft.getMinecraft().mcDataDir + "/cache/baldr/"
                        + BaldrVars.BASE_PROVIDER + "/" + res.getResourceDomain() + "/resources/"
                        + (res.getResourcePath().contains("/") ? res.getResourcePath()
                                .substring(res.getResourcePath().lastIndexOf("/"), res.getResourcePath().length())
                                : res.getResourcePath())));
            }
        }
        return null;
    }

    @Override
    public boolean resourceExists(ResourceLocation res)
    {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Set getResourceDomains()
    {
        return BaldrResourcePack.resourceDomains;
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException
    {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException
    {
        return ImageIO.read(DefaultResourcePack.class
                .getResourceAsStream("/" + (new ResourceLocation("pack.png")).getResourcePath()));
    }

    @Override
    public String getPackName()
    {
        return "baldr-fakepack";
    }
}