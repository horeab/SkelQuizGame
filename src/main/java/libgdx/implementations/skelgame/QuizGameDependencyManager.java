package libgdx.implementations.skelgame;

import libgdx.campaign.*;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizGameDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<IncrementingRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(QuizGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(QuizGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<QuizGameSpecificResource> getSpecificResourceTypeEnum() {
        return QuizGameSpecificResource.class;
    }

    @Override
    public Class<QuizCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return QuizCampaignLevelEnum.class;
    }

    @Override
    public Class<QuizQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return QuizQuestionCategoryEnum.class;
    }

    @Override
    public Class<QuizQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return QuizQuestionDifficultyLevel.class;
    }

    public StarsService getStarsService() {
        return new StarsService();
    }
}
