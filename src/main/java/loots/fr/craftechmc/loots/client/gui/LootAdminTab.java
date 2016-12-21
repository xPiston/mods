package fr.craftechmc.loots.client.gui;

import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.element.GuiListView;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.panel.GuiRelativePane;

import fr.craftechmc.loots.common.CraftechLoots;
import fr.craftechmc.loots.common.LootsPacketConstants;
import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.objects.ContainerLootList;
import fr.craftechmc.loots.common.objects.ContainerLootTable;
import fr.ourten.teabeans.listener.ValueChangeListener;

/**
 * @author Ourten 28 oct. 2016
 */
public class LootAdminTab extends GuiTab
{
    private GuiRelativePane           mainPanel;

    // Data syncing
    private boolean                   shouldCheck = false;
    final GuiListView<LootTableBadge> listView;

    public LootAdminTab()
    {
        super("Loot Containers");

        this.setContent(this.mainPanel = new GuiRelativePane());
        this.getContent().setWidthRatio(1);
        this.getContent().setHeightRatio(1);

        this.listView = new GuiListView<>();

        this.listView.setCellHeight(12);
        this.listView.setCellWidth(200);

        ContainerLootTablesManager.getInstance().getRefreshingProperty().addListener(
                (ValueChangeListener<Boolean>) (observable, oldValue, newValue) -> this.refreshListView(newValue));
        this.mainPanel.addChild(this.listView, 0.5f, 0.55f);

        this.listView.setWidthRatio(1);
        this.listView.setHeightRatio(1);

        final GuiLabel placeholder = new GuiLabel("I'm a sexy loading string ...");
        placeholder.setTextColor(Color.LIGHT_GRAY);
        this.listView.setPlaceholder(placeholder);
        this.shouldCheck = true;

        final GuiButton refreshButton = new GuiButton("↺");
        refreshButton.setTextColor(Color.BLUE);
        refreshButton.setOnActionEvent(e -> this.shouldCheck = true);
        refreshButton.getSkin().setBorderColor(Color.BLUE);
        refreshButton.getSkin().setBorderThin(1);
        refreshButton.setWidth(16);
        refreshButton.setHeight(16);
        this.mainPanel.addChild(refreshButton, 0.05f, 0.05f);
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        super.renderNode(renderer, pass, mouseX, mouseY);

        this.refreshLootTables();
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

    public void refreshListView(final boolean refreshing)
    {
        this.listView.getElementsProperty().clear();
        if (!refreshing)
            for (final ContainerLootTable table : ContainerLootTablesManager.getInstance().getLootTables().getTables())
                for (final ContainerLootList list : table.getLootLists())
                {
                    final LootTableBadge badge = new LootTableBadge(table);

                    this.listView.getElementsProperty().add(badge);
                }
    }
}