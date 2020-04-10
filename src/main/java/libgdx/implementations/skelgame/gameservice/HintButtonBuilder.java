package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.screen.AbstractScreen;

public class HintButtonBuilder {

    private HintButtonType hintButtonType;
    private AbstractScreen gameScreen;
    private ImageButtonBuilder imageButtonBuilder;

    public HintButtonBuilder(HintButtonType hintButtonType, AbstractScreen gameScreen) {
        this.hintButtonType = hintButtonType;
        this.gameScreen = gameScreen;
        imageButtonBuilder = new ImageButtonBuilder(hintButtonType.getButtonSkin(), gameScreen).addFadeOutOnClick().animateZoomInZoomOut();

    }

    public ImageButtonBuilder getImageButtonBuilder() {
        return imageButtonBuilder;
    }

    public HintButton build() {
        return new HintButton(hintButtonType, imageButtonBuilder.build());
    }

}
