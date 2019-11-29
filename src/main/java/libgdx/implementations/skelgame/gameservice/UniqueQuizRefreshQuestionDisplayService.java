package libgdx.implementations.skelgame.gameservice;


import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.screen.AbstractScreen;

public class UniqueQuizRefreshQuestionDisplayService extends RefreshQuestionDisplayService<QuizGameService> {

    public UniqueQuizRefreshQuestionDisplayService(AbstractScreen abstractGameScreen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        super(abstractGameScreen, gameContext, allAnswerButtons);
    }

    @Override
    public void refreshQuestion(GameQuestionInfo gameQuestionInfo) {
    }

    @Override
    public void gameOverQuestion(GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo != null) {
            for (String answer : gameService.getAnswers()) {
                allAnswerButtons.get(answer.toLowerCase()).setButtonSkin(UniqueQuizQuestionContainerCreatorService.getCorrectQuizGameButtonSkin(gameService.getAllAnswerOptions()));
            }
        }
    }

}
