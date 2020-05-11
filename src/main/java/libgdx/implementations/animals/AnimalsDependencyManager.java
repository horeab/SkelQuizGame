package libgdx.implementations.animals;

import libgdx.campaign.*;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnimalsDependencyManager extends CampaignGameDependencyManager {

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
    public Class<AnimalsSpecificResource> getSpecificResourceTypeEnum() {
        return AnimalsSpecificResource.class;
    }

    @Override
    public Class<AnimalsCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return AnimalsCampaignLevelEnum.class;
    }

    @Override
    public Class<AnimalsQuestionCategoryEnum> getQuestionCategoryTypeEnum() {
        return AnimalsQuestionCategoryEnum.class;
    }

    @Override
    public Class<AnimalsQuestionDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return AnimalsQuestionDifficultyLevel.class;
    }

}
