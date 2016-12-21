package fr.craftechmc.contentmod.client.render;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import fr.craftechmc.contentmod.common.CraftechContentMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class ClientModelManager
{
    private final IModelCustom                          erroredModel;
    private final HashMap<String, ResourceLocation>     mbTextures;
    private final HashMap<String, ResourceLocation>     mbModels;

    private final HashMap<String, ResourceLocation>     basicTextures;
    private final HashMap<String, ResourceLocation>     basicModels;

    private final LoadingCache<String, WavefrontObject> multiblockModelCache;
    private final LoadingCache<String, WavefrontObject> blockModelCache;

    private static volatile ClientModelManager          instance = null;

    private ClientModelManager()
    {
        this.mbTextures = Maps.newHashMap();
        this.mbModels = Maps.newHashMap();
        this.basicTextures = Maps.newHashMap();
        this.basicModels = Maps.newHashMap();

        this.multiblockModelCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, WavefrontObject>()
                {
                    @Override
                    public WavefrontObject load(final String key)
                    {
                        try
                        {
                            return (WavefrontObject) AdvancedModelLoader
                                    .loadModel(ClientModelManager.this.mbModels.get(key));
                        } catch (final ModelFormatException e)
                        {
                            CraftechContentMod.LOGGER.log(Level.SEVERE,
                                    "Unable to load : " + ClientModelManager.this.mbModels.get(key).getResourceDomain()
                                            + "/" + ClientModelManager.this.mbModels.get(key).getResourcePath());
                            return (WavefrontObject) ClientModelManager.this.getErroredModel();
                        }
                    }
                });

        this.blockModelCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, WavefrontObject>()
                {
                    @Override
                    public WavefrontObject load(final String key)
                    {
                        try
                        {
                            return (WavefrontObject) AdvancedModelLoader
                                    .loadModel(ClientModelManager.this.basicModels.get(key));
                        } catch (final ModelFormatException e)
                        {
                            CraftechContentMod.LOGGER.log(Level.SEVERE,
                                    "Unable to load : "
                                            + ClientModelManager.this.basicModels.get(key).getResourceDomain() + "/"
                                            + ClientModelManager.this.basicModels.get(key).getResourcePath());
                            return (WavefrontObject) ClientModelManager.this.getErroredModel();
                        }
                    }
                });

        this.erroredModel = AdvancedModelLoader
                .loadModel(new ResourceLocation(CraftechContentMod.MODASSETS, "models/errored.obj"));
    }

    public static final ClientModelManager getInstance()
    {
        if (ClientModelManager.instance == null)
            synchronized (ClientModelManager.class)
            {
                if (ClientModelManager.instance == null)
                    ClientModelManager.instance = new ClientModelManager();
            }
        return ClientModelManager.instance;
    }

    public IModelCustom getErroredModel()
    {
        return this.erroredModel;
    }

    public HashMap<String, ResourceLocation> getMbTextures()
    {
        return this.mbTextures;
    }

    public HashMap<String, ResourceLocation> getMbModels()
    {
        return this.mbModels;
    }

    public HashMap<String, ResourceLocation> getBasicTextures()
    {
        return this.basicTextures;
    }

    public HashMap<String, ResourceLocation> getBasicModels()
    {
        return this.basicModels;
    }

    public LoadingCache<String, WavefrontObject> getMultiblockModelCache()
    {
        return this.multiblockModelCache;
    }

    public LoadingCache<String, WavefrontObject> getBlockModelCache()
    {
        return this.blockModelCache;
    }
}