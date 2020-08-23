package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;

public class HangmanQuestionContainerCreatorService extends QuestionContainerCreatorService {

    public static String AVAILABLE_TRIES_IMAGE_CELL_NAME = "AVAILABLE_TRIES_IMAGE_CELL_NAME";

    public HangmanQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        refreshWord();
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new GameControlsCreatorService().createSquareAnswerOptionsTable(getNrOfAnswersOnRow(), getNrOfAnswerRows(), new ArrayList<>(getAllAnswerButtons().values()));
    }

    public void refreshWord() {
        refreshQuestionDisplayService.refreshQuestion(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    protected MyButton createAnswerButton(final String answer) {
//        no Uppercase for ß, if uppercase its displayed ass SS
        return new ButtonBuilder(answer, FontManager.getBigFontDim())
                .setButtonSkin(GameButtonSkin.SQUARE)
                .setFixedButtonSize(getNrOfAnswersOnRow() >= 7 ? GameButtonSize.HANGMAN_SMALL_BUTTON : GameButtonSize.HANGMAN_BUTTON)
                .build();

    }

    @Override
    protected void setContainerBackground() {
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = super.createQuestionTable();
        String questionToBeDisplayed = gameService.getQuestionToBeDisplayed();
        Image questionImage = gameService.getQuestionImage();
        if (StringUtils.isNotBlank(questionToBeDisplayed)) {
            MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(questionToBeDisplayed).build());
            questionContainer.add(questionLabel).padBottom(MainDimen.vertical_general_margin.getDimen() * 16).row();
        } else if (questionImage != null) {
            MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(gameContext.getQuestion().getQuestionCategory().getIndex())).build());
            questionContainer.add(questionLabel).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
            Table imageTable = createQuestionImage(questionImage, ScreenDimensionsManager.getScreenWidthValue(80), ScreenDimensionsManager.getScreenHeightValue(25));
            questionContainer.add(imageTable);
            questionContainer.add(createAvailableTriesTableForQuestionWithImage()).padLeft(MainDimen.horizontal_general_margin.getDimen());
        }
        return questionTable;
    }

    private Table createAvailableTriesTableForQuestionWithImage() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen() / 3;
        for (int i = 0; i < HangmanGameService.GAME_OVER_WRONG_LETTERS; i++) {
            Image image = GraphicUtils.getImage(MainResource.heart_full);
            image.setName(AVAILABLE_TRIES_IMAGE_CELL_NAME + i);
            float dimen = verticalGeneralMarginDimen * 5;
            table.add(image).padBottom(verticalGeneralMarginDimen).width(dimen).height(dimen).row();
        }
        return table;
    }

    public int getNrOfAnswerRows() {
        return nrOfAnswerRows();
    }

    public static int nrOfAnswerRows() {
        return 5;
    }

    public int getNrOfAnswersOnRow() {
        return (int) Math.ceil(gameService.getAllAnswerOptions().size() / Float.valueOf(getNrOfAnswerRows()));
    }

    @Override
    public ButtonSkin correctAnswerSkin() {
        return GameButtonSkin.SQUARE_CORRECT;
    }

    @Override
    public ButtonSkin wrongAnswerSkin() {
        return GameButtonSkin.SQUARE_WRONG;
    }

    public Table createSquareAnswerOptionsTable(List<MyButton> allAnswerButtons) {
        int answerIndex = 0;
        Table buttonTable = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        for (int i = getNrOfAnswerRows(); i >= 0; i--) {
            Table buttonRow = new Table();
            for (int j = 0; j < getNrOfAnswersOnRow(); j++) {
                if (answerIndex < allAnswerButtons.size()) {
                    MyButton button = allAnswerButtons.get(answerIndex);
                    buttonRow.add(button).width(button.getWidth()).height(button.getHeight()).padRight(ScreenDimensionsManager.getScreenWidthValue(0.65f));
                    answerIndex++;
                }
            }
            buttonTable.add(buttonRow).padBottom(verticalGeneralMarginDimen / 2).row();
        }
        return buttonTable;
    }
}
