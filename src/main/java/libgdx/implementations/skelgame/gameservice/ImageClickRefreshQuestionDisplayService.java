package libgdx.implementations.skelgame.gameservice;


import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.screen.AbstractScreen;

public class ImageClickRefreshQuestionDisplayService extends RefreshQuestionDisplayService<ImageClickGameService> {

    public ImageClickRefreshQuestionDisplayService(AbstractScreen abstractGameScreen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        super(abstractGameScreen, gameContext, allAnswerButtons);
    }

    @Override
    public void refreshQuestion(GameQuestionInfo gameQuestionInfo) {
    }

    @Override
    public void gameOverQuestion(GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo != null) {
            for (String answer : gameService.getAnswers(gameQuestionInfo.getQuestion())) {
//                allAnswerButtons.get(answer).setButtonSkin(ButtonSkin.ANSWER_IMAGE_CLICK_CORRECT);
            }
        }
    }

}
