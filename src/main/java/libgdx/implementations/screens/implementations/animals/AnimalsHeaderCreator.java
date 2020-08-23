package libgdx.implementations.screens.implementations.animals;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HintButton;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

import java.util.List;

public class AnimalsHeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(GameContext gameContext, List<HintButton> hintButtonList, Table hintTable) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        MyWrappedLabel wrappedLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontColor(new SinglePlayerLevelFinishedService().isGameWon(gameUser) ? FontColor.GREEN : FontColor.BLACK)
                .setText(gameUser.getFinishedQuestions() + "/" + gameUser.getTotalNrOfQuestions())
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        wrappedLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        table.add().width(ScreenDimensionsManager.getScreenWidthValue(40));
        table.add(wrappedLabel).width(ScreenDimensionsManager.getScreenWidthValue(20));
        table.add(hintTable).width(ScreenDimensionsManager.getScreenWidthValue(40));
        return table;
    }

}
