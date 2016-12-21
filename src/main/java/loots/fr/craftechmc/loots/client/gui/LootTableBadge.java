package fr.craftechmc.loots.client.gui;

import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.panel.GuiRelativePane;

import fr.craftechmc.loots.common.manager.ContainerLootTablesManager;
import fr.craftechmc.loots.common.objects.ContainerLootTable;

/**
 * @author Ourten 4 déc. 2016
 */
public class LootTableBadge extends GuiRelativePane
{
    private boolean isEditing;

    public LootTableBadge(final ContainerLootTable table)
    {
        final GuiLabel name = new GuiLabel(table.getLootTableName());
        name.setTextColor(new Color(.87f, .87f, .87f));
        this.addChild(name, 0.5f, 0.5f);

        final GuiButton editButton = new GuiButton("E");
        editButton.setTextColor(new Color(.87f, .87f, .87f));
        editButton.getSkin().setBorderColor(new Color(.87f, .87f, .87f));
        editButton.getSkin().setBorderThin(1);
        editButton.setWidth(11);
        editButton.setHeight(11);
        this.addChild(editButton, 0.8f, 0.5f);

        final GuiButton addButton = new GuiButton("+");
        addButton.setTextColor(Color.GREEN);
        addButton.getSkin().setBorderColor(Color.GREEN);
        addButton.getSkin().setBorderThin(1);
        addButton.setWidth(11);
        addButton.setHeight(11);
        this.addChild(addButton, 0.87f, 0.5f);

        final GuiButton deleteButton = new GuiButton("D");
        deleteButton.setTextColor(Color.RED);
        deleteButton.getSkin().setBorderColor(Color.RED);
        deleteButton.getSkin().setBorderThin(1);
        deleteButton.setWidth(11);
        deleteButton.setHeight(11);
        deleteButton.setOnActionEvent(
                e -> ContainerLootTablesManager.getInstance().getLootTables().getTables().remove(table));
        this.addChild(deleteButton, 0.94f, 0.5f);
    }
}