package fr.craftechmc.loots.client.gui;

import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.panel.GuiRelativePane;

import fr.craftechmc.core.common.utils.FormatUtils;
import fr.craftechmc.loots.common.objects.ContainerLootList;
import fr.craftechmc.loots.common.objects.ContainerLootTable;

/**
 * @author Ourten 4 déc. 2016
 */
public class LootListBadge extends GuiRelativePane
{
    private final ContainerLootList list;

    public LootListBadge(final ContainerLootTable table, final ContainerLootList list)
    {
        this.list = list;

        final GuiLabel name = new GuiLabel(
                FormatUtils.toLatinNumber(list.getLootLevel() + 1) + " | " + table.getLootTableName());
        final GuiButton editButton = new GuiButton("E");
        final GuiButton deleteButton = new GuiButton("D");

        name.setTextColor(new Color(.87f, .87f, .87f));
        editButton.setTextColor(new Color(.87f, .87f, .87f));
        deleteButton.setTextColor(Color.RED);
        deleteButton.getSkin().setBorderColor(Color.GREEN);
        deleteButton.getSkin().setBorderThin(1);

        deleteButton.setOnActionEvent(e ->
        {
            table.getLootLists().remove(list);
        });

        this.addChild(name, 0.5f, 0.5f);
        this.addChild(editButton, 0.8f, 0.5f);
        this.addChild(deleteButton, 0.9f, 0.5f);
    }

    public ContainerLootList getList()
    {
        return this.list;
    }
}