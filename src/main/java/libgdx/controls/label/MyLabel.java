package libgdx.controls.label;


import com.badlogic.gdx.scenes.scene2d.ui.Label;

import libgdx.resources.FontManager;
import libgdx.resources.ResourcesManager;

public class MyLabel extends Label {

    MyLabel(String text) {
        super(text, ResourcesManager.getSkin());
        setFontScale(FontManager.getNormalFontDim());
    }

    /**
     * In some cases wrap displays wrong. Use {@link MyWrappedLabel} instead.
     */
    @Override
    public void setWrap(boolean wrap) {
        throw new RuntimeException("Wrap is not supported, see javadoc for other component");
    }
}
