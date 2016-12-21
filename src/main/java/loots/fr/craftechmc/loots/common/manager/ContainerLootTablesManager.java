package fr.craftechmc.loots.common.manager;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fr.craftechmc.loots.common.objects.ContainerLootEntry;
import fr.craftechmc.loots.common.objects.ContainerLootList;
import fr.craftechmc.loots.common.objects.ContainerLootTable;
import fr.craftechmc.loots.common.storage.LootStorage;
import fr.ourten.teabeans.value.BaseProperty;

public class ContainerLootTablesManager
{
    private static volatile ContainerLootTablesManager serverInstance, clientInstance = null;
    private final LootTablesWrapper                    lootTables;
    private BaseProperty<Boolean>                      refreshingProperty;

    private ContainerLootTablesManager(final Side side)
    {
        this.lootTables = new LootTablesWrapper();
        if (side.isServer())
            this.lootTables.getTables().addAll(LootStorage.getInstance().getAllContainerLootTables());
        else
        {
            this.refreshingProperty = new BaseProperty<>(false, "refreshingProperty");
        }
    }

    public static final ContainerLootTablesManager getInstance()
    {
        return ContainerLootTablesManager.getInstance(FMLCommonHandler.instance().getEffectiveSide());
    }

    public static final ContainerLootTablesManager getInstance(final Side side)
    {
        if (side.isClient())
        {
            if (ContainerLootTablesManager.clientInstance == null)
                synchronized (ContainerLootTablesManager.class)
                {
                    if (ContainerLootTablesManager.clientInstance == null)
                        ContainerLootTablesManager.clientInstance = new ContainerLootTablesManager(side);
                }
            return ContainerLootTablesManager.clientInstance;
        }
        if (ContainerLootTablesManager.serverInstance == null)
            synchronized (ContainerLootTablesManager.class)
            {
                if (ContainerLootTablesManager.serverInstance == null)
                    ContainerLootTablesManager.serverInstance = new ContainerLootTablesManager(side);
            }
        return ContainerLootTablesManager.serverInstance;
    }

    public void loadTables()
    {
        this.lootTables.getTables().clear();
        final ContainerLootTable lootTable = new ContainerLootTable();
        lootTable.setLootTableName("applewonderland");

        final ContainerLootList lootList = new ContainerLootList();
        lootList.setModulation(1.10);
        lootList.setRespawnTime(30000);
        lootList.setLootLevel(0);

        LootItemManager.getInstance().getLootItem("item.apple:0").ifPresent(item ->
        {
            final ContainerLootEntry apple = new ContainerLootEntry();
            apple.setChance(1f);
            apple.setQuantityMax(30);
            apple.setQuantityMin(20);
            apple.setItem(LootItemManager.getInstance().getLootItem("item.apple:0").get());
            lootList.getLoots().add(apple);
        });

        LootItemManager.getInstance().getLootItem("item.golden_apple:0").ifPresent(item ->
        {
            final ContainerLootEntry appleGold = new ContainerLootEntry();
            appleGold.setChance(1.1f);
            appleGold.setQuantityMax(30);
            appleGold.setQuantityMin(20);
            appleGold.setItem(LootItemManager.getInstance().getLootItem("item.golden_apple:0").get());
            lootList.getLoots().add(appleGold);
        });

        lootTable.getLootLists().add(lootList);
        this.lootTables.getTables().add(lootTable);
    }

    public ContainerLootList getLootListByIdentifiers(final String tableName, final int level)
    {
        for (final ContainerLootTable table : this.lootTables.getTables())
            if (StringUtils.equals(table.getLootTableName(), tableName))
                return table.getLootLists().get(level);
        return null;
    }

    public ContainerLootTable getLootTableByName(final String tableName)
    {
        for (final ContainerLootTable table : this.lootTables.getTables())
            if (StringUtils.equals(table.getLootTableName(), tableName))
                return table;
        return null;
    }

    public final class LootTablesWrapper
    {
        private final ArrayList<ContainerLootTable> tables;

        public LootTablesWrapper()
        {
            this.tables = Lists.newArrayList();
        }

        public ArrayList<ContainerLootTable> getTables()
        {
            return this.tables;
        }

        @Override
        public String toString()
        {
            return "LootTablesWrapper [tables=" + this.tables + "]";
        }
    }

    public LootTablesWrapper getLootTables()
    {
        return this.lootTables;
    }

    public BaseProperty<Boolean> getRefreshingProperty()
    {
        return this.refreshingProperty;
    }
}