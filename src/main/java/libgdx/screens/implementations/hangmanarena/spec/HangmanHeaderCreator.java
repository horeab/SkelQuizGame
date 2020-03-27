package libgdx.screens.implementations.hangmanarena.spec;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HintButton;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

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
        float imgSide = horizontalGeneralMarginDimen * 5;
        for (int i = 0; i < gameUser.getTotalNrOfQuestions(); i++) {
            HangmanArenaSpecificResource resource = HangmanArenaSpecificResource.btn_hint;
            if (gameUser.getGameQuestionInfo(i).getStatus() == GameQuestionInfoStatus.WON) {
                resource= HangmanArenaSpecificResource.btn_hint_disabled;
            } else if (gameUser.getGameQuestionInfo(i).getStatus() == GameQuestionInfoStatus.LOST) {
                resource= HangmanArenaSpecificResource.bomb;
            }
            table.add(GraphicUtils.getImage(resource)).width(imgSide)
                    .height(imgSide).pad(horizontalGeneralMarginDimen);
        }
        return table;
    }

}
