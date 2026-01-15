package libgdx.controls.label;

import libgdx.constants.Contrast;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class MyWrappedLabelConfig {

    private float width;
    private float fontScale;
    private FontColor textColor;
    private FontConfig fontConfig;
    private String text;
    private Contrast contrast;
    private boolean singleLineLabel;

    MyWrappedLabelConfig() {
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getFontScale() {
        return fontScale;
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public FontColor getTextColor() {
        return textColor;
    }

    public void setTextColor(FontColor textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FontConfig getFontConfig() {
        return fontConfig;
    }

    public void setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
    }

    public Contrast getContrast() {
        return contrast;
    }

    public void setContrast(Contrast contrast) {
        this.contrast = contrast;
    }

    public boolean isSingleLineLabel() {
        return singleLineLabel;
    }

    public void setSingleLineLabel(boolean singleLineLabel) {
        this.singleLineLabel = singleLineLabel;
    }
}
