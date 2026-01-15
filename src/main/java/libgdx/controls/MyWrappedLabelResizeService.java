package libgdx.controls;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;

public class MyWrappedLabelResizeService {

    private MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder;

    public MyWrappedLabelResizeService(MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder) {
        this.myWrappedLabelConfigBuilder = myWrappedLabelConfigBuilder;
    }

    public MyWrappedLabel resizeMaxHeight(float maxHeight) {
        float fontScaleMultiplier = 1f;
        MyWrappedLabel label = createQuestionLabel(fontScaleMultiplier);
        while (label.getPrefHeight() > maxHeight) {
            fontScaleMultiplier = fontScaleMultiplier - 0.1f;
            label = createQuestionLabel(fontScaleMultiplier);
        }
        return label;
    }

    private MyWrappedLabel createQuestionLabel(float fontScaleMultiplier) {
        return new MyWrappedLabel(myWrappedLabelConfigBuilder.setFontScale(myWrappedLabelConfigBuilder.getFontScale() * fontScaleMultiplier).build());
    }

}
