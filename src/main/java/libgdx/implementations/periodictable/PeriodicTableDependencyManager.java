package libgdx.implementations.periodictable;

import libgdx.campaign.*;
import libgdx.constants.Contrast;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;

import java.util.ArrayList;
import java.util.Arrays;
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
        StringBuilder text = new StringBuilder();
        Scanner scanner = new Scanner(ChemicalElementsUtil.getAllFileText());
        while (scanner.hasNextLine()) {
            text.append(scanner.nextLine());
        }
        scanner.close();
        return text.toString();
    }

    @Override
    public QuestionConfigFileHandler getQuestionConfigFileHandler() {
        return new QuestionConfigFileHandler() {
            @Override
            public List<QuestionCategory> getQuestionCategoriesForDifficulty(QuestionDifficulty questionDifficulty) {
                return new ArrayList<>(Arrays.asList(PeriodicTableCategoryEnum.values()));
            }
        };
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
