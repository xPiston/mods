package fr.craftechmc.tracking.client.gui;

import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.panel.GuiRelativePane;

/**
 * @author Ourten 28 oct. 2016
 */
public class AdminTrackingTab extends GuiTab
{
    private GuiRelativePane mainPanel;

    public AdminTrackingTab()
    {
        super("Tracking Stats");

        this.setContent(this.mainPanel = new GuiRelativePane());
    }
}