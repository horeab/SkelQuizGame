package libgdx.xxutils.geoquiz;

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
import libgdx.implementations.geoquiz.QuizQuestionCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TextProcessor;

public class FlutterCountriesProcessor {


    private static final GameIdEnum GAME_ID = GameIdEnum.quizgame;
    private static final String QUESTION_CONFIG_FILE_NAME = "GeoQuizGameQuestionConfig";
    private static final QuestionDifficulty[] DIFFS = QuizQuestionDifficultyLevel.values();
    private static final QuestionCategory[] CATEGS = QuizQuestionCategoryEnum.values();

    public static void main(String[] args) {

        ///
        boolean fromTemp = true;
        ///


        StringBuilder countriesAndSyn = new StringBuilder();
        List<Language> languages = Arrays.asList(Language.en, Language.ro);
//        List<Language> languages = Arrays.asList(Language.values());


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

//        System.out.println(countriesAndSyn);


        StringBuilder res = new StringBuilder();
        res.append(FlutterQuestionProcessor.getQuestionsHeader(Language.en));
        getStatisticsQuestions(res);
        getRegionsCountriesQuestions(res);
        res.append("}\n");
        System.out.println(res);
    }

    static void getRegionsCountriesQuestions(StringBuilder res) {
        List<QuestionCategory> cats = Arrays.asList(QuizQuestionCategoryEnum.cat3);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    cat, getCategoryStats(cat)));
        }

    }

    static void getStatisticsQuestions(StringBuilder res) {
        List<QuestionCategory> cats = Arrays.asList(QuizQuestionCategoryEnum.cat0);
        for (QuestionCategory cat : cats) {
            res.append(FlutterQuestionProcessor.getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0,
                    cat, getCategoryStats(cat)));
        }
    }

    private static String getCategoryStats(QuestionCategory cat) {
        String qPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/"
                + "aquestions/diff0/questions_diff0_cat" + cat.getIndex() + ".txt";
        BufferedReader reader;
        List<String> questions = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(":");
                questions.add((Integer.parseInt(split[0]) - 1) + ":" + split[1]);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
        }
        if (Arrays.asList(QuizQuestionCategoryEnum.cat0, QuizQuestionCategoryEnum.cat1).contains(cat)) {
            questions = questions.stream()
                    .sorted(Comparator.comparing(e -> -Integer.parseInt(e.split(":")[1])))
                    .collect(Collectors.toList());
        }
        return "[" + questions.stream().map(e -> "\"" + e + "\"").collect(Collectors.joining(",")) + "]";
    }

    static String getSynonymsString(Language language) throws IOException {
        String synonyms = getSynonyms(language)
                .stream().map(e -> "\"" + (Integer.parseInt(e.split(":")[0]) - 1) + ":" + e.split(":")[1] + "\"")
                .collect(Collectors.joining(","));
        return "List<String> _allSynonyms" + language.toString().toUpperCase() + "() {\n" +
                "    return [" + synonyms + "];\n" +
                "  }\n";
    }

    static String getCountriesString(Language language) throws IOException {
        String countries = getCountries(language)
                .stream().map(e -> "\"" + e + "\"").collect(Collectors.joining(","));
        return "List<String> _allCountries" + language.toString().toUpperCase() + "() {\n" +
                "    return [" + countries + "];\n" +
                "  }\n";
    }

    public static List<String> getCountries(Language translateTo) throws IOException {
        return getFromCountriesFolder(translateTo, "countries");
    }

    public static List<String> getFromCountriesFolder(Language translateTo, String fileName) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/" + fileName + ".txt";
        return readFileContents(String.format(rootPath, translateTo.toString()));
    }

    public static List<String> getSynonyms(Language translateTo) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/synonyms.txt";
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

}
