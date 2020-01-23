package libgdx.implementations.anatomy;

import libgdx.campaign.*;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnatomyDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public String getExtraContentProductId() {
        return "extraContentAnatomy";
    }

    @Override
    public Class<AnatomySpecificResource> getSpecificResourceTypeEnum() {
        return AnatomySpecificResource.class;
    }

    @Override
    public Class<AnatomyCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return AnatomyCampaignLevelEnum.class;
    }

    @Override
    public Class<AnatomyQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return AnatomyQuestionCategoryEnum.class;
    }

    @Override
    public Class<AnatomyQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return AnatomyQuestionDifficultyLevel.class;
    }

    @Override
    public RGBColor getScreenBackgroundColor() {
        return new RGBColor(1, 230, 242, 255);
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
