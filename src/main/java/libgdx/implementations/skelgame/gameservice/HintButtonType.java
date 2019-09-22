package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.ButtonSkin;
import libgdx.implementations.skelgame.GameButtonSkin;

public enum HintButtonType {

    HINT_PRESS_RANDOM_ANSWER(GameButtonSkin.HINT),
    HINT_DISABLE_TWO_ANSWERS(GameButtonSkin.HINT),;

    private ButtonSkin buttonSkin;

    HintButtonType(ButtonSkin buttonSkin) {
        this.buttonSkin = buttonSkin;
    }

    public ButtonSkin getButtonSkin() {
        return buttonSkin;
    }
}
