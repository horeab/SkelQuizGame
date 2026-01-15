package libgdx.controls.labelimage;

import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class LabelImageConfigBuilder {

    public static final float DEFAULT_IMAGE_SIDE_DIMENSION = ScreenDimensionsManager.getScreenWidth(9);

    private String text;
    private Res image;
    private boolean alignTextRight;
    private float marginBetweenLabelImage;
    private float imageSideDimension = DEFAULT_IMAGE_SIDE_DIMENSION;
    private float fontScale = FontManager.getNormalFontDim();
    private FontColor textColor = FontManager.getBaseColorForContrast();
    private boolean singleLineLabel = true;
    private FontConfig fontConfig;
    private float labelWidth = ScreenDimensionsManager.getScreenWidth(50);

    public LabelImageConfigBuilder setSingleLineLabel() {
        this.singleLineLabel = true;
        return this;
    }

    public LabelImageConfigBuilder setWrappedLineLabel(float labelWidth) {
        this.singleLineLabel = false;
        this.labelWidth = labelWidth;
        return this;
    }

    public LabelImageConfigBuilder setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
        return this;
    }

    public LabelImageConfigBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public LabelImageConfigBuilder setImage(Res image) {
        this.image = image;
        return this;
    }

    public LabelImageConfigBuilder setAlignTextRight(boolean alignTextRight) {
        this.alignTextRight = alignTextRight;
        return this;
    }

    public LabelImageConfigBuilder setFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public LabelImageConfigBuilder setTextColor(FontColor textColor) {
        this.textColor = textColor;
        return this;
    }

    public LabelImageConfigBuilder setMarginBetweenLabelImage(float marginBetweenLabelImage) {
        this.marginBetweenLabelImage = marginBetweenLabelImage;
        return this;
    }

    public LabelImageConfigBuilder setImageSideDimension(float imageSideDimension) {
        this.imageSideDimension = imageSideDimension;
        return this;
    }

    public LabelImageConfig build() {
        LabelImageConfig labelImageConfig = new LabelImageConfig();
        labelImageConfig.setText(text);
        labelImageConfig.setAlignTextRight(alignTextRight);
        labelImageConfig.setFontScale(fontScale);
        labelImageConfig.setImage(image);
        labelImageConfig.setLabelWidth(labelWidth);
        labelImageConfig.setSingleLineLabel(singleLineLabel);
        labelImageConfig.setFontConfig(fontConfig);
        if (image != null) {
            labelImageConfig.setImageSideDimension(imageSideDimension);
        }
        labelImageConfig.setTextColor(textColor);
        labelImageConfig.setMarginBetweenLabelImage(marginBetweenLabelImage);
        return labelImageConfig;
    }
}
