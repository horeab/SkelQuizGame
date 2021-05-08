package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;

import java.util.Map;

public class HeaderCreator {

    public static String HEADER_TABLE_NAME = "HEADER_TABLE_NAME";

    public Table createHeaderTable(Map<Integer, GameQuestionInfoStatus> allQuestions) {
        Table table = new Table();
        table.setName(HEADER_TABLE_NAME);
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        table.add(createQuestionsTable(allQuestions)).pad(dimen);
        table.add().growX();
        return table;
    }


    private Table createQuestionsTable(Map<Integer, GameQuestionInfoStatus> allQuestions) {
        Table table = new Table();
        float hm = MainDimen.horizontal_general_margin.getDimen();
        float qTableSideDimen = hm * 3;
        for (Map.Entry<Integer, GameQuestionInfoStatus> e : allQuestions.entrySet()) {
            Image image = GraphicUtils.getImage(getQuestionTableBackgr(e.getValue()));
            table.add(image).pad(hm / 3).padTop(hm * 3).height(qTableSideDimen).width(qTableSideDimen);
        }
        return table;
    }

    private Res getQuestionTableBackgr(GameQuestionInfoStatus gameQuestionInfoStatus) {
        Res background = AstronomySpecificResource.star_disabled;
        if (gameQuestionInfoStatus == GameQuestionInfoStatus.WON) {
            background = AstronomySpecificResource.star;
        } else if (gameQuestionInfoStatus == GameQuestionInfoStatus.LOST) {
            background = AstronomySpecificResource.star_wrong;
        }
        return background;
    }
}
