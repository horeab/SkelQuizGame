package libgdx.implementations.screens.implementations.astronomy;

import libgdx.constants.Contrast;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuizQuestionContainerCreatorService;
import libgdx.resources.FontManager;
import libgdx.implementations.screens.GameScreen;
import org.apache.commons.lang3.StringUtils;

public class AstronomyQuestionContainerCreatorService extends QuizQuestionContainerCreatorService {

    public AstronomyQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected float getQuestionFontScale(String questionToBeDisplayed, float fontScale, boolean longAnswerButtons) {
        return super.getQuestionFontScale(questionToBeDisplayed, fontScale, longAnswerButtons) * 1.2f;
    }

    @Override
    protected ButtonBuilder getAnswerButtonBuilder(String answer, ButtonSize buttonSize, GameButtonSkin buttonSkin) {
        return new ButtonBuilder().setContrast(Contrast.DARK).setWrappedText(StringUtils.capitalize(answer), buttonSize.getWidth() / 1.1f).setFontScale(getAnswerFontScale(answer, FontManager.getNormalBigFontDim())).setFixedButtonSize(buttonSize).setButtonSkin(buttonSkin);
    }

    protected void setContainerBackground() {
    }

}