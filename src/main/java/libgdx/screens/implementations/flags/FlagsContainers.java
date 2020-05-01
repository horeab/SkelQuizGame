package libgdx.screens.implementations.flags;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignStoreService;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.flags.FlagsSpecificResource;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;

import java.util.List;

public class FlagsContainers {

    public static int getTotalCorrectAnswers(List<Question> questions) {
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        int totalAnswers = 0;
        for (Question question : questions) {
            boolean questionAlreadyPlayed = campaignStoreService.isQuestionAlreadyPlayed(getQuestionId(question));
            if (questionAlreadyPlayed) {
                totalAnswers++;
            }
        }
        return totalAnswers;
    }

    public static Table allQuestionsTable(List<Question> questions) {
        Table table = new Table();
        float qTableWidth = 100 / Float.valueOf(questions.size());
        double nrOfCorrectQuestions = getTotalCorrectAnswers(questions);
        for (int i = 0; i < questions.size(); i++) {
            Table qTable = new Table();
            if (i <= (nrOfCorrectQuestions - 1) && nrOfCorrectQuestions != 0) {
                qTable.setBackground(GraphicUtils.getNinePatch(FlagsSpecificResource.allq_bakcground));
            }
            table.add(qTable).width(ScreenDimensionsManager.getScreenWidthValue(qTableWidth));
        }
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return table;
    }

    public static String getQuestionId(Question question) {
        return question.getQuestionString();
    }
}
