package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;

import java.util.Map;

public abstract class CreatorDependencies {

    public GameService getGameService(Question question) {
        return GameServiceContainer.getGameService(getGameServiceClass(), question);
    }

    public QuestionCreator getQuestionCreator(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        return new QuestionCreator(questionDifficulty, questionCategory);
    }

    public QuestionCreator getQuestionCreator() {
        return new QuestionCreator();
    }

    public abstract Class<? extends GameService> getGameServiceClass();

    public abstract RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons);

    public abstract QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen);

    public abstract HintButtonType getHintButtonType();

}
