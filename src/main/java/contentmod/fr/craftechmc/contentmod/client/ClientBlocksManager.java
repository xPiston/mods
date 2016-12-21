package fr.craftechmc.contentmod.client;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.contentmod.client.render.ClientModelManager;
import fr.craftechmc.contentmod.client.render.RenderModel;
import fr.craftechmc.contentmod.common.blocks.BlockModelDescribed;
import fr.craftechmc.contentmod.common.blocks.BlocksManager;
import fr.craftechmc.contentmod.common.blocks.ModelDataManager;
import fr.craftechmc.contentmod.common.multiblocks.BlockModelMultiBlockDescribed;
import fr.craftechmc.contentmod.common.multiblocks.ItemBlockMultiBlockBase;
import fr.craftechmc.contentmod.common.multiblocks.TileModelMultiBlockCore;
import fr.craftechmc.contentmod.common.multiblocks.TileModelMultiBlockGag;
import fr.craftechmc.contentmod.common.objects.BasicMultiBlockGroupDescriptor;
import fr.craftechmc.contentmod.common.objects.BlockDescriptors.BasicMultiBlockDescriptor;
import fr.craftechmc.contentmod.common.objects.EOrientable;
import fr.craftechmc.contentmod.common.objects.ModelDescriptor;
import fr.craftechmc.contentmod.common.tiles.TileModel;
import fr.craftechmc.core.client.render.ISBRHInventoryRenderer;
import fr.craftechmc.core.client.render.TESRIndex;
import fr.craftechmc.core.common.tab.CTabManager;
import fr.craftechmc.core.common.utils.MaterialUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ClientBlocksManager extends BlocksManager
{
    @Override
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
            for (final BasicMultiBlockDescriptor model : bmdescriptor.getDescriptors())
            {
                ClientModelManager.getInstance().getMbModels().put(model.getName(),
                        new ResourceLocation("craftechcontent-multiblock", model.getModel() + ".obj"));
                ClientModelManager.getInstance().getMbTextures().put(model.getName(),
                        new ResourceLocation("craftechcontent-multiblock", model.getTexture() + ".png"));
            }
            net.minecraftforge.client.MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block),
                    ClientProxy.multiBlock);
        }
        GameRegistry.registerTileEntity(TileModelMultiBlockCore.class, "TileModelMultiBlockCore");
        GameRegistry.registerTileEntity(TileModelMultiBlockGag.class, "TileModelMultiBlockGag");
    }

    @Override
    public void buildModelBlocks()
    {
        final ArrayList<ModelDescriptor> modelDescriptors = Lists.newArrayList();
        for (final String mgroup : this.modelGroups)
            modelDescriptors.add(this.GSON.fromJson(mgroup, ModelDescriptor.class));

        GameRegistry.registerTileEntity(TileModel.class, "ContentMod:TileModel");
        final RenderModel render = new RenderModel();
        ClientRegistry.bindTileEntitySpecialRenderer(TileModel.class, render);
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
            ClientModelManager.getInstance().getBasicModels().put("tile." + mdescriptor.getName(),
                    new ResourceLocation("craftechcontent-model", mdescriptor.getModel() + ".obj"));
            ClientModelManager.getInstance().getBasicTextures().put("tile." + mdescriptor.getName(),
                    new ResourceLocation("craftechcontent-model", mdescriptor.getTexture() + ".png"));
            ISBRHInventoryRenderer.blockByTESR.put(new TESRIndex(block, 0, false), render);
        }
    }
}