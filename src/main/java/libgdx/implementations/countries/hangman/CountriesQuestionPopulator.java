package libgdx.implementations.countries.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.countries.CountriesQuestionCreator;

public class CountriesQuestionPopulator {

    private CountriesQuestionCreator questionCreator = new CountriesQuestionCreator();
    private List<String> allCountries;
    private CountriesCategoryEnum categoryEnum;

    private static final int CATEG_0_1_QUESTIONS_NR = 20;
    private static final int CATEG_3_QUESTIONS_NR = 5;
    private static final int CATEG_4_5_MIN_QUESTIONS_NR = 20;
    private static final int CATEG_4_5_QUESTIONS_NR = 5;

    public CountriesQuestionPopulator(List<String> allCountries, CountriesCategoryEnum categoryEnum) {
        this.allCountries = allCountries;
        this.categoryEnum = categoryEnum;
    }

    public CountriesCategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public HashMap<Integer, List<String>> getQuestions() {
        HashMap<Integer, List<String>> q = new HashMap<>();
        if (categoryEnum == CountriesCategoryEnum.cat0 || categoryEnum == CountriesCategoryEnum.cat1) {
            getCat0_1(q);
        } else if (categoryEnum == CountriesCategoryEnum.cat2) {
            getCat2(q);
        } else if (categoryEnum == CountriesCategoryEnum.cat3) {
            sortCat3Q(getCat3(q));
        } else if (categoryEnum == CountriesCategoryEnum.cat4 || categoryEnum == CountriesCategoryEnum.cat5) {
            getCat4_5(q);
        }
        return q;
    }

    private HashMap<Integer, List<String>> getCat4_5(HashMap<Integer, List<String>> q) {
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        Map<Integer, List<String>> c = new TreeMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            c.put(Integer.parseInt(line.split(":")[0]), Arrays.asList(line.split(":")[1].split(",")));
        }
        int qAdded = 0;
        for (Map.Entry<Integer, List<String>> e : c.entrySet()) {
            if (qAdded < CATEG_4_5_QUESTIONS_NR) {
                q.put(e.getKey(), e.getValue());
                qAdded++;
            } else if (qAdded == CATEG_4_5_QUESTIONS_NR) {
                break;
            }
        }
        return q;
    }

    private HashMap<Integer, List<String>> getCat2(HashMap<Integer, List<String>> q) {
        List<String> azCountries = new ArrayList<>();
        List<Integer> excludeIndexes = Arrays.asList(3, 12, 16, 17);
        q.put(0, azCountries);
        int i = 1;
        for (String country : allCountries) {
            boolean isValidC = true;
            for (String c : azCountries) {
                String addedC = allCountries.get(Integer.parseInt(c) - 1);
                if (addedC.substring(0, 1).equals(country.substring(0, 1))) {
                    if (addedC.substring(addedC.length() - 1).equals(country.substring(country.length() - 1))) {
                        break;
                    }
                    isValidC = false;
                    break;
                }
            }
            if (isValidC && !excludeIndexes.contains(i)) {
                azCountries.add(String.valueOf(i));
            }
            i++;
        }
        return q;
    }

    private HashMap<Integer, List<String>> getCat0_1(HashMap<Integer, List<String>> q) {
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        int i = 0;
        List<String> topCountries = new ArrayList<>();
        q.put(0, topCountries);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (i < CATEG_0_1_QUESTIONS_NR) {
                topCountries.add(line.split(":")[0]);
            }
            i++;
        }
        return q;
    }

    private HashMap<Integer, List<String>> getCat3(HashMap<Integer, List<String>> q) {
        List<Integer> excludeCountries = Arrays.asList(6, 8, 15, 16);
        CountriesDifficultyLevel difficultyLevelToCreate = CountriesDifficultyLevel._0;
        Scanner scanner = new Scanner(questionCreator.getFileText(difficultyLevelToCreate, categoryEnum));
        Map<Integer, List<String>> c = new TreeMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            c.put(Integer.parseInt(line.split(":")[0]), Arrays.asList(line.split(":")[1].split(",")));
        }
        int qAdded = 0;
        for (Map.Entry<Integer, List<String>> e : c.entrySet()) {
            if (qAdded < CATEG_3_QUESTIONS_NR && e.getValue().size() >= 2 && !excludeCountries.contains(e.getKey())) {
                q.put(e.getKey(), e.getValue());
                qAdded++;
            } else if (qAdded == CATEG_3_QUESTIONS_NR) {
                break;
            }
        }
        return q;
    }

    private HashMap<Integer, List<String>> sortCat3Q(HashMap<Integer, List<String>> q) {
        Map<Integer, Integer> c = new TreeMap<>();
        for (Map.Entry<Integer, List<String>> e : q.entrySet()) {
            c.put(e.getValue().size(), e.getKey());
        }
        HashMap<Integer, List<String>> res = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : c.entrySet()) {
            res.put(e.getKey(), q.get(e.getKey()));
        }
        return res;
    }
}
