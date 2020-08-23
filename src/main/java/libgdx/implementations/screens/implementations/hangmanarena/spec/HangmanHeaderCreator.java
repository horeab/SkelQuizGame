package libgdx.implementations.screens.implementations.hangmanarena.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class HangmanHeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(GameContext gameContext, Table hintTable) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        table.add(hintTable).width(ScreenDimensionsManager.getScreenWidthValue(40));
        return table;
    }

    public Table refreshRemainingAnswersTable(GameContext gameContext) {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        String remainingAnswersTableName = "RemainingAnswersTable";
        Table table = Game.getInstance().getAbstractScreen().getRoot().findActor(remainingAnswersTableName);
        if (table == null) {
            table = new Table();
        }
        table.clearChildren();
        table.setName(remainingAnswersTableName);
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        float imgSide = horizontalGeneralMarginDimen * 6;
        for (int i = 0; i < gameUser.getTotalNrOfQuestions(); i++) {
            Res resource = HangmanArenaSpecificResource.unkownq;
            if (gameUser.getGameQuestionInfo(i).getStatus() == GameQuestionInfoStatus.WON) {
                resource = HangmanArenaSpecificResource.correctq;
            } else if (gameUser.getGameQuestionInfo(i).getStatus() == GameQuestionInfoStatus.LOST) {
                resource = HangmanArenaSpecificResource.wrongq;
            }
            table.add(GraphicUtils.getImage(resource)).width(imgSide)
                    .height(imgSide).pad(horizontalGeneralMarginDimen / 3);
        }
        return table;
    }

}
