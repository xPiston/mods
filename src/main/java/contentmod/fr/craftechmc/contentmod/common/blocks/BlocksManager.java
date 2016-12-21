package fr.craftechmc.contentmod.common.blocks;

import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.contentmod.common.CraftechContentMod;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomCarpet;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomDoor;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomFence;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomFenceGate;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomSlab;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomStairs;
import fr.craftechmc.contentmod.common.blocks.customs.BlockCustomWall;
import fr.craftechmc.contentmod.common.blocks.customs.BlockObject;
import fr.craftechmc.contentmod.common.blocks.customs.ItemBlockCustomSlab;
import fr.craftechmc.contentmod.common.blocks.customs.ItemBlockGenericMeta;
import fr.craftechmc.contentmod.common.blocks.customs.ItemCustomDoor;
import fr.craftechmc.contentmod.common.multiblocks.BlockModelMultiBlockDescribed;
import fr.craftechmc.contentmod.common.multiblocks.ItemBlockMultiBlockBase;
import fr.craftechmc.contentmod.common.multiblocks.TileModelMultiBlockCore;
import fr.craftechmc.contentmod.common.multiblocks.TileModelMultiBlockGag;
import fr.craftechmc.contentmod.common.objects.BasicBlockGroupDescriptor;
import fr.craftechmc.contentmod.common.objects.BasicMultiBlockGroupDescriptor;
import fr.craftechmc.contentmod.common.objects.BlockDescriptors.BasicBlockDescriptor;
import fr.craftechmc.contentmod.common.objects.BlockDescriptors.BasicMultiBlockDescriptor;
import fr.craftechmc.contentmod.common.objects.BlockDescriptors.DeclinaisonDescriptor;
import fr.craftechmc.contentmod.common.objects.DeclinaisonGroupDescriptor;
import fr.craftechmc.contentmod.common.objects.EDeclinaison;
import fr.craftechmc.contentmod.common.objects.EOrientable;
import fr.craftechmc.contentmod.common.objects.ModelDescriptor;
import fr.craftechmc.contentmod.common.tiles.TileModel;
import fr.craftechmc.core.common.tab.CTabManager;
import fr.craftechmc.core.common.utils.MaterialUtils;

public class BlocksManager
{
    protected final Gson           GSON;

    public final ArrayList<String> basicBlockGroups;
    public final ArrayList<String> declinaisonGroups;
    public final ArrayList<String> multiBlockGroups;
    public final ArrayList<String> modelGroups;

    public BlocksManager()
    {
        this.GSON = new Gson();

        this.basicBlockGroups = Lists.newArrayList();
        this.declinaisonGroups = Lists.newArrayList();
        this.multiBlockGroups = Lists.newArrayList();
        this.modelGroups = Lists.newArrayList();
    }

    public final void buildDeclinaisons()
    {
        final ArrayList<DeclinaisonGroupDescriptor> declinaisonDescriptors = Lists.newArrayList();

        for (final String dgroup : this.declinaisonGroups)
            declinaisonDescriptors.add(this.GSON.fromJson(dgroup, DeclinaisonGroupDescriptor.class));
        for (final DeclinaisonGroupDescriptor ddescriptor : declinaisonDescriptors)
            for (final DeclinaisonDescriptor descriptor : ddescriptor.getDescriptors())
            {
                final BlockObject block = new BlockObject(descriptor.getDeclinaisonName(), descriptor.getMeta());
                if (block.getBlock() == null)
                {
                    CraftechContentMod.LOGGER.log(Level.SEVERE, "Block " + block.getBlockName()
                            + " not found in registry ! He will be ignored. Blame vroulas !");
                    continue;
                }
                if (descriptor.getDeclinaison().equals(EDeclinaison.STAIR))
                    GameRegistry.registerBlock(new BlockCustomStairs(descriptor.getName(), block),
                            descriptor.getName());
                else if (descriptor.getDeclinaison().equals(EDeclinaison.SLAB))
                    GameRegistry.registerBlock(new BlockCustomSlab(descriptor.getName(), block),
                            ItemBlockCustomSlab.class, descriptor.getName());
                else if (descriptor.getDeclinaison().equals(EDeclinaison.WALL))
                    GameRegistry.registerBlock(new BlockCustomWall(descriptor.getName(), block),
                            ItemBlockGenericMeta.class, descriptor.getName());
                else if (descriptor.getDeclinaison().equals(EDeclinaison.FENCE))
                    GameRegistry.registerBlock(new BlockCustomFence(descriptor.getName(), block),
                            ItemBlockGenericMeta.class, descriptor.getName());
                else if (descriptor.getDeclinaison().equals(EDeclinaison.FENCE_GATE))
                    GameRegistry.registerBlock(new BlockCustomFenceGate(descriptor.getName(), block),
                            descriptor.getName());
                else if (descriptor.getDeclinaison().equals(EDeclinaison.CARPET))
                    GameRegistry.registerBlock(new BlockCustomCarpet(descriptor.getName(), block),
                            ItemBlockGenericMeta.class, descriptor.getName());
                else if (descriptor.getDeclinaison().equals(EDeclinaison.DOOR))
                {
                    final BlockCustomDoor blockObject = new BlockCustomDoor(descriptor.getName(),
                            descriptor.getTextures(), block, descriptor.isPressureActivate());
                    GameRegistry.registerBlock(blockObject, descriptor.getName());
                    GameRegistry.registerItem(
                            blockObject
                                    .setDrop(new ItemCustomDoor(blockObject).setUnlocalizedName(descriptor.getName())),
                            "item_" + descriptor.getName());
                }
            }
    }

    public void buildMultiBlocks()
    {
        final ArrayList<BasicMultiBlockGroupDescriptor> multiBlockGroupDescriptors = Lists.newArrayList();

        for (final String mgroup : this.multiBlockGroups)
            multiBlockGroupDescriptors.add(this.GSON.fromJson(mgroup, BasicMultiBlockGroupDescriptor.class));

        for (final BasicMultiBlockGroupDescriptor bmdescriptor : multiBlockGroupDescriptors)
        {
            final BlockModelMultiBlockDescribed block = (BlockModelMultiBlockDescribed) new BlockModelMultiBlockDescribed(
                    bmdescriptor.getName(), MaterialUtils.getMaterialFromName(bmdescriptor.getMaterial()))
                            .setCreativeTab(CTabManager.getTab("CONTENT_MULTIBLOCK"));

            for (final BasicMultiBlockDescriptor descriptor : bmdescriptor.getDescriptors())
                block.addModel(descriptor);

            GameRegistry.registerBlock(block, ItemBlockMultiBlockBase.class, bmdescriptor.getName());
        }
        GameRegistry.registerTileEntity(TileModelMultiBlockCore.class, "TileModelMultiBlockCore");
        GameRegistry.registerTileEntity(TileModelMultiBlockGag.class, "TileModelMultiBlockGag");
    }

    public final void buildBasicBlocks()
    {
        final ArrayList<BasicBlockGroupDescriptor> blockGroupDescriptors = Lists.newArrayList();
        for (final String bbgroup : this.basicBlockGroups)
            blockGroupDescriptors.add(this.GSON.fromJson(bbgroup, BasicBlockGroupDescriptor.class));

        for (final BasicBlockGroupDescriptor bbdescriptor : blockGroupDescriptors)
        {
            final BlockDescribed block = new BlockDescribed(
                    MaterialUtils.getMaterialFromName(bbdescriptor.getMaterial()));
            block.setUnlocalizedName(bbdescriptor.getName());
            block.setOpaque(bbdescriptor.isOpaque());
            final BasicBlockDescriptor[] descriptors = new BasicBlockDescriptor[bbdescriptor.getDescriptors().size()];
            for (int i = 0; i < bbdescriptor.getDescriptors().size(); i++)
                descriptors[i] = bbdescriptor.getDescriptors().get(i);
            block.addSubBlocks(descriptors);
            GameRegistry.registerBlock(block, ItemBlockBasicDescribed.class, bbdescriptor.getName());
        }
    }

    public void buildModelBlocks()
    {
        final ArrayList<ModelDescriptor> modelDescriptors = Lists.newArrayList();
        for (final String mgroup : this.modelGroups)
            modelDescriptors.add(this.GSON.fromJson(mgroup, ModelDescriptor.class));

        GameRegistry.registerTileEntity(TileModel.class, "ContentMod:TileModel");
        for (final ModelDescriptor mdescriptor : modelDescriptors)
        {
            final BlockModelDescribed block = new BlockModelDescribed(
                    MaterialUtils.getMaterialFromName(mdescriptor.getMaterial()));
            block.setUnlocalizedName(mdescriptor.getName());
            block.setLightLevel(mdescriptor.getLightning());
            block.setHardness(mdescriptor.getBlockHardness());
            block.setResistance(mdescriptor.getBlockResistance());
            if (!StringUtils.isEmpty(mdescriptor.getOrientable()))
                block.setOrientable(EOrientable.valueOf(mdescriptor.getOrientable()));
            block.setTextureName("craftechcontent-model:" + mdescriptor.getTexture());
            block.setCreativeTab(CTabManager.getTab("CONTENT_MODEL"));
            GameRegistry.registerBlock(block, "BlockModelDescribed" + mdescriptor.getName());
            ModelDataManager.getInstance().getModelDatas().put("tile." + mdescriptor.getName(), mdescriptor);
        }
    }
}