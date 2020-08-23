package libgdx.implementations.countries;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Contrast;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;
import libgdx.utils.EnumUtils;

public class CountriesDependencyManager extends CampaignGameDependencyManager {

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        QuestionConfigFileHandler questionConfigFileHandler = new QuestionConfigFileHandler();
        StringBuilder text = new StringBuilder();
        for (QuestionCategory category : (QuestionCategory[]) EnumUtils.getValues(CountriesGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            for (QuestionDifficulty difficultyLevel : (QuestionDifficulty[]) EnumUtils.getValues(CountriesGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
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
    public Class<CountriesSpecificResource> getSpecificResourceTypeEnum() {
        return CountriesSpecificResource.class;
    }

    @Override
    public Class<CountriesCampaignLevelEnum> getCampaignLevelTypeEnum() {
        return CountriesCampaignLevelEnum.class;
    }

    @Override
    public Class<CountriesCategoryEnum> getQuestionCategoryTypeEnum() {
        return CountriesCategoryEnum.class;
    }

    @Override
    public Contrast getScreenContrast() {
        return Contrast.DARK;
    }

    @Override
    public Class<CountriesDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return CountriesDifficultyLevel.class;
    }
    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }
}
