package libgdx.implementations.countries.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.countries.CountriesQuestionCreator;
import libgdx.implementations.screens.implementations.countries.CountriesGameScreen;

public class CountriesQuestionPopulator {

    private CountriesQuestionCreator questionCreator = new CountriesQuestionCreator();
    private List<String> allCountries;
    private CountriesCategoryEnum categoryEnum;

    private static final int CATEG_3_QUESTIONS_NR = 5;
    private static final int CATEG_4_5_QUESTIONS_NR = 5;

    public CountriesQuestionPopulator(List<String> allCountries, CountriesCategoryEnum categoryEnum) {
        this.allCountries = allCountries;
        this.categoryEnum = categoryEnum;
    }

    public CountriesCategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public static Map<Integer, List<String>> getSynonyms() {
        Scanner scanner = new Scanner(new CountriesQuestionCreator().getSynonymsFileText());
        Map<Integer, List<String>> syn = new LinkedHashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(":");
            syn.put(Integer.parseInt(split[0]), Arrays.asList(split[1].split(",")));
        }
        return syn;
    }

    public Map<Integer, List<Integer>> getQuestions() {
        Map<Integer, List<Integer>> q = new LinkedHashMap<>();
        if (categoryEnum == CountriesCategoryEnum.cat0 || categoryEnum == CountriesCategoryEnum.cat1) {
            getCat0_1(q);
        } else if (categoryEnum == CountriesCategoryEnum.cat2) {
            getCat2(q);
        } else if (categoryEnum == CountriesCategoryEnum.cat3) {
            getCat3(q);
        } else if (categoryEnum == CountriesCategoryEnum.cat4 || categoryEnum == CountriesCategoryEnum.cat5) {
            getCat4_5(q);
        }
        return q;
    }

    private Map<Integer, List<Integer>> getCat4_5(Map<Integer, List<Integer>> q) {
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        Map<Integer, List<Integer>> c = new TreeMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            c.put(Integer.parseInt(line.split(":")[0]), toInt(line.split(":")[1].split(",")));
        }
        int qAdded = 0;
        for (Map.Entry<Integer, List<Integer>> e : c.entrySet()) {
            if (qAdded < CATEG_4_5_QUESTIONS_NR) {
                q.put(e.getKey(), e.getValue());
                qAdded++;
            } else if (qAdded == CATEG_4_5_QUESTIONS_NR) {
                break;
            }
        }
        return q;
    }

    private List<Integer> toInt(String... string) {
        List<Integer> res = new ArrayList<>();
        for (String s : string) {
            res.add(Integer.parseInt(s));
        }
        return res;
    }

    private Map<Integer, List<Integer>> getCat2(Map<Integer, List<Integer>> q) {
        List<Integer> azCountries = new ArrayList<>();
        List<Integer> excludeIndexes = Arrays.asList(3, 12, 16, 17);
        q.put(0, azCountries);
        int i = 1;
        for (String country : allCountries) {
            boolean isValidC = true;
            for (Integer c : azCountries) {
                String addedC = allCountries.get(c - 1);
                if (addedC.substring(0, 1).equals(country.substring(0, 1))) {
                    if (addedC.substring(addedC.length() - 1).equals(country.substring(country.length() - 1))) {
                        break;
                    }
                    isValidC = false;
                    break;
                }
            }
            if (isValidC && !excludeIndexes.contains(i)) {
                azCountries.add(i);
            }
            i++;
        }
        return q;
    }

    private Map<Integer, List<Integer>> getCat0_1(Map<Integer, List<Integer>> q) {
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        int i = 0;
        List<Integer> topCountries = new ArrayList<>();
        q.put(0, topCountries);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (i < CountriesGameScreen.TOP_COUNTRIES_TO_BE_FOUND) {
                topCountries.add(Integer.parseInt(line.split(":")[0]));
            }
            i++;
        }
        return q;
    }

    private Map<Integer, List<Integer>> getCat3(Map<Integer, List<Integer>> q) {
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        Map<Integer, List<Integer>> c = new LinkedHashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            c.put(Integer.parseInt(line.split(":")[0]), toInt(line.split(":")[1].split(",")));
        }
        int qAdded = 0;
        for (Map.Entry<Integer, List<Integer>> e : c.entrySet()) {
            if (qAdded < CATEG_3_QUESTIONS_NR) {
                q.put(e.getKey(), e.getValue());
                qAdded++;
            } else if (qAdded == CATEG_3_QUESTIONS_NR) {
                break;
            }
        }
        return q;
    }

}
