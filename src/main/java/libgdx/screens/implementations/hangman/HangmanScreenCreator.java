package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HangmanScreenCreator {

    private Map<String, MyButton> createAnswerOptionsButtons(List<String> allAnswerOptions) {
        Map<String, MyButton> allAnswerButtons = new LinkedHashMap<>();
        for (String answer : allAnswerOptions) {
            MyButton button = createAnswerButton(answer);
            allAnswerButtons.put(answer, button);
        }
        return allAnswerButtons;
    }

    protected MyButton createAnswerButton(final String answer) {
//        no Uppercase for ß, if uppercase its displayed ass SS
        return new ButtonBuilder(answer)
                .setFixedButtonSize(getNrOfAnswersOnRow() >= 7 ? ButtonSize.HANGMAN_SMALL_BUTTON : ButtonSize.HANGMAN_BUTTON)
                .setButtonSkin(ButtonSkin.SQUARE).build();

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

    public Table createSquareAnswerOptionsTable(int nrOfAnswersOnRow,
                                                int getNrOfAnswerRows,
                                                List<MyButton> allAnswerButtons) {
        int answerIndex = 0;
        Table buttonTable = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        for (int i = getNrOfAnswerRows; i >= 0; i--) {
            Table buttonRow = new Table();
            for (int j = 0; j < nrOfAnswersOnRow; j++) {
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
