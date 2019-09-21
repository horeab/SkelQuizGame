package libgdx.implementations.skelgame.gameservice;

import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.QuizGameButtonSkin;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.screen.AbstractScreen;

import java.util.Map;

public class HangmanRefreshQuestionDisplayService extends RefreshQuestionDisplayService<DependentAnswersQuizGameService> {

    public HangmanRefreshQuestionDisplayService(AbstractScreen abstractGameScreen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        super(abstractGameScreen, gameContext, allAnswerButtons);
    }

    @Override
    public void refreshQuestion(GameQuestionInfo gameQuestionInfo) {
    }

    @Override
    public void gameOverQuestion(GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo != null) {
            for (String answer : gameService.getAnswers()) {
                allAnswerButtons.get(answer).setButtonSkin(QuizGameButtonSkin.SQUARE_ANSWER_OPTION_CORRECT);
            }
        }
    }

}