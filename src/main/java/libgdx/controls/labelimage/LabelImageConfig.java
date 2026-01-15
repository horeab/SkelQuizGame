package libgdx.controls.labelimage;


import libgdx.resources.Res;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class LabelImageConfig {

    private String text;
    private FontColor textColor;
    private Res image;
    private float imageSideDimension;
    private float fontScale;
    private float labelWidth;
    private float marginBetweenLabelImage;
    private boolean alignTextRight;
    private boolean singleLineLabel;
    private FontConfig fontConfig;

    public boolean isSingleLineLabel() {
        return singleLineLabel;
    }

    public void setSingleLineLabel(boolean shortLabel) {
        this.singleLineLabel = shortLabel;
    }

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    public FontConfig getFontConfig() {
        return fontConfig;
    }

    public void setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
    }

    public Res getImage() {
        return image;
    }

    void setImage(Res image) {
        this.image = image;
    }

    public boolean isAlignTextRight() {
        return alignTextRight;
    }

    void setAlignTextRight(boolean alignTextRight) {
        this.alignTextRight = alignTextRight;
    }

    public float getFontScale() {
        return fontScale;
    }

    void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public FontColor getTextColor() {
        return textColor;
    }

    public float getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(float labelWidth) {
        this.labelWidth = labelWidth;
    }

    void setTextColor(FontColor textColor) {
        this.textColor = textColor;
    }

    public float getMarginBetweenLabelImage() {
        return marginBetweenLabelImage;
    }

    void setMarginBetweenLabelImage(float marginBetweenLabelImage) {
        this.marginBetweenLabelImage = marginBetweenLabelImage;
    }

    public float getImageSideDimension() {
        return imageSideDimension;
    }

    public void setImageSideDimension(float imageSideDimension) {
        this.imageSideDimension = imageSideDimension;
    }
}
