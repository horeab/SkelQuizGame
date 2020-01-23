package libgdx.implementations.flags;

import libgdx.campaign.*;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlagsDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        list.add(new ImageCategIncrementRes(0, 41, FlagsCategoryEnum.cat0, ImageCategIncrementRes.PNG));
        list.add(new ImageCategIncrementRes(0, 32, FlagsCategoryEnum.cat1, ImageCategIncrementRes.PNG));
        list.add(new ImageCategIncrementRes(0, 63, FlagsCategoryEnum.cat2, ImageCategIncrementRes.PNG));
        list.add(new ImageCategIncrementRes(0, 15, FlagsCategoryEnum.cat3, ImageCategIncrementRes.PNG));
        list.add(new ImageCategIncrementRes(0, 52, FlagsCategoryEnum.cat4, ImageCategIncrementRes.PNG));
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(FlagsGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(FlagsGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<FlagsSpecificResource> getSpecificResourceTypeEnum() {
        return FlagsSpecificResource.class;
    }

    @Override
    public Class<FlagsCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return FlagsCampaignLevelEnum.class;
    }

    @Override
    public Class<FlagsCategoryEnum> getQuestionCategoryTypeEnum() {
        return FlagsCategoryEnum.class;
    }

    @Override
    public Class<FlagsDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return FlagsDifficultyLevel.class;
    }

}
