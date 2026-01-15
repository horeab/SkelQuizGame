package libgdx.controls;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import libgdx.resources.ResourcesManager;
import libgdx.utils.ScreenDimensionsManager;

public class NumberTextField extends MyTextField {

    private TextField textField = new TextField("", ResourcesManager.getSkin());

    public NumberTextField() {
        build();
    }

    public void build() {
        setWidth(ScreenDimensionsManager.getScreenWidth(70));
        setHeight(ScreenDimensionsManager.getScreenHeight(10));
        textField.setSize(ScreenDimensionsManager.getScreenWidth(70), ScreenDimensionsManager.getScreenHeight(10));
    }

    public TextField getTextField() {
        return textField;
    }
}