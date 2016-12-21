package fr.craftechmc.admin.common.fogpath;

import org.yggard.brokkgui.gui.BrokkGuiScreen;

/**
 * This is the input gui used to prompt the player a string entry (ex: name of a
 * fogpath) Created by arisu on 24/07/2016.
 */
public class InputSetterGui extends BrokkGuiScreen
{
    public InputSetterGui(final String promptText)
    {
        // this.setMainPanel(new GuiAlignmentPane());
        // this.getMainPanel().getxPosProperty().bind(new BaseBinding<Float>()
        // {
        // {
        // super.bind(InputSetterGui.this.getWidthProperty());
        // super.bind(InputSetterGui.this.getMainPanel().getWidthProperty());
        // }
        //
        // @Override
        // public Float computeValue()
        // {
        // return InputSetterGui.this.getWidthProperty().getValue() / 2
        // - InputSetterGui.this.getMainPanel().getWidthProperty().getValue() /
        // 2;
        // }
        // });
        // this.getMainPanel().getyPosProperty().bind(new BaseBinding<Float>()
        // {
        // {
        // super.bind(InputSetterGui.this.getHeightProperty());
        // super.bind(InputSetterGui.this.getMainPanel().getHeightProperty());
        // }
        //
        // @Override
        // public Float computeValue()
        // {
        // return InputSetterGui.this.getHeightProperty().getValue() / 2
        // - InputSetterGui.this.getMainPanel().getHeightProperty().getValue() /
        // 2;
        // }
        // });
        //
        // final GuiAlignmentPane pane = (GuiAlignmentPane) this.getMainPanel();
        // pane.setWidth(300);
        // pane.setHeight(40);
        // pane.setColor(new Color(255, 255, 255, 0.9f));
        //
        // final GuiTextfield prompt = new GuiTextfield();
        // pane.addChild(prompt, EAlignment.LEFT_UP);
        // prompt.setWidth(150);
        // prompt.setHeight(20);
        // prompt.setPosX(10);
        // prompt.setPosY(10);
        // prompt.setPromptText(promptText);
        // prompt.setFocused(true);
        // prompt.addValidator(new AsciiPrintableTextValidator());
        // prompt.addValidator(new RequiredInputTextValidator());
        //
        // final GuiButton button = new GuiButton("Valider");
        // pane.addChild(button, EAlignment.LEFT_UP);
        // button.setWidth(50);
        // button.setHeight(20);
        // button.setPosX(170);
        // button.setPosY(10);
        // button.setOnActionEvent(e ->
        // {
        // prompt.validate();
        // if (prompt.isInvalidated())
        // {
        // this.close();
        // FogPathManager.getInstance().input(prompt.getText());
        // }
        // });
    }
}
