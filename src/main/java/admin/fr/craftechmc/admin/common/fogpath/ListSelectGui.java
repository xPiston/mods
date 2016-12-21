package fr.craftechmc.admin.common.fogpath;

import java.util.ArrayList;

import org.yggard.brokkgui.gui.BrokkGuiScreen;

/**
 * This is the gui used to prompt the player a selection in a list of
 * ANameGetter (ex: FogZone) Created by arisu on 24/07/2016.
 */
public class ListSelectGui extends BrokkGuiScreen
{
    public ListSelectGui(final String prefix, final ArrayList<? extends ANameGetter> list)
    {
        // final GuiAlignmentPane rootPane = new GuiAlignmentPane();
        // this.setMainPanel(rootPane);
        // rootPane.setWidth(300);
        // rootPane.setHeight(150);
        // rootPane.setColor(Color.WHITE);
        //
        // rootPane.getxPosProperty().bind(new BaseBinding<Float>()
        // {
        // {
        // super.bind(ListSelectGui.this.getWidthProperty());
        // super.bind(ListSelectGui.this.getMainPanel().getWidthProperty());
        // }
        //
        // @Override
        // public Float computeValue()
        // {
        // return ListSelectGui.this.getWidthProperty().getValue() / 2
        // - ListSelectGui.this.getMainPanel().getWidthProperty().getValue() /
        // 2;
        // }
        // });
        // rootPane.getyPosProperty().bind(new BaseBinding<Float>()
        // {
        // {
        // super.bind(ListSelectGui.this.getHeightProperty());
        // super.bind(ListSelectGui.this.getMainPanel().getHeightProperty());
        // }
        //
        // @Override
        // public Float computeValue()
        // {
        // return ListSelectGui.this.getHeightProperty().getValue() / 2
        // - ListSelectGui.this.getMainPanel().getHeightProperty().getValue() /
        // 2;
        // }
        // });
        //
        // final GuiScrollPane scrollPane = new GuiScrollPane();
        // rootPane.addChild(scrollPane);
        // scrollPane.setFather(rootPane);
        // final GuiAlignmentPane pane = new GuiAlignmentPane();
        // scrollPane.setContent(pane);
        // pane.setFather(scrollPane);
        // rootPane.addChild(scrollPane, EAlignment.MIDDLE_CENTER);
        // scrollPane.setWidth(300);
        // scrollPane.setHeight(150);
        // pane.setWidth(300);
        // pane.setHeight(16 * (list.size() + 1) + 10);
        // pane.setColor(new Color(255, 255, 255, 0.9f));
        //
        // list.forEach(el ->
        // {
        // final GuiButton b = new GuiButton(prefix + el.getName());
        // b.setWidth(280);
        // b.setHeight(16);
        // pane.addChild(b, EAlignment.MIDDLE_UP);
        // b.setPosY(5 + 16 * list.indexOf(el));
        // b.setOnActionEvent(event ->
        // {
        // this.close();
        // FogPathManager.getInstance().select(el);
        // });
        // });
        //
        // final GuiButton b = new GuiButton("NOUVEAU");
        // b.setWidth(280);
        // b.setHeight(16);
        // pane.addChild(b, EAlignment.MIDDLE_UP);
        // b.setPosY(5 + 16 * list.size());
        // b.setOnActionEvent(event ->
        // {
        // this.close();
        // FogPathManager.getInstance().select(null);
        // });
    }
}
