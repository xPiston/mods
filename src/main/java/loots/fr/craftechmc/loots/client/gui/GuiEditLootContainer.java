package fr.craftechmc.loots.client.gui;

import org.apache.commons.lang3.tuple.Pair;
import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiToggleButtonBase;
import org.yggard.brokkgui.element.GuiListView;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.skin.GuiLabeledSkinBase;
import org.yggard.brokkgui.skin.GuiSkinBase;

import fr.craftechmc.core.common.utils.FormatUtils;
import fr.craftechmc.loots.common.CraftechLoots;
import fr.craftechmc.loots.common.LootsPacketConstants;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.objects.ContainerLootList;
import fr.craftechmc.loots.common.objects.ContainerLootTable;
import fr.craftechmc.loots.common.tiles.TileLootContainer;
import fr.ourten.teabeans.listener.ValueChangeListener;

public class GuiEditLootContainer extends BrokkGuiScreen
{
    private final TileLootContainer          lootContainer;

    // Data syncing
    private boolean                          shouldCheck = false;

    private final GuiListView<GuiLootButton> listView;

    public GuiEditLootContainer(final TileLootContainer lootContainer)
    {
        super(0.5f, 0.5f, 200, 200);

        this.lootContainer = lootContainer;

        this.getMainPanel().setBorderColor(Color.LIGHT_GRAY);
        this.getMainPanel().setBorderThin(1);

        this.listView = new GuiListView<>();
        this.listView.setCellHeight(12);
        this.listView.setCellWidth(200);

        ContainerLootTablesManager.getInstance().getRefreshingProperty().addListener(
                (ValueChangeListener<Boolean>) (observable, oldValue, newValue) -> this.refreshListView(newValue));
        this.getMainPanel().addChild(this.listView);
        this.listView.setWidthRatio(1);
        this.listView.setHeightRatio(1);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.shouldCheck = true;
    }

    public void refreshLootTables()
    {
        if (this.shouldCheck)
        {
            CraftechLoots.network.send().packet(LootsPacketConstants.LOOT_TABLE_REQUEST).toServer();
            this.shouldCheck = false;
            ContainerLootTablesManager.getInstance().getRefreshingProperty().setValue(true);
        }
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);

        this.refreshLootTables();
    }

    public void refreshListView(final boolean refreshing)
    {
        this.listView.getElementsProperty().clear();
        if (!refreshing)
            for (final ContainerLootTable table : ContainerLootTablesManager.getInstance().getLootTables().getTables())
                for (final ContainerLootList list : table.getLootLists())
                {
                    final GuiLootButton button = new GuiLootButton(
                            FormatUtils.toLatinNumber(list.getLootLevel() + 1) + " | " + table.getLootTableName(),
                            this.lootContainer.getLoot().getLootLists()
                                    .containsKey(Pair.of(table.getLootTableName(), list.getLootLevel())));
                    button.setOnClickEvent(event ->
                    {
                        if (button.isSelected())
                            this.lootContainer.getLoot().getLootLists()
                                    .remove(Pair.of(table.getLootTableName(), list.getLootLevel()));
                        else
                            this.lootContainer.getLoot().getLootLists()
                                    .put(Pair.of(table.getLootTableName(), list.getLootLevel()), 0L);
                        CraftechLoots.network.send().packet(LootsPacketConstants.LOOT_CONTAINER_LISTCHANGE)
                                .with("remove", button.isSelected()).with("lootlistName", table.getLootTableName())
                                .with("lootlistLevel", list.getLootLevel()).with("coordX", this.lootContainer.xCoord)
                                .with("coordY", this.lootContainer.yCoord).with("coordZ", this.lootContainer.zCoord)
                                .toServer();
                        GuiEditLootContainer.this.refreshListView(false);
                    });
                    this.listView.getElementsProperty().add(button);
                }
        else
        {
            final GuiLootButton button = new GuiLootButton("I'm a sexy loading string ...", false);
            button.setDisabled(true);
            this.listView.getElementsProperty().add(button);
        }
    }

    private static class GuiLootButton extends GuiToggleButtonBase
    {
        public GuiLootButton(final String text, final boolean selected)
        {
            super(text);

            if (!selected)
                this.setTextColor(new Color(.87f, .87f, .87f));
            else
                this.setTextColor(Color.fromHex("#4CAF50"));
            this.setSelected(selected);
        }

        @Override
        protected GuiSkinBase<?> makeDefaultSkin()
        {
            return new GuiLabeledSkinBase<>(this, new GuiBehaviorBase<>(this));
        }
    }
}