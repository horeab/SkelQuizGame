package libgdx.implementations.screens.implementations.anatomy;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuizGameService;
import libgdx.implementations.skelgame.gameservice.QuizQuestionContainerCreatorService;
import libgdx.resources.FontManager;
import org.apache.commons.lang3.StringUtils;

public class AnatomyQuestionContainerCreatorService extends QuizQuestionContainerCreatorService {


    public AnatomyQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected float getQuestionFontScale(String questionToBeDisplayed, float fontScale, boolean longAnswerButtons) {
        return super.getQuestionFontScale(questionToBeDisplayed, fontScale, longAnswerButtons) * 1.2f;
    }

    @Override
    protected ButtonBuilder getAnswerButtonBuilder(String answer, ButtonSize buttonSize, GameButtonSkin buttonSkin) {
        return new ButtonBuilder().setWrappedText(StringUtils.capitalize(answer), buttonSize.getWidth() / 1.1f)
                .setFontScale(getAnswerFontScale(answer, FontManager.getNormalBigFontDim())).setFixedButtonSize(buttonSize)
                .setButtonSkin(buttonSkin);
    }

    protected void setContainerBackground() {
    }


    @Override
    protected QuizGameService getGameService(GameContext gameContext) {
        return new AnatomyUniqueQuizGameService(gameContext.getQuestion());
    }
}