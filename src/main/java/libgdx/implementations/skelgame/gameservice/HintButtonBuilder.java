package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.screen.AbstractScreen;

public class HintButtonBuilder {

    private HintButtonType hintButtonType;
    private AbstractScreen gameScreen;

    public HintButtonBuilder(HintButtonType hintButtonType, AbstractScreen gameScreen) {
        this.hintButtonType = hintButtonType;
        this.gameScreen = gameScreen;
    }

    public HintButton build() {
        return new HintButton(hintButtonType, new ImageButtonBuilder(hintButtonType.getButtonSkin(), gameScreen).addFadeOutOnClick().animateZoomInZoomOut().build());
    }

}
