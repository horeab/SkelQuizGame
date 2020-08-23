package libgdx.implementations.screens.implementations.conthistory;

import libgdx.graphics.GraphicUtils;
import libgdx.implementations.conthistory.ConthistorySpecificResource;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.UniqueQuizQuestionContainerCreatorService;
import libgdx.implementations.screens.GameScreen;

public class ConthistoryQuestionContainerCreatorService extends UniqueQuizQuestionContainerCreatorService {

    public ConthistoryQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected float getQuestionFontScale(String questionToBeDisplayed, float fontScale, boolean longAnswerButtons) {
        return super.getQuestionFontScale(questionToBeDisplayed, fontScale, longAnswerButtons) * 1.2f;
    }

    protected void setContainerBackground() {
        questionContainer.setBackground(GraphicUtils.getNinePatch(ConthistorySpecificResource.question_background));
    }

}