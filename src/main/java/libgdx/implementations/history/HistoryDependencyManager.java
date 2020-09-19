package libgdx.implementations.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

public class HistoryDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(HistoryGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(HistoryGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
                Scanner scanner = new Scanner(questionConfigFileHandler.getFileText(difficultyLevel, category));
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine());
                }
                scanner.close();
            }
        }
        return text.toString();
    }

    @Override
    public Class<HistorySpecificResource> getSpecificResourceTypeEnum() {
        return HistorySpecificResource.class;
    }

    @Override
    public Class<HistoryCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return HistoryCampaignLevelEnum.class;
    }

    @Override
    public Class<HistoryCategoryEnum> getQuestionCategoryTypeEnum() {
        return HistoryCategoryEnum.class;
    }

    @Override
    public Class<HistoryDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return HistoryDifficultyLevel.class;
    }
    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
