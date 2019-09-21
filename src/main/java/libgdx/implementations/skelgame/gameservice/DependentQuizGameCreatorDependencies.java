package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.MyButton;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;

import java.util.Map;

public class DependentQuizGameCreatorDependencies extends CreatorDependencies {

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return DependentAnswersQuizGameService.class;
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen) {
        return new DependentQuizQuestionContainerCreatorService(gameContext, screen);
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new DependentQuizRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_DISABLE_TWO_ANSWERS;
    }

}
