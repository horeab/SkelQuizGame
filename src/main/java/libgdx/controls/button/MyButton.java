package libgdx.controls.button;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.List;

import libgdx.constants.Contrast;
import libgdx.controls.TextTable;
import libgdx.controls.label.MyLabel;
import libgdx.game.Game;
import libgdx.resources.ResourcesManager;

public class MyButton extends TextButton {

    private ButtonSkin buttonSkin;
    private Contrast contrast;

    MyButton(ButtonSize buttonSize, ButtonSkin buttonSkin) {
        this(buttonSize, buttonSkin, Contrast.LIGHT);
    }

    MyButton(ButtonSize buttonSize, ButtonSkin buttonSkin, Contrast contrast) {
        super(null, ResourcesManager.getSkin());
        this.contrast = contrast;
        setButtonSkin(buttonSkin);
        if (buttonSize != null) {
            setWidth(buttonSize.getWidth());
            setHeight(buttonSize.getHeight());
        }
        clearChildren();
    }

    public String getText() {
        return ((TextTable) ((Table) findActor(ButtonBuilder.CENTER_TEXT_IMAGE_ROW_NAME)).getChildren().first()).getText();
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);
        if (isDisabled) {
            setTouchable(Touchable.disabled);
        }
        TextButtonStyle buttonStyle = new TextButtonStyle(getStyle());
        if (isDisabled && buttonSkin.getButtonDisabledFontColor() != null) {
            buttonStyle.font = Game.getInstance().getFontManager().getFont(buttonSkin.getButtonDisabledFontColor());
        }
        setStyle(buttonStyle);
    }

    public Table getCenterRow() {
        return (Table) findActor(ButtonBuilder.CENTER_TEXT_IMAGE_ROW_NAME);
    }

    public void setButtonSkin(ButtonSkin buttonSkin) {
        this.buttonSkin = buttonSkin;
        TextButtonStyle buttonStyle = new TextButtonStyle(getStyle());
        buttonStyle.up = buttonSkin.getImgUp();
        buttonStyle.down = buttonSkin.getImgDown();
        buttonStyle.checked = buttonSkin.getImgChecked();
        buttonStyle.disabled = buttonSkin.getImgDisabled();
        if (buttonSkin.getButtonDisabledFontColor() != null) {
            buttonStyle.font = Game.getInstance().getFontManager().getFont(buttonSkin.getButtonDisabledFontColor());
        }
        setStyle(buttonStyle);
    }

    @Override
    public void setStyle(ButtonStyle style) {
        super.setStyle(style);
        List<MyLabel> labels = getCenterRowLabels();
        for (MyLabel label : labels) {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = label.getStyle().font;
            label.setStyle(labelStyle);
        }
    }

    public ButtonSkin getButtonSkin() {
        return buttonSkin;
    }

    public List<MyLabel> getCenterRowLabels() {
        Table centerRow = getCenterRow();
        if (centerRow != null && centerRow.getChildren() != null && centerRow.getChildren().size > 0 && centerRow.getChildren().first() instanceof TextTable) {
            return ((TextTable) centerRow.getChildren().first()).getLabels();
        }
        return new ArrayList<>();
    }
}
