package libgdx.implementations.skelgame.gameservice;

import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;

public class ImageClickGameCreatorDependencies extends CreatorDependencies {

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return ImageClickGameService.class;
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new ImageClickRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext,  GameScreen screen) {
        return new ImageClickQuestionContainerCreatorService(gameContext, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_DISABLE_TWO_ANSWERS;
    }

}
