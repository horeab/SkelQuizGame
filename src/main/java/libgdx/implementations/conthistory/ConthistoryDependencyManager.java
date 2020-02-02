package libgdx.implementations.conthistory;

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

public class ConthistoryDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(ConthistoryGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(ConthistoryGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<ConthistorySpecificResource> getSpecificResourceTypeEnum() {
        return ConthistorySpecificResource.class;
    }

    @Override
    public Class<ConthistoryCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return ConthistoryCampaignLevelEnum.class;
    }

    @Override
    public Class<ConthistoryCategoryEnum> getQuestionCategoryTypeEnum() {
        return ConthistoryCategoryEnum.class;
    }

    @Override
    public Class<ConthistoryDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return ConthistoryDifficultyLevel.class;
    }
    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
