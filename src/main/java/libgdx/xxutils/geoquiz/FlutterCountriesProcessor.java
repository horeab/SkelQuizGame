package libgdx.xxutils.geoquiz;

import org.apache.commons.lang3.mutable.MutableInt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.xxutils.FlutterQuestionProcessor;

public class FlutterCountriesProcessor {


    private static final GameIdEnum GAME_ID = GameIdEnum.quizgame;
    private static final String QUESTION_CONFIG_FILE_NAME = "GeoQuizGameQuestionConfig";
    private static final QuestionDifficulty[] DIFFS = QuizQuestionDifficultyLevel.values();
    private static final QuestionCategory[] CATEGS = CountriesCategoryEnum.values();

    public static void main(String[] args) throws IOException {

        ///
        boolean fromTemp = true;
        ///

        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        createCountriesAndSynonyms(languages);
//        System.out.println(countriesAndSyn);

        ///////////QUESTIONS////////////////
        StringBuilder res = new StringBuilder();
        res.append(FlutterQuestionProcessor.getQuestionsHeader(Language.en));
        getStatisticsQuestions(res, Language.en);
        getNeighbourCountriesQuestions(res, "cat2", Language.en);
        getRegionsForLangCountriesQuestions(res, Language.en);
        res.append("}\n");
        for (Language language : languages) {
            if (language == Language.en) {
                continue;
            }
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language));
            getRegionsForLangCountriesQuestions(res, language);
        }
        res.append("}\n");
        System.out.println(res);
    }

    public static String createCountriesAndSynonyms(List<Language> languages) throws IOException {
        StringBuilder countriesAndSyn = new StringBuilder();
//        List<Language> languages = Arrays.asList(Language.values());

        countriesAndSyn.append("List<CountryRanges> get allCountryRanges {\n" +
                "            _allCountryRanges ??= [\n");

        countriesAndSyn.append(getCountryRangesStartEnd().stream()
                .map(e -> e.toString()).collect(Collectors.joining(",\n")));

        countriesAndSyn.append(" ];\n" +
                "            return _allCountryRanges!;\n" +
                "        }\n");

        countriesAndSyn.append(" Map<Language, List<String>> _getAllCountries() {\n" +
                "    Map<Language, List<String>> result = HashMap<Language, List<String>>();\n");
        for (Language language : languages) {
            countriesAndSyn.append("result.putIfAbsent(Language." + language + ", () => _allCountries" + language.toString().toUpperCase() + "());\n");
        }
        countriesAndSyn.append("    return result;\n" +
                "  }\n");
        for (Language language : languages) {
            try {
                countriesAndSyn.append(getCountriesString(language));
            } catch (IOException e) {
            }
        }

        countriesAndSyn.append(getCountriesRankedString(Language.en));
        countriesAndSyn.append(getFlagsString());
        countriesAndSyn.append(getMapsString());


        countriesAndSyn.append(" Map<Language, List<String>> _getAllCapitals() {\n" +
                "    Map<Language, List<String>> result = HashMap<Language, List<String>>();\n");
        for (Language language : languages) {
            countriesAndSyn.append("result.putIfAbsent(Language." + language + ", () => _allCapitals" + language.toString().toUpperCase() + "());\n");
        }
        countriesAndSyn.append("    return result;\n" +
                "  }\n");
        for (Language language : languages) {
            try {
                countriesAndSyn.append(getCapitalsString(language));
            } catch (IOException e) {
            }
        }

        countriesAndSyn.append(" Map<Language, List<String>> _getAllSynonyms() {\n" +
                "    Map<Language, List<String>> result = HashMap<Language, List<String>>();\n");
        for (Language language : languages) {
            countriesAndSyn.append("result.putIfAbsent(Language." + language + ", () => _allSynonyms" + language.toString().toUpperCase() + "());\n");
        }
        countriesAndSyn.append("    return result;\n" +
                "  }\n");
        for (Language language : languages) {
            try {
                countriesAndSyn.append(getSynonymsString(language));
            } catch (IOException e) {
            }
        }
        return countriesAndSyn.toString();
    }

    private static List<CountryRanges> getCountryRangesStartEnd() throws IOException {
        List<String> englishCountries2 = FlutterCountriesProcessor.getFromCountriesFolder(Language.en, "countries_2");

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

    private static void getRegionsForLangCountriesQuestions(StringBuilder res, Language lang) {
        List<QuestionCategory> cats = Arrays.asList(CountriesCategoryEnum.cat4, CountriesCategoryEnum.cat5);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    cat.name(), getGeneralQPath(cat, lang)));
        }
    }

    static void getNeighbourCountriesQuestions(StringBuilder res, String flutterCat, Language lang) {
        List<QuestionCategory> cats = Arrays.asList(CountriesCategoryEnum.cat3);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    flutterCat, getGeneralQPath(cat, lang)));
        }
    }

    public static void getStatisticsQuestions(StringBuilder res, Language lang) {
        List<QuestionCategory> cats = Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    cat.name(), getGeneralQPath(cat, lang)));
        }
    }

    private static String getGeneralQPath(QuestionCategory cat, Language lang) {
        String qPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/"
                + "aquestions/diff0/temp/questions_diff0_cat" + cat.getIndex() + ".txt";

        if (Arrays.asList(CountriesCategoryEnum.cat4, CountriesCategoryEnum.cat5).contains(cat)) {
            qPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/"
                    + lang.toString() + "/questions_diff0_cat" + cat.getIndex() + ".txt";
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
            questions = questions.stream()
                    .sorted(Comparator.comparing(e -> cat == CountriesCategoryEnum.cat0
                            ? -Integer.parseInt(e.split(":")[1])
                            : -Double.parseDouble(e.split(":")[1])))
                    .collect(Collectors.toList());
        }
        return "[" + questions.stream().map(e -> "\"" + e + "\"").collect(Collectors.joining(",")) + "]";
    }

    private static String getSynonymsString(Language language) throws IOException {
        String synonyms = getSynonyms(language)
                .stream().map(e -> "\"" + (Integer.parseInt(e.split(":")[0]) - 1) + ":" + e.split(":")[1] + "\"")
                .collect(Collectors.joining(","));
        return "List<String> _allSynonyms" + language.toString().toUpperCase() + "() {\n" +
                "    return [" + synonyms + "];\n" +
                "  }\n\n";
    }

    private static String getCapitalsString(Language language) throws IOException {
        String countries = getCapitals(language)
                .stream().map(e -> "\"" + e + "\"")
                .collect(Collectors.joining(","));
        return "List<String> _allCapitals" + language.toString().toUpperCase() + "() {\n" +
                "    return [" + countries + "];\n" +
                "  }\n";
    }

    private static String getFlagsString() throws IOException {
        String flags = getFromCountriesFolder(Language.en, "flags_list")
                .stream().map(e -> "\"" + e + "\"")
                .collect(Collectors.joining(","));
        return "List<String> _allFlags =>\n" +
                "    [" + flags + "];\n" +
                "  }\n\n";
    }
    private static String getMapsString() throws IOException {
        String maps = getFromCountriesFolder(Language.en, "maps_list")
                .stream().map(e -> "\"" + e + "\"")
                .collect(Collectors.joining(","));
        return "List<String> _allMaps =>\n" +
                "    [" + maps + "];\n" +
                "  }\n\n";
    }

    private static String getCountriesString(Language language) throws IOException {
        String countries = getFromCountriesFolder(language, "countries_2")
                .stream().map(e -> "\"" + e.replace("----", "")
                        .replace("****", "").split(":")[0] + "\"")
                .collect(Collectors.joining(","));
        return "List<String> _allCountries" + language.toString().toUpperCase() + "() {\n" +
                "    return [" + countries + "];\n" +
                "  }\n\n";
    }

    private static String getCountriesRankedString(Language language) throws IOException {
        MutableInt index = new MutableInt();
        index.setValue(0);
        String countries = getFromCountriesFolder(language, "countries_2")
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


    public static List<String> getCountries(Language translateTo) throws IOException {
        return getFromCountriesFolder(translateTo, "countries");
    }

    public static List<String> getCapitals(Language translateTo) throws IOException {
        return getFromCountriesFolder(translateTo, "capitals");
    }

    static List<String> getFromCountriesFolder(Language translateTo, String fileName) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/" + fileName + ".txt";
        return readFileContents(String.format(rootPath, translateTo.toString()));
    }

    private static List<String> getSynonyms(Language translateTo) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/synonyms.txt";
        return readFileContents(String.format(rootPath, translateTo.toString()));
    }

    static List<String> readFileContents(String path) throws IOException {
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
