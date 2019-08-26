package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;

public abstract class CreatorDependencies {

    public GameService getGameService(Question question) {
        return GameServiceContainer.getGameService(getGameServiceClass(), question);
    }

    public QuestionCreator getQuestionCreator(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        return new QuestionCreator(questionDifficulty, questionCategory);
    }

    public abstract Class<? extends GameService> getGameServiceClass();

    public abstract QuestionContainerCreatorService getQuestionContainerCreatorService(GameUser gameUser, GameContext gameContext, AbstractScreen screen);

    public abstract HintButtonType getHintButtonType();

}
