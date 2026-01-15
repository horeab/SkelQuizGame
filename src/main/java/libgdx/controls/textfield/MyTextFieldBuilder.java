package libgdx.controls.textfield;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import libgdx.controls.MyTextField;
import libgdx.resources.FontManager;
import libgdx.utils.ScreenDimensionsManager;

public class MyTextFieldBuilder {

    private float width = ScreenDimensionsManager.getScreenWidth(20);
    private float height = ScreenDimensionsManager.getScreenWidth(10);
    private TextField.TextFieldFilter textFieldFilter;
    private float fontScale = FontManager.getNormalFontDim();

    public MyTextFieldBuilder setWidth(float width) {
        this.width = width;
        return this;
    }

    public MyTextFieldBuilder setFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public MyTextFieldBuilder setTextFieldFilter(TextField.TextFieldFilter textFieldFilter) {
        this.textFieldFilter = textFieldFilter;
        return this;
    }

    public MyTextFieldBuilder setHeight(float height) {
        this.height = height;
        return this;
    }

    public MyTextField build() {
        MyTextField myTextField = new MyTextField();
        myTextField.setSize(width, height);
        myTextField.getTextField().setTextFieldFilter(textFieldFilter);
        myTextField.add(myTextField.getTextField()).width(width).height(height);
        return myTextField;
    }
}