package libgdx.implementations.skelgame.gameservice;

import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;
import libgdx.ui.model.game.GameUser;
import libgdx.ui.screens.game.creator.utils.AbstractGameScreenBackgroundCreator;
import libgdx.ui.screens.game.screens.AbstractGameScreen;
import libgdx.ui.services.gametypes.types.imageclickgame.screencreator.ImageClickQuestionContainerCreatorService;
import libgdx.ui.services.gametypes.types.imageclickgame.screencreator.ImageClickScreenBackgroundCreator;
import libgdx.ui.services.gametypes.types.imageclickgame.service.ImageClickGameService;
import libgdx.ui.services.gametypes.types.imageclickgame.service.ImageClickRefreshQuestionDisplayService;

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
        return new ImageClickQuestionContainerCreatorService(gameUser, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_DISABLE_TWO_ANSWERS;
    }

}
