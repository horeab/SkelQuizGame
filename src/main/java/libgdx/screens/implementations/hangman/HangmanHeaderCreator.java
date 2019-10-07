package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import javafx.scene.control.Tab;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HintButton;
import libgdx.implementations.skelgame.gameservice.HintButtonBuilder;
import libgdx.implementations.skelgame.gameservice.HintButtonType;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class HangmanHeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(GameContext gameContext, List<HintButton> hintButtonList, Table hintTable) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        MyWrappedLabel wrappedLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setTextColor(new SinglePlayerLevelFinishedService().isGameWon(gameUser) ? FontColor.GREEN : FontColor.BLACK)
                .setText(gameUser.getFinishedQuestions() + "/" + gameUser.getTotalNrOfQuestions())
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        wrappedLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        table.add().width(ScreenDimensionsManager.getScreenWidthValue(40));
        table.add(wrappedLabel).width(ScreenDimensionsManager.getScreenWidthValue(20));
        table.add(hintTable).width(ScreenDimensionsManager.getScreenWidthValue(40));
        return table;
    }

}
