package libgdx.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.conthistory.ConthistorySpecificResource;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;

public class HeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(GameContext gameContext) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        table.add(createQuestionsTable(gameContext.getCurrentUserGameUser())).pad(dimen);
        table.add().growX();
        return table;
    }


    private Table createQuestionsTable(GameUser gameUser) {
        Table table = new Table();
        float hm = MainDimen.horizontal_general_margin.getDimen();
        float qTableSideDimen = hm * 3;
        for (int i = 0; i < gameUser.getTotalNrOfQuestions(); i++) {
            Image image = GraphicUtils.getImage(getQuestionTableBackgr(i, gameUser));
            table.add(image).pad(hm / 3).padTop(hm * 3).height(qTableSideDimen).width(qTableSideDimen);
        }
        return table;
    }

    private Res getQuestionTableBackgr(int qNr, GameUser gameUser) {
        Res background = ConthistorySpecificResource.star_disabled;
        if (gameUser.getGameQuestionInfo(qNr).getStatus() == GameQuestionInfoStatus.WON) {
            background = ConthistorySpecificResource.star;
        } else if (gameUser.getGameQuestionInfo(qNr).getStatus() == GameQuestionInfoStatus.LOST) {
            background = ConthistorySpecificResource.star_wrong;
        }
        return background;
    }
}
