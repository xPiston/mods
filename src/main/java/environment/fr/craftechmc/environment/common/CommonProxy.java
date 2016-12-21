package fr.craftechmc.environment.common;

import java.util.HashMap;
import java.util.Map;

import com.unascribed.lambdanetwork.DataType;
import com.unascribed.lambdanetwork.LambdaNetwork;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.environment.common.biomes.BiomeGenCTBase;
import fr.craftechmc.environment.common.blocks.BlockFluid;
import fr.craftechmc.environment.common.fog.FogEntity;
import fr.craftechmc.environment.common.items.ItemNonDrinkableWaterBucket;
import fr.craftechmc.environment.common.world.WorldProviderCT;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by arisu on 23/07/2016.
 */
public class CommonProxy
{
    private final BiomeGenCTBase biome      = new BiomeGenCTBase(168);
    private final byte[]         biomeArray = new byte[256];
    public Map<Block, Item>      buckets    = new HashMap<>();

    public void init()
    {
        // Register events
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        // Register biomes
        BiomeManager.addSpawnBiome(this.biome); // Biome ids starting from 256
                                                // should be free for a long
                                                // time

        // Register fluids
        FluidRegistry.registerFluid(CraftechEnvironment.nonDrinkableWater);
        CraftechEnvironment.nonDrinkableWater = FluidRegistry.getFluid("non_drinkable_water");

        CraftechEnvironment.nonDrinkableWaterBlock = new BlockFluid(CraftechEnvironment.nonDrinkableWater,
                Material.water, "non_drinkable_water").setUnlocalizedName("nonDrinkableWater");
        GameRegistry.registerBlock(CraftechEnvironment.nonDrinkableWaterBlock, null, "non_drinkable_water");

        CraftechEnvironment.itemNonDrinkableWaterBucket = new ItemNonDrinkableWaterBucket(
                CraftechEnvironment.nonDrinkableWaterBlock);
        GameRegistry.registerItem(CraftechEnvironment.itemNonDrinkableWaterBucket, "nonDrinkableWaterBucket");
        FluidContainerRegistry.registerFluidContainer(CraftechEnvironment.nonDrinkableWater,
                new ItemStack(CraftechEnvironment.itemNonDrinkableWaterBucket), new ItemStack(Items.bucket));
        this.buckets.put(CraftechEnvironment.nonDrinkableWaterBlock, CraftechEnvironment.itemNonDrinkableWaterBucket);

        // Register entities
        CraftechEnvironment.registerEntity(FogEntity.class, "FogEntity");

        // Register worldprovider for daylightcycle modification
        WorldProviderCT.overrideDefault();

        // Fill biome array to replace every vanilla biome
        for (int i = 0; i < this.biomeArray.length; i++)
            this.biomeArray[i] = (byte) this.biome.biomeID;

        // Build lambda network
        CraftechEnvironment.network = LambdaNetwork.builder().channel(CraftechEnvironment.MODID)
                .packet(CraftechEnvironmentPacketConstants.ENTITY_SYNC_PACKET).with(DataType.INT, "id")
                .with(DataType.BOOLEAN, "isDead").with(DataType.DOUBLE, "x").with(DataType.DOUBLE, "y")
                .with(DataType.DOUBLE, "z").with(DataType.DOUBLE, "xv").with(DataType.DOUBLE, "yv")
                .with(DataType.DOUBLE, "zv").boundTo(Side.CLIENT).handledOnMainThreadBy((player, token) ->
                {
                    FogEntity entity = null;
                    for (final Object e : player.worldObj.getLoadedEntityList())
                        if (e instanceof FogEntity && ((FogEntity) e).getEntityId() == token.getInt("id"))
                            entity = (FogEntity) e;
                    if (entity == null)
                    {
                        entity = new FogEntity(player.worldObj);
                        player.worldObj.spawnEntityInWorld(entity);
                        entity.setEntityId(token.getInt("id"));
                    }
                    if (token.getBoolean("isDead"))
                        entity.setDead();
                    entity.setPosition(token.getDouble("x"), token.getDouble("y"), token.getDouble("z"));
                    entity.setVelocity(token.getDouble("xv"), token.getDouble("yv"), token.getDouble("zv"));
                }).build();
    }

    @SubscribeEvent
    public void onChunkLoad(final ChunkEvent.Load event)
    {
        event.getChunk().setBiomeArray(this.biomeArray);
    }

    @SubscribeEvent
    public void onBucketFill(final FillBucketEvent event)
    {
        if (!event.entityPlayer.capabilities.isCreativeMode)
        {
            event.setCanceled(true);
            return;
        }

        final ItemStack result = this.fillCustomBucket(event.world, event.target);

        if (result == null)
            return;

        event.result = result;
        event.setResult(Event.Result.ALLOW);
    }

    private ItemStack fillCustomBucket(final World world, final MovingObjectPosition pos)
    {
        final Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

        final Item bucket = this.buckets.get(block);
        if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0)
        {
            world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
            return new ItemStack(bucket);
        }
        else
            return null;
    }

}
