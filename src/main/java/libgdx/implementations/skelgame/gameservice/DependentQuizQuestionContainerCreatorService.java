package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screen.AbstractScreen;

public class DependentQuizQuestionContainerCreatorService extends QuizQuestionContainerCreatorService {

    public DependentQuizQuestionContainerCreatorService(GameUser gameUser, GameContext gameContext, AbstractScreen abstractGameScreen) {
        super(gameUser, gameContext, abstractGameScreen);
    }

}
