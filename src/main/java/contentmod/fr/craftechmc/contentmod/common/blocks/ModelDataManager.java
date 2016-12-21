package fr.craftechmc.contentmod.common.blocks;

import java.util.HashMap;

import com.google.common.collect.Maps;

import fr.craftechmc.contentmod.common.objects.ModelDescriptor;

public class ModelDataManager
{
    private final HashMap<String, ModelDescriptor> modelDatas;

    private static volatile ModelDataManager       instance = null;

    private ModelDataManager()
    {
        this.modelDatas = Maps.newHashMap();
    }

    public static final ModelDataManager getInstance()
    {
        if (ModelDataManager.instance == null)
            synchronized (ModelDataManager.class)
            {
                if (ModelDataManager.instance == null)
                    ModelDataManager.instance = new ModelDataManager();
            }
        return ModelDataManager.instance;
    }

    public HashMap<String, ModelDescriptor> getModelDatas()
    {
        return this.modelDatas;
    }
}