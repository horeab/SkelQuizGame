package libgdx.implementations.skelgame.gameservice;

import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screen.AbstractScreen;

public class DependentQuizGameCreatorDependencies extends CreatorDependencies {

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return DependentAnswersQuizGameService.class;
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameUser gameUser, GameContext gameContext, AbstractScreen screen) {
        return new DependentQuizQuestionContainerCreatorService(gameUser, gameContext, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_DISABLE_TWO_ANSWERS;
    }

}
