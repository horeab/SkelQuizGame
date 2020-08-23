package libgdx.implementations.screens.implementations.animals.spec;

import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screen.AbstractScreen;

public abstract class AbstractGameScreenBackgroundCreator {

    private AbstractScreen abstractGameScreen;
    private GameQuestionInfo gameQuestionInfo;

    public AbstractGameScreenBackgroundCreator(AbstractScreen abstractGameScreen, GameUser gameUser) {
        this.abstractGameScreen = abstractGameScreen;
        this.gameQuestionInfo = gameUser.getGameQuestionInfo();
    }

    public GameQuestionInfo getGameQuestionInfo() {
        return gameQuestionInfo;
    }

    public AbstractScreen getAbstractGameScreen() {
        return abstractGameScreen;
    }

    public abstract void createBackground();

    public abstract void refreshBackground(int nrOfWrongLettersPressed);

}
