package libgdx.implementations.countries;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.constants.Contrast;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;

public class CountriesDependencyManager extends CampaignGameDependencyManager {

    private List<String> allCountries = new ArrayList<>();

    @Override
    public List<? extends IncrementingRes> getIncrementResList() {
        List<ImageCategIncrementRes> list = new ArrayList<>();
        return list;
    }

    @Override
    protected String allQuestionText() {
        Scanner scanner = new Scanner(new CountriesQuestionCreator().getCountriesFileText());
        while (scanner.hasNextLine()) {
            allCountries.add(scanner.nextLine());
        }
        StringBuilder text = new StringBuilder();
        for (String c : allCountries) {
            text.append(c);
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
        return Contrast.LIGHT;
    }

    @Override
    public Class<CountriesDifficultyLevel> getQuestionDifficultyTypeEnum() {
        return CountriesDifficultyLevel.class;
    }

    public QuizStarsService getStarsService() {
        return new QuizStarsService();
    }

    public List<String> getAllCountries() {
        return allCountries;
    }
}
