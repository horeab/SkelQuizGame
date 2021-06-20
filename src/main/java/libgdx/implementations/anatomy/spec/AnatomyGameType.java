package libgdx.implementations.anatomy.spec;

import libgdx.controls.button.ButtonSkin;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.resources.gamelabel.GameLabel;
import libgdx.resources.gamelabel.MainGameLabel;

public enum  AnatomyGameType {

    GENERALK(MainGameLabel.l_general_knowledge, GameButtonSkin.ANATOMY_GENERALK),
    IDENTIFY(MainGameLabel.l_identification_quiz, GameButtonSkin.ANATOMY_IDENTIFY),;

    GameLabel levelName;
    ButtonSkin buttonSkin;

    AnatomyGameType(GameLabel levelName, ButtonSkin buttonSkin) {
        this.levelName = levelName;
        this.buttonSkin = buttonSkin;
    }

    public GameLabel getLevelName() {
        return levelName;
    }

    public ButtonSkin getButtonSkin() {
        return buttonSkin;
    }
}
