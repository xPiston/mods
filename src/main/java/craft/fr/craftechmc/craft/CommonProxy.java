package fr.craftechmc.craft;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.craft.api.WorkbenchRegistry;
import fr.craftechmc.craft.block.BlockWorkbench;
import fr.craftechmc.craft.network.GuiHandler;
import fr.craftechmc.craft.network.PacketWorkbench;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Desc...
 * Created by Thog the 16/04/2016
 */
public class CommonProxy
{
    private SimpleNetworkWrapper networkPipeline;
    private GuiHandler           guiHandler;

    public void preInit()
    {

    }

    public void init()
    {
        WorkbenchRegistry
                .addCraft(new ItemStack(Block.getBlockFromName("craftechcontentmod:BlockModelDescribedPoubelleParc")),
                        "Poubelle", new ItemStack(Blocks.planks, 4), new ItemStack(Blocks.log, 1),
                        new ItemStack(Items.stick, 4));
        WorkbenchRegistry
                .addUpgrade(new ItemStack(Blocks.bookshelf), new ItemStack(Items.book, 1), new ItemStack(Items.book, 1),
                        new ItemStack(Items.book, 1));
        this.guiHandler = new GuiHandler(this);
        GameRegistry.registerBlock(CraftechCraft.WORKBENCH, "workbench");
        GameRegistry.registerTileEntity(BlockWorkbench.Tile.class, "crafttech_workbench");
        NetworkRegistry.INSTANCE.registerGuiHandler(CraftechCraft.getInstance(), guiHandler);
        networkPipeline = NetworkRegistry.INSTANCE.newSimpleChannel(CraftechCraft.MOD_ID);
        networkPipeline.registerMessage(PacketWorkbench.class, PacketWorkbench.class, 0, Side.CLIENT);
        networkPipeline.registerMessage(PacketWorkbench.class, PacketWorkbench.class, 0, Side.SERVER);
    }

    public SimpleNetworkWrapper pipeline()
    {
        return networkPipeline;
    }

    public GuiHandler guiHandler()
    {
        return guiHandler;
    }
}
