package libgdx.implementations.countries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.campaign.ImageCategIncrementRes;
import libgdx.constants.Contrast;
import libgdx.implementations.countries.hangman.CountriesQuestionPopulator;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.IncrementingRes;

public class CountriesDependencyManager extends CampaignGameDependencyManager {

    private List<String> allCountries = new ArrayList<>();
    private Map<CountriesCategoryEnum, Map<Integer, List<Integer>>> categQuestions = new LinkedHashMap<>();
    private Map<Integer, List<String>> synonyms = new LinkedHashMap<>();

    public Map<Integer, List<Integer>> getCategQuestions(CountriesCategoryEnum questionCategory) {
        return categQuestions.get(questionCategory);
    }

    public Map<Integer, List<String>> getSynonyms() {
        return synonyms;
    }

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
        synonyms = CountriesQuestionPopulator.getSynonyms();
        for (CountriesCategoryEnum categoryEnum : CountriesCategoryEnum.values()) {
            CountriesQuestionPopulator countriesQuestionPopulator = new CountriesQuestionPopulator(CountriesGame.getInstance().getSubGameDependencyManager().getAllCountries(), categoryEnum);
            categQuestions.put(categoryEnum, countriesQuestionPopulator.getQuestions());
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
