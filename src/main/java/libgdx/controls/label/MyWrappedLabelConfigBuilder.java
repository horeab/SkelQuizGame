package libgdx.controls.label;

import libgdx.resources.FontManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class MyWrappedLabelConfigBuilder {

    private float width = ScreenDimensionsManager.getScreenWidth(80);
    private float fontScale = FontManager.getNormalFontDim();
    private FontColor textColor = FontManager.getBaseColorForContrast();
    private FontConfig fontConfig;
    private String text;
    private boolean singleLineLabel = false;

    public MyWrappedLabelConfigBuilder setWidth(float width) {
        this.width = width;
        return this;
    }

    public MyWrappedLabelConfigBuilder setFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public float getFontScale() {
        return fontScale;
    }

    public MyWrappedLabelConfigBuilder setFontColor(FontColor color) {
        this.textColor = color;
        return this;
    }

    public MyWrappedLabelConfigBuilder setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
        return this;
    }

    public MyWrappedLabelConfigBuilder setStyleDependingOnContrast() {
        this.textColor = FontManager.getBaseColorForContrast();
        return this;
    }

    public MyWrappedLabelConfigBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MyWrappedLabelConfigBuilder setSingleLineLabel(boolean singleLineLabel) {
        return singleLineLabel ? setSingleLineLabel() : setWrappedLineLabel(width);
    }

    public MyWrappedLabelConfigBuilder setSingleLineLabel() {
        this.singleLineLabel = true;
        return this;
    }

    public MyWrappedLabelConfigBuilder setWrappedLineLabel(float width) {
        this.singleLineLabel = false;
        this.width = width;
        return this;
    }

    public MyWrappedLabelConfig build() {
        MyWrappedLabelConfig myWrappedLabelConfig = new MyWrappedLabelConfig();
        myWrappedLabelConfig.setWidth(width);
        myWrappedLabelConfig.setFontScale(fontScale);
        myWrappedLabelConfig.setSingleLineLabel(singleLineLabel);
        myWrappedLabelConfig.setText(text);
        myWrappedLabelConfig.setFontConfig(fontConfig);
        myWrappedLabelConfig.setTextColor(textColor);
        return myWrappedLabelConfig;
    }
}