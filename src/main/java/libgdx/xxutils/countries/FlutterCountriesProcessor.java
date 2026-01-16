package libgdx.xxutils.countries;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.xxutils.FlutterQuestionProcessor;

public class FlutterCountriesProcessor {


    private static final GameIdEnum GAME_ID = GameIdEnum.quizgame;
    private static final String QUESTION_CONFIG_FILE_NAME = "CountriesGameQuestionConfig";
    private static final QuestionDifficulty[] DIFFS = QuizQuestionDifficultyLevel.values();
    private static final QuestionCategory[] CATEGS = CountriesCategoryEnum.values();

    public static void main(String[] args) throws IOException {

        ///
        boolean fromTemp = true;
        ///

        List<Language> languages = Arrays.asList(Language.values());
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        String countriesAndSyn = createCountriesAndCapitals(languages);
        System.out.println(countriesAndSyn);

        ///////////QUESTIONS////////////////
        StringBuilder res = new StringBuilder();
        res.append(FlutterQuestionProcessor.getQuestionsHeader(Language.en, FlutterQuestionProcessor.QUESTION_CONFIG_FILE_NAME));
        getStatisticsQuestions(res, Language.en);
        getNeighbourCountriesQuestions(res, "cat2", Language.en);
        addQuestionCategory(GameIdEnum.countries, false,"cat5", "cat3", Language.en,
                new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0}, res);
        addQuestionCategory(GameIdEnum.countries, false,"cat4", "cat4", Language.en,
                new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0}, res);
        res.append("}\n");

        for (Language language : languages) {
            if (language == Language.en) {
                continue;
            }
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language, FlutterQuestionProcessor.QUESTION_CONFIG_FILE_NAME));
            addQuestionCategory(GameIdEnum.countries, false,"cat5", "cat3", language,
                    new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0}, res);
            addQuestionCategory(GameIdEnum.countries, false,"cat4", "cat4", language,
                    new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0}, res);
            res.append("}\n");
        }
        System.out.println(res);
    }

    public static String createCountriesAndCapitals(List<Language> languages) throws IOException {
        StringBuilder countriesString = new StringBuilder();
//        List<Language> languages = Arrays.asList(Language.values());

        countriesString.append("List<CountryRanges> get allCountryRanges {\n" +
                "            _allCountryRanges ??= [\n");

        countriesString.append(getCountryRangesStartEnd().stream()
                .map(e -> e.toString()).collect(Collectors.joining(",\n")));

        countriesString.append(" ];\n" +
                "            return _allCountryRanges!;\n" +
                "        }\n");

        countriesString.append(" Map<Language, List<String>> get _getAllCountries {\n" +
                "    Map<Language, List<String>> result = HashMap<Language, List<String>>();\n");
        for (Language language : languages) {
            countriesString.append("result.putIfAbsent(Language." + language + ", () => _allCountries" + language.toString().toUpperCase() + ");\n");
        }
        countriesString.append("    return result;\n" +
                "  }\n");

        countriesString.append(" Map<Language, List<String>> get _getAllSynonyms {\n" +
                "    Map<Language, List<String>> result = HashMap<Language, List<String>>();\n");
        for (Language language : languages) {
            countriesString.append("result.putIfAbsent(Language." + language + ", () => _allSynonyms" + language.toString().toUpperCase() + ");\n");
        }
        countriesString.append("    return result;\n" +
                "  }\n");

        for (Language language : languages) {
            try {
                countriesString.append(getCountriesString(language));
            } catch (IOException e) {
            }
        }

        for (Language language : languages) {
            try {
                countriesString.append(getSynonymsString(language));
            } catch (IOException e) {
            }
        }

        countriesString.append(getCountriesRankedString(Language.en));
//        countriesString.append(getFlagsString());
//        countriesString.append(getMapsString());
//        countriesString.append(getCapitalsIndexesString());

//
//        countriesString.append(" Map<Language, List<String>> get _getAllCapitals {\n" +
//                "    Map<Language, List<String>> result = HashMap<Language, List<String>>();\n");
//        for (Language language : languages) {
//            countriesString.append("result.putIfAbsent(Language." + language + ", () => _allCapitals" + language.toString().toUpperCase() + ");\n");
//        }
//        for (Language language : languages) {
//            try {
//                countriesString.append(getCapitalsString(language));
//            } catch (IOException e) {
//            }
//        }

        return countriesString.toString();
    }

    private static List<CountryRanges> getCountryRangesStartEnd() throws IOException {
        List<String> englishCountries2 = FlutterCountriesProcessor.getFromCountriesFolder(Language.en, "countries");

        List<CountryRanges> countryRanges = new ArrayList<>();
        List<MaxCountryRanges> maxRanges = getMaxRangeFrom(englishCountries2);
        int start = 0;
        int i = 0;
        for (String c : englishCountries2) {
            if (i != 0 && c.contains("----")) {
                int finI = i;
                MaxCountryRanges newMaxStart = maxRanges.stream().filter(e -> e.maxStart < finI && e.maxEnd >= finI - 1).findFirst().get();
                countryRanges.add(new CountryRanges(start, i - 1, newMaxStart.maxStart, newMaxStart.maxEnd));
                start = i;
            }
            i++;
        }
        MaxCountryRanges latMaxRange = maxRanges.get(maxRanges.size() - 1);
        countryRanges.add(new CountryRanges(countryRanges.get(countryRanges.size() - 1).end + 1,
                englishCountries2.size() - 1, latMaxRange.maxStart, latMaxRange.maxEnd));
        return countryRanges;
    }

    private static List<MaxCountryRanges> getMaxRangeFrom(List<String> englishCountries) {
        int i = 0;
        int maxStart = 0;
        List<MaxCountryRanges> countryRanges = new ArrayList<>();
        for (String c : englishCountries) {
            if (i != 0 && c.contains("****")) {
                countryRanges.add(new MaxCountryRanges(maxStart, i - 1));
                maxStart = i;
            }
            i++;
        }
        countryRanges.add(new MaxCountryRanges(countryRanges.get(countryRanges.size() - 1).maxEnd + 1, englishCountries.size() - 1));
        return countryRanges;
    }

    private static void getRegionsForLangCountriesQuestions(StringBuilder res, Map<QuestionCategory, String> flutterCats, Language lang) {
        List<QuestionCategory> cats = Arrays.asList(CountriesCategoryEnum.cat4, CountriesCategoryEnum.cat5);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    flutterCats.get(cat), getGeneralQPath(cat, CountriesDifficultyLevel._0, lang)));
        }
    }

    static void getNeighbourCountriesQuestions(StringBuilder res, String flutterCat, Language lang) {
        List<QuestionCategory> cats = Arrays.asList(CountriesCategoryEnum.cat3);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    flutterCat, getGeneralQPath(cat, CountriesDifficultyLevel._0, lang)));
        }
    }

    public static void getStatisticsQuestions(StringBuilder res, Language lang) {
        List<QuestionCategory> cats = Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1);
        List<QuestionDifficulty> diff = Arrays.asList(CountriesDifficultyLevel._0, CountriesDifficultyLevel._1, CountriesDifficultyLevel._2, CountriesDifficultyLevel._3, CountriesDifficultyLevel._4, CountriesDifficultyLevel._5, CountriesDifficultyLevel._6);
        for (QuestionCategory cat : cats) {
            for (QuestionDifficulty difficulty : diff) {
                res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(difficulty,
                        cat.name(), getGeneralQPath(cat, difficulty, lang)));
            }
        }
    }

    private static String getGeneralQPath(QuestionCategory cat, QuestionDifficulty difficulty, Language lang) {
        String qPath = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/"
                + "aquestions/diff0/questions_diff0_cat" + cat.getIndex() + ".txt";

        if (Arrays.asList(CountriesCategoryEnum.cat4, CountriesCategoryEnum.cat5).contains(cat)) {
            qPath = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/"
                    + lang.toString() + "/questions_diff0_cat" + cat.getIndex() + ".txt";
        }
        if (Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1).contains(cat)) {
            qPath = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/aaquestions/cat"
                    + cat.getIndex() + "/questions_cat" + cat.getIndex() + "_diff" + difficulty.getIndex() + ".txt";
        }

        BufferedReader reader;
        List<String> questions = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            while (line != null) {
                questions.add(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
        }
        if (Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1).contains(cat)) {
            Map<String, String> countriesNameIndex = getCountriesNameIndex();
            questions = questions.stream()
                    .map(countryName -> {
                                if (!countriesNameIndex.containsKey(countryName)) {
                                    throw new RuntimeException("Country " + countryName + " not in countries list.");
                                }
                                return countriesNameIndex.get(countryName);
                            }
                    )
                    .collect(Collectors.toList());
        }
        return "[" + questions.stream().map(e -> "\"" + e + "\"").collect(Collectors.joining(",")) + "]";
    }

    private static String getSynonymsString(Language language) throws IOException {
        String synonyms = getSynonyms(language)
                .stream().map(e -> "\"" + (Integer.parseInt(e.split(":")[0]) - 1) + ":" + e.split(":")[1] + "\"")
                .collect(Collectors.joining(","));
        return "List<String> get _allSynonyms" + language.toString().toUpperCase() + " => \n" +
                "    [" + synonyms + "];\n" +
                "\n\n";
    }

    private static Map<String, String> getCountriesNameIndex() {
        List<String> countriesList = null;
        try {
            countriesList = getFromCountriesFolder(Language.en, "countries");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (countriesList.size() != 195) {
            throw new RuntimeException();
        }
        Map<String, String> result = new HashMap<>();
        countriesList.forEach(e -> {
            String string = e.replace("----", "")
                    .replace("****", "");
            result.put(string.split(":")[0], string.split(":")[1]);
        });
        return result;
    }


    private static String getCountriesString(Language language) throws IOException {
        List<String> countriesList = getFromCountriesFolder(language, "countries");
        if (countriesList.size() != 195) {
            throw new RuntimeException();
        }
        String countries = countriesList
                .stream().map(e -> "\"" + e.replace("----", "")
                        .replace("****", "").split(":")[0] + "\"")
                .collect(Collectors.joining(","));
        return "List<String> get _allCountries" + language.toString().toUpperCase() + " => \n" +
                "     [" + countries + "];\n" +
                "\n\n";
    }

    private static String getCountriesRankedString(Language language) throws IOException {
        MutableInt index = new MutableInt();
        index.setValue(0);
        String countries = getFromCountriesFolder(language, "countries")
                .stream().map(e -> {
                    String s = "\"" + index + ":" + e.split(":")[1] + "\"";
                    index.setValue(index.getValue() + 1);
                    return s;
                })
                .collect(Collectors.joining(","));
        return "List<String> get allCountriesRanked =>\n" +
                "    [" + countries + "];\n" +
                "  \n\n";
    }

    private static void addQuestionCategory(GameIdEnum gameIdEnum, boolean fromTemp, String libGdxCat, String flutterCat,
                                            Language language, QuestionDifficulty[] diffs, StringBuilder res) {
        if (StringUtils.isNotBlank(flutterCat)) {
            for (QuestionDifficulty diff : diffs) {
                String qPath = getLibgdxQuestionPath(language, gameIdEnum, fromTemp, libGdxCat, diff);
                BufferedReader reader;
                List<String> questions = new ArrayList<>();
                try {
                    reader = new BufferedReader(new FileReader(qPath));
                    String line = reader.readLine();
                    while (line != null) {
                        questions.add("\"" + line + "\"");
                        line = reader.readLine();
                    }

                    res.append(FlutterQuestionProcessor
                            .getQuestionsForCatAndDiff(diff, flutterCat, questions.toString()));

                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static String getLibgdxQuestionPath(Language language, GameIdEnum gameId, boolean fromTemp, String libGdxCat, QuestionDifficulty diff) {
        return FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + gameId + "/questions/"
                + language+ (fromTemp ? "/temp" : "") + "/diff" + diff.getIndex()  + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

    public static List<String> getCountries(Language translateTo) throws IOException {
        return getFromCountriesFolder(translateTo, "countries");
    }

    public static List<String> getCapitals(Language translateTo) throws IOException {
        return getFromCountriesFolder(translateTo, "capitals");
    }

    static List<String> getFromCountriesFolder(Language translateTo, String fileName) throws IOException {
        String rootPath = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/" + fileName + ".txt";
        return readFileContents(String.format(rootPath, translateTo.toString()));
    }

    private static List<String> getSynonyms(Language translateTo) throws IOException {
        String rootPath = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/synonyms.txt";
        return readFileContents(String.format(rootPath, translateTo.toString()));
    }

    public static List<String> readFileContents(String path) throws IOException {
        FileReader file = new FileReader(path);
        String line = "";
        List<String> returnValue = new ArrayList<>();
        BufferedReader reader = new BufferedReader(file);
        try {
            while ((line = reader.readLine()) != null) {
                returnValue.add(line);
            }
        } finally {
            reader.close();
        }
        return returnValue;
    }


    static class MaxCountryRanges {

        int maxStart;
        int maxEnd;

        public MaxCountryRanges(int maxStart, int maxEnd) {
            this.maxStart = maxStart;
            this.maxEnd = maxEnd;
        }

        @Override
        public String toString() {
            return "MaxCountryRanges{" +
                    "maxStart=" + maxStart +
                    ", maxEnd=" + maxEnd +
                    '}';
        }
    }

    static class CountryRanges {

        int start;
        int end;
        int maxStart;
        int maxEnd;

        public CountryRanges(int start, int end, int maxStart, int maxEnd) {
            this.start = start;
            this.end = end;
            this.maxStart = maxStart;
            this.maxEnd = maxEnd;
        }

        @Override
        public String toString() {
            return "CountryRanges("
                    + start +
                    ", " + end +
                    ", " + maxStart +
                    ", " + maxEnd +
                    ')';
        }
    }


}
