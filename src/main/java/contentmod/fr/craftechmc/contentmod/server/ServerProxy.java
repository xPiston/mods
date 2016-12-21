package fr.craftechmc.contentmod.server;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.craftechmc.baldr.BaldrStorage;
import fr.craftechmc.baldr.resource.objects.BaldrContentDescriptor;
import fr.craftechmc.contentmod.common.CommonProxy;
import fr.craftechmc.contentmod.common.CraftechContentMod;
import fr.craftechmc.contentmod.common.blocks.BlocksManager;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit(final FMLPreInitializationEvent e)
    {
        super.preInit(e);
        CraftechContentMod.blocksManager = new BlocksManager();

        final BaldrStorage provider = BaldrStorage.getInstance();

        final BaldrContentDescriptor multiblock = provider.getContentDescriptor("craftechcontent-multiblock");
        final BaldrContentDescriptor model = provider.getContentDescriptor("craftechcontent-model");
        final BaldrContentDescriptor block = provider.getContentDescriptor("craftechcontent-block");
        final BaldrContentDescriptor declinaison = provider.getContentDescriptor("craftechcontent-declinaison");

        model.getContentFiles().keySet().forEach(
                cFile -> CraftechContentMod.blocksManager.modelGroups.add(provider.getContentFile(model, cFile)));
        multiblock.getContentFiles().keySet().forEach(cFile -> CraftechContentMod.blocksManager.multiBlockGroups
                .add(provider.getContentFile(multiblock, cFile)));
        block.getContentFiles().keySet().forEach(
                cFile -> CraftechContentMod.blocksManager.basicBlockGroups.add(provider.getContentFile(block, cFile)));
        declinaison.getContentFiles().keySet().forEach(cFile -> CraftechContentMod.blocksManager.declinaisonGroups
                .add(provider.getContentFile(declinaison, cFile)));

        CraftechContentMod.blocksManager.buildBasicBlocks();
        CraftechContentMod.blocksManager.buildMultiBlocks();
        CraftechContentMod.blocksManager.buildModelBlocks();
        CraftechContentMod.blocksManager.buildDeclinaisons();
    }
}