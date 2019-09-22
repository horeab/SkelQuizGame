package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;

public class HangmanHeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(GameContext gameContext) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        table.add(new MyWrappedLabel(gameUser.getFinishedQuestions() + "/" + gameUser.getTotalNrOfQuestions())).pad(dimen);
        table.add().growX();
        return table;
    }

}
