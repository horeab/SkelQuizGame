package libgdx.controls.button.builders;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.labelimage.LabelImage;
import libgdx.controls.labelimage.LabelImageConfigBuilder;
import libgdx.game.Game;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.model.FontConfig;

public class ButtonWithIconBuilder extends ButtonBuilder {

    private boolean singleLineLabel;
    private String text;
    private Res icon;
    private Float fontScale;
    private Float imageSideDimen;
    private Float labelWidth;

    public ButtonWithIconBuilder(String text, Res icon) {
        this.text = text;
        this.icon = icon;
    }

    @Override
    public MyButton build() {
        LabelImage table = createTableLabelImage(text, icon);
        addCenterTextImageColumn(table);
        return super.build();
    }

    public ButtonWithIconBuilder setFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public ButtonWithIconBuilder setLabelWidth(float labelWidth) {
        this.labelWidth = labelWidth;
        return this;
    }

    public ButtonWithIconBuilder setSingleLineLabel() {
        singleLineLabel = true;
        return this;
    }

    public ButtonWithIconBuilder setImageSideDimen(float imageSideDimen) {
        this.imageSideDimen = imageSideDimen;
        return this;
    }

    @Override
    protected LabelImage createTableLabelImage(String text, Res icon) {
        LabelImageConfigBuilder labelImageConfigBuilder = getLabelImageConfigBuilder(text, icon)
                .setFontScale(Game.getInstance().getAppInfoService().isPortraitMode() ? FontManager.getNormalBigFontDim() : FontManager.getBigFontDim());
        if (fontConfig != null) {
            labelImageConfigBuilder.setFontConfig(fontConfig);
        } else {
            if (fontScale != null) {
                labelImageConfigBuilder.setFontScale(fontScale);
            }
            if (fontColor != null) {
                labelImageConfigBuilder.setFontConfig(new FontConfig(fontColor.getColor()));
            }
        }
        if (imageSideDimen != null) {
            labelImageConfigBuilder.setImageSideDimension(imageSideDimen);
        }
        return new LabelImage(labelImageConfigBuilder.build());
    }

    private LabelImageConfigBuilder getLabelImageConfigBuilder(String text, Res icon) {
        LabelImageConfigBuilder builder = new LabelImageConfigBuilder()
                .setImage(icon)
                .setImageSideDimension(getButtonSize().getHeight())
                .setMarginBetweenLabelImage(MainDimen.horizontal_general_margin.getDimen())
                .setText(text)
                .setAlignTextRight(true);
        if (singleLineLabel) {
            builder.setSingleLineLabel();
        } else {
            builder.setWrappedLineLabel(labelWidth != null ? labelWidth : getButtonSize().getWidth() * 3);
        }
        return builder;
    }

}
