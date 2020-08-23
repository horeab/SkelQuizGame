package libgdx.implementations.screens.implementations.periodictable;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.constants.Contrast;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameControlsCreatorService;
import libgdx.implementations.skelgame.gameservice.HintButtonBuilder;
import libgdx.implementations.skelgame.gameservice.QuizQuestionContainerCreatorService;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PeriodicTableQuestionContainerCreatorService extends QuizQuestionContainerCreatorService {

    public PeriodicTableQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = new Table();
        questionTable.add(questionContainer).pad(MainDimen.horizontal_general_margin.getDimen() * 4);
        setContainerBackground();
        System.out.println(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionString());
        String elName = gameService.getQuestionToBeDisplayed();
        String categoryText = SpecificPropertiesUtils.getQuestionCategoryLabel(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionCategory().getIndex());
        MyWrappedLabel elNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2))
                .setText(elName).build());
        MyWrappedLabel categLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 1.5f))
                .setText(categoryText).build());
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table container = new Table();
        container.add(elNameLabel).row();
        container.add(categLabel);
        questionContainer.add(container).pad(verticalGeneralMarginDimen).row();
        return questionTable;
    }

    @Override
    protected void enrichHintButtonBuilder(HintButtonBuilder hintButtonBuilder) {
        super.enrichHintButtonBuilder(hintButtonBuilder);
        hintButtonBuilder.getImageButtonBuilder().clearChangeListeners();
        hintButtonBuilder.getImageButtonBuilder().setFixedButtonSize(GameButtonSize.PERIODICTABLE_MENU_BUTTON);
    }

    @Override
    protected MyButton createAnswerButton(final String answer) {
        ButtonSize buttonSize = GameButtonSize.PERIODICTABLE_ANSWER_BUTTON;
        GameButtonSkin buttonSkin = GameButtonSkin.SQUARE_ANSWER_OPTION;
        return getAnswerButtonBuilder(answer, buttonSize, buttonSkin).build();
    }

    @Override
    protected float getQuestionFontScale(String questionToBeDisplayed, float fontScale, boolean longAnswerButtons) {
        return super.getQuestionFontScale(questionToBeDisplayed, fontScale, longAnswerButtons) * 1.2f;
    }

    @Override
    protected ButtonBuilder getAnswerButtonBuilder(String answer, ButtonSize buttonSize, GameButtonSkin buttonSkin) {
        return new ButtonBuilder().setContrast(Contrast.DARK)
                .setFontConfig(new FontConfig(getAnswerFontScale(answer, FontConfig.FONT_SIZE * 1.1f)))
                .setWrappedText(StringUtils.capitalize(answer), buttonSize.getWidth() / 1.1f)
                .setFixedButtonSize(buttonSize).setButtonSkin(buttonSkin);
    }

    protected float getAnswerFontScale(String answerToBeDisplayed, float fontScale) {
        float factor = 1f;
//        factor = Arrays.asList(Language.th).contains(Language.valueOf(Game.getInstance().getAppInfoService().getLanguage())) ? 1.5f : factor;
        //if there are long answer buttons, the question fontScale should be smaller
        float increaseFactor = 0.05f;
        int increaseWordCount = 5;
        for (int standardAnswerLength = GameControlsCreatorService.getLongAnswerLimit();
             standardAnswerLength < 100; standardAnswerLength = standardAnswerLength + increaseWordCount)
            if (answerToBeDisplayed.length() > standardAnswerLength) {
                factor = factor + increaseFactor;
            } else {
                break;
            }
        return fontScale / factor;
    }

    @Override
    public Table createAnswerOptionsTable() {
        Table buttonTable = new Table();
        int answerIndex = 0;
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        int nrAnswersOnRow = 3;
        List<MyButton> answersBtn = new ArrayList<>(getAllAnswerButtons().values());
        for (int i = nrAnswersOnRow; i >= 0; i--) {
            Table buttonRow = new Table();
            for (int j = 0; j < nrAnswersOnRow; j++) {
                if (answerIndex < answersBtn.size()) {
                    MyButton button = answersBtn.get(answerIndex);
                    buttonRow.add(button).width(button.getWidth()).height(button.getHeight()).padRight(ScreenDimensionsManager.getScreenWidthValue(0.65f));
                    answerIndex++;
                }
            }
            buttonTable.add(buttonRow).padBottom(verticalGeneralMarginDimen / 2).row();
        }
        return buttonTable;
    }

    protected void setContainerBackground() {
    }

}