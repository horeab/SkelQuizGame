package libgdx.controls;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import org.apache.commons.lang3.StringUtils;

import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class SearchTextField extends MyTextField {

    public SearchTextField() {
        build();
    }

    public void build() {
        add(GraphicUtils.getImage(MainResource.magnify_glass)).padRight(MainDimen.horizontal_general_margin.getDimen()).width(ScreenDimensionsManager.getScreenWidth(10)).height(ScreenDimensionsManager.getScreenHeight(5));
        setWidth(ScreenDimensionsManager.getScreenWidth(70));
        setHeight(ScreenDimensionsManager.getScreenHeight(10));
        textField.setSize(ScreenDimensionsManager.getScreenWidth(70), ScreenDimensionsManager.getScreenHeight(10));
        textField.setTextFieldFilter(new TextField.TextFieldFilter() {
            public boolean acceptChar(TextField textField, char c) {
                return StringUtils.isAlphanumericSpace(String.valueOf(c));
            }
        });
        add(textField).width(ScreenDimensionsManager.getScreenWidth(70)).height(ScreenDimensionsManager.getScreenHeight(10));
    }
}