package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.screens.GameScreen;

public class DependentQuizQuestionContainerCreatorService extends QuizQuestionContainerCreatorService {

    public DependentQuizQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected void setContainerBackground() {
    }
}
