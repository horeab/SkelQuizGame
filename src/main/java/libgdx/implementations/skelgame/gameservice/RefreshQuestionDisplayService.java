package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screen.AbstractScreen;

import java.util.Map;

public abstract class RefreshQuestionDisplayService<TGameService extends GameService> {

    protected TGameService gameService;
    protected AbstractScreen abstractGameScreen;
    protected GameUser currentUserGameUser;
    protected Map<String, MyButton> allAnswerButtons;

    public RefreshQuestionDisplayService(AbstractScreen abstractScreen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        currentUserGameUser = gameContext.getCurrentUserGameUser();
        gameService = (TGameService) gameContext.getCurrentUserCreatorDependencies().getGameService(currentUserGameUser.getGameQuestionInfo().getQuestion());
        this.abstractGameScreen = abstractScreen;
        this.allAnswerButtons = allAnswerButtons;
    }

    public GameUser getCurrentUserGameUser() {
        return currentUserGameUser;
    }

    public AbstractScreen getAbstractGameScreen() {
        return abstractGameScreen;
    }

    public abstract void refreshQuestion(GameQuestionInfo gameQuestionInfo);

    public abstract void gameOverQuestion(GameQuestionInfo gameQuestionInfo);
}
