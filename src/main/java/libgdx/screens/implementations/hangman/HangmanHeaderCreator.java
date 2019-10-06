package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfig;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.gameservice.UsersWithLevelFinished;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.model.FontColor;

public class HangmanHeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(GameContext gameContext) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setTextColor(new SinglePlayerLevelFinishedService().isGameWon(gameUser) ? FontColor.GREEN : FontColor.BLACK)
                .setText(gameUser.getFinishedQuestions() + "/" + gameUser.getTotalNrOfQuestions())
                .setFontScale(FontManager.getBigFontDim()).build())).pad(dimen);
        table.add().growX();
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return table;
    }

}
