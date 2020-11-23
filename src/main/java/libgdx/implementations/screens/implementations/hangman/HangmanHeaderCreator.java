package libgdx.implementations.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
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

}
