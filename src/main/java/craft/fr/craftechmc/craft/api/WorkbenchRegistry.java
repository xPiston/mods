package fr.craftechmc.craft.api;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Registry for craft in the workbench
 * Created by Thog the 15/05/2016
 */
public final class WorkbenchRegistry
{
    private static final List<IWorkbenchEntry>  RECIPES  = Lists.newArrayList();
    private static final List<IUpgradableEntry> UPGRADES = Lists.newArrayList();

    public static void registerUpgrade(IUpgradableEntry entry)
    {
        UPGRADES.add(entry);
    }

    public static void registerCraft(IWorkbenchEntry entry)
    {
        RECIPES.add(entry);
    }

    public static void addCraft(ItemStack result, String name, ItemStack... inputs)
    {
        registerCraft(new ItemStackEntry(result, name, inputs));
    }

    public static void addUpgrade(ItemStack result, ItemStack... inputs)
    {
        registerUpgrade(new ItemStackUpgradableEntry(inputs, result));
    }

    public static IUpgradableEntry getUpgradeEntry(IInventory inventory)
    {
        for (IUpgradableEntry entry : UPGRADES)
        {
            if (entry.canUpgrade(inventory))
                return entry;
        }
        return null;
    }

    public static List<IWorkbenchEntry.Render> buildAvailableList(EntityPlayer player)
    {
        List<IWorkbenchEntry.Render> renders = Lists.newArrayList();
        renders.addAll(RECIPES.stream().filter(entry -> entry.canCraft(player)).map(IWorkbenchEntry::getRender)
                .collect(Collectors.toList()));
        return renders;
    }

    public static IWorkbenchEntry getEntryByIcon(ItemStack renderStack)
    {
        for (IWorkbenchEntry render : RECIPES)
        {
            if (ItemStack.areItemStacksEqual(render.getRender().getIcon(), renderStack) && ItemStack
                    .areItemStackTagsEqual(render.getRender().getIcon(), renderStack))
                return render;
        }
        return null;
    }
}
