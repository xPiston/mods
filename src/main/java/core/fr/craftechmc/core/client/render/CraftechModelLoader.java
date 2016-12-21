package fr.craftechmc.core.client.render;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;

public class CraftechModelLoader implements IModelCustomLoader
{

    @Override
    public String getType()
    {
        return "Craftech Wavefront model";
    }

    private static final String[] types = { "cwm" };

    @Override
    public String[] getSuffixes()
    {
        return CraftechModelLoader.types;
    }

    @Override
    public IModelCustom loadInstance(final ResourceLocation resource) throws ModelFormatException
    {
        return new CraftechWavefrontObject(resource);
    }
}