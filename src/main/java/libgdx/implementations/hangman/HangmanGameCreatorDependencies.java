package libgdx.implementations.hangman;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;

import java.util.Map;

public class HangmanGameCreatorDependencies extends CreatorDependencies {

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return HangmanGameService.class;
    }

    @Override
    public QuestionCreator getQuestionCreator(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        return new QuestionCreator(questionDifficulty, questionCategory) {
            @Override
            protected boolean isQuestionValid(Question question) {
                return question.getQuestionString().length() > 3 && question.getQuestionString().length() < 21;
            }
        };
    }

    @Override
    public RefreshQuestionDisplayService getRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        return new HangmanRefreshQuestionDisplayService(screen, gameContext, allAnswerButtons);
    }

    @Override
    public QuestionContainerCreatorService getQuestionContainerCreatorService(GameContext gameContext, GameScreen screen) {
        return new HangmanQuestionContainerCreatorService(gameContext, screen);
    }

    @Override
    public HintButtonType getHintButtonType() {
        return HintButtonType.HINT_PRESS_RANDOM_ANSWER;
    }
}
