package fr.craftechmc.admin.client;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.panel.GuiRelativePane;
import org.yggard.brokkgui.panel.GuiTabPane;

import com.google.common.collect.Lists;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiAdmin extends BrokkGuiScreen
{
    public static final ArrayList<Supplier<? extends GuiTab>> tabs = Lists.newArrayList();

    public GuiAdmin()
    {
        super(0.5f, 0.5f, 300, 200);

        final GuiRelativePane pane = new GuiRelativePane();

        this.setMainPanel(pane);

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.getSkin().setBorderColor(Color.LIGHT_GRAY);
        tabPane.getSkin().setBorderThin(1);

        final GuiTab defaultTab = new GuiTab("Admin Center");
        tabPane.addTab(defaultTab);
        tabPane.setDefaultTab(0);

        GuiAdmin.tabs.forEach(supplier -> tabPane.addTab(supplier.get()));

        pane.addChild(tabPane);
        tabPane.setWidthRatio(1);
        tabPane.setHeightRatio(1);
    }
}