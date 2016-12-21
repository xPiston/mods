package fr.craftechmc.environment.common.biomes;

import net.minecraft.world.biome.BiomeGenBase;

/**
 * The custom biome class Created by arisu on 24/07/2016.
 */
public class BiomeGenCTBase extends BiomeGenBase
{
    private float toxicityLevel;

    public BiomeGenCTBase(final int biomeID)
    {
        super(biomeID);
        this.setBiomeName("CustomBiome");
        this.setColor(0x6dd16e);
        this.waterColorMultiplier = 0x00ff00;
        this.temperature = 0.7f;
        this.flowers.clear();
        this.toxicityLevel = 1.2f;
    }

    @Override
    public int getSkyColorByTemp(final float temp)
    {
        return 0x9CC98D;
    }

    @Override
    public int getBiomeGrassColor(final int x, final int y, final int z)
    {
        return 0xb6c763;
    }

    public float getToxicityLevel()
    {
        return this.toxicityLevel;
    }

    public void setToxicityLevel(final float toxicityLevel)
    {
        this.toxicityLevel = toxicityLevel;
    }
}