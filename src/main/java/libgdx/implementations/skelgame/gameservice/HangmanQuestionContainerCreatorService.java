package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.resources.dimen.MainDimen;
import libgdx.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;

import java.util.ArrayList;
import java.util.List;

public class HangmanQuestionContainerCreatorService extends QuestionContainerCreatorService {

    public HangmanQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new GameControlsCreatorService().createSquareAnswerOptionsTable(getNrOfAnswersOnRow(), getNrOfAnswerRows(), new ArrayList<>(getAllAnswerButtons().values()));
    }

    public void refreshWord(){
        refreshQuestionDisplayService.refreshQuestion(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    protected MyButton createAnswerButton(final String answer) {
//        no Uppercase for ß, if uppercase its displayed ass SS
        return new ButtonBuilder(answer)
                .setFixedButtonSize(getNrOfAnswersOnRow() >= 7 ? GameButtonSize.HANGMAN_SMALL_BUTTON : GameButtonSize.HANGMAN_BUTTON)
                .setButtonSkin(GameButtonSkin.SQUARE).build();

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
