package libgdx.implementations.periodictable;

import libgdx.campaign.*;
import libgdx.constants.Contrast;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PeriodicTableDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(PeriodicTableGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(PeriodicTableGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<PeriodicTableSpecificResource> getSpecificResourceTypeEnum() {
        return PeriodicTableSpecificResource.class;
    }

    @Override
    public Class<PeriodicTableCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return PeriodicTableCampaignLevelEnum.class;
    }

    @Override
    public Class<PeriodicTableCategoryEnum> getQuestionCategoryTypeEnum() {
        return PeriodicTableCategoryEnum.class;
    }

    @Override
    public Contrast getScreenContrast() {
        return Contrast.DARK;
    }

    @Override
    public Class<PeriodicTableDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return PeriodicTableDifficultyLevel.class;
    }
    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
