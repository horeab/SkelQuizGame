package libgdx.implementations.astronomy;

import libgdx.campaign.*;
import libgdx.constants.Contrast;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AstronomyDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(AstronomyGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(AstronomyGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<AstronomySpecificResource> getSpecificResourceTypeEnum() {
        return AstronomySpecificResource.class;
    }

    @Override
    public Class<AstronomyCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return AstronomyCampaignLevelEnum.class;
    }

    @Override
    public Class<AstronomyCategoryEnum> getQuestionCategoryTypeEnum() {
        return AstronomyCategoryEnum.class;
    }

    @Override
    public Contrast getScreenContrast() {
        return Contrast.DARK;
    }

    @Override
    public Class<AstronomyDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return AstronomyDifficultyLevel.class;
    }
    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
