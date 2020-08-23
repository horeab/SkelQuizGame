package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.implementations.screens.GameScreen;

public abstract class QuizQuestionContainerCreatorService extends QuestionContainerCreatorService<QuizGameService> {

    public QuizQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = super.createQuestionTable();
        Image questionImage = gameService.getQuestionImage();
        String questionToBeDisplayed = gameService.getQuestionToBeDisplayed();
        if (StringUtils.isBlank(questionToBeDisplayed)) {
            questionToBeDisplayed = SpecificPropertiesUtils.getQuestionCategoryLabel(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionCategory().getIndex());
        }
        MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder = getMyWrappedLabelConfigBuilder(questionImage, questionToBeDisplayed);
        MyWrappedLabel questionLabel = new MyWrappedLabel(myWrappedLabelConfigBuilder.setStyleDependingOnContrast().build());
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        questionContainer.add(questionLabel).pad(verticalGeneralMarginDimen).row();
        if (questionImage != null) {
            addQuestionImage(questionImage);
        }
        return questionTable;
    }

    protected MyWrappedLabelConfigBuilder getMyWrappedLabelConfigBuilder(Image questionImage, String questionToBeDisplayed) {
        MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder = new MyWrappedLabelConfigBuilder().setText(questionToBeDisplayed);
        if (questionImage == null) {
            myWrappedLabelConfigBuilder.setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f));
        }
        myWrappedLabelConfigBuilder.setFontScale(getQuestionFontScale(questionToBeDisplayed,
                myWrappedLabelConfigBuilder.getFontScale(), GameControlsCreatorService.longAnswerButtons(gameService.getAllAnswerOptions())));
        return myWrappedLabelConfigBuilder;
    }

    protected float getQuestionFontScale(String questionToBeDisplayed, float fontScale, boolean longAnswerButtons) {
        float factor = 1f;
        //if there are long answer buttons, the question fontScale should be smaller
        float increaseFactor = longAnswerButtons ? 0.035f : 0.018f;
        int increaseWordCount = 5;
        for (int standardQuestionLength = 160; standardQuestionLength < 600; standardQuestionLength = standardQuestionLength + increaseWordCount)
            if (questionToBeDisplayed.length() > standardQuestionLength) {
                factor = factor + increaseFactor;
            } else {
                break;
            }
        return fontScale / factor;
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new GameControlsCreatorService().createAnswerOptionsTable(gameService.getAllAnswerOptions(),
                getNrOfAnswersOnRow(),
                getNrOfAnswerRows(),
                new ArrayList<>(getAllAnswerButtons().values()));
    }

    @Override
    public int getNrOfAnswerRows() {
        return 2;
    }

    @Override
    public int getNrOfAnswersOnRow() {
        return 2;
    }

    @Override
    public GameButtonSkin correctAnswerSkin() {
        return getCorrectQuizGameButtonSkin(gameService.getAllAnswerOptions());
    }

    public static GameButtonSkin getCorrectQuizGameButtonSkin(List<String> allAnswerOptions) {
        return GameControlsCreatorService.longAnswerButtons(allAnswerOptions) ? GameButtonSkin.LONG_ANSWER_OPTION_CORRECT : GameButtonSkin.SQUARE_ANSWER_OPTION_CORRECT;
    }

    @Override
    public GameButtonSkin wrongAnswerSkin() {
        return GameControlsCreatorService.longAnswerButtons(gameService.getAllAnswerOptions()) ? GameButtonSkin.LONG_ANSWER_OPTION_WRONG : GameButtonSkin.SQUARE_ANSWER_OPTION_WRONG;
    }

    @Override
    protected MyButton createAnswerButton(final String answer) {
        ButtonSize buttonSize = GameButtonSize.SQUARE_QUIZ_OPTION_ANSWER;
        GameButtonSkin buttonSkin = GameButtonSkin.SQUARE_ANSWER_OPTION;
        if (GameControlsCreatorService.longAnswerButtons(gameService.getAllAnswerOptions())) {
            buttonSize = GameButtonSize.LONG_QUIZ_OPTION_ANSWER;
            buttonSkin = GameButtonSkin.LONG_ANSWER_OPTION;
        }
        return getAnswerButtonBuilder(answer, buttonSize, buttonSkin).build();
    }

    protected ButtonBuilder getAnswerButtonBuilder(String answer, ButtonSize buttonSize, GameButtonSkin buttonSkin) {
        return new ButtonBuilder().setWrappedText(answer, buttonSize.getWidth() / 1.1f).setFontScale(getAnswerFontScale(answer, FontManager.getNormalBigFontDim())).setFixedButtonSize(buttonSize).setButtonSkin(buttonSkin);
    }

    protected float getAnswerFontScale(String answerToBeDisplayed, float fontScale) {
        float factor = 1f;
        //if there are long answer buttons, the question fontScale should be smaller
        float increaseFactor = 0.05f;
        int increaseWordCount = 5;
        for (int standardAnswerLength = GameControlsCreatorService.getLongAnswerLimit(); standardAnswerLength < 100; standardAnswerLength = standardAnswerLength + increaseWordCount)
            if (answerToBeDisplayed.length() > standardAnswerLength) {
                factor = factor + increaseFactor;
            } else {
                break;
            }
        return fontScale / factor;
    }

}
