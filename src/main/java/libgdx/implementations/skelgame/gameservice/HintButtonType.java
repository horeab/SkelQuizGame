package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.ButtonSkin;
import libgdx.implementations.skelgame.QuizGameButtonSkin;

public enum HintButtonType {

    HINT_PRESS_RANDOM_ANSWER(QuizGameButtonSkin.HINT),
    HINT_DISABLE_TWO_ANSWERS(QuizGameButtonSkin.HINT),;

    private ButtonSkin buttonSkin;

    HintButtonType(ButtonSkin buttonSkin) {
        this.buttonSkin = buttonSkin;
    }

    public ButtonSkin getButtonSkin() {
        return buttonSkin;
    }
}
