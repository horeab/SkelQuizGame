package libgdx.xxutils.geoquiz;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.xxutils.FlutterQuestionProcessor;

public class FlutterGeoQuizQuestionProcessor {


    private static final String QUESTION_CONFIG_FILE_NAME = "GeoQuizGameQuestionConfig";
    private static final QuestionDifficulty[] DIFFS = QuizQuestionDifficultyLevel.values();
    private static final String[] CATEGS = {"cat0", "cat1", "cat2", "cat3", "cat4", "cat5"};

    public static void main(String[] args) throws IOException {

        List<Language> languages = Arrays.asList(Language.en, Language.ro);

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        //COUNTRIES - SYNONYMS - CAPITALS - COUNTRY_RANKS - COUNTRY_RANGES
        res.append(FlutterCountriesProcessor.createCountriesAndSynonyms(languages));

        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language));

            if (language == Language.en) {
                //cat0
                //cat1
                FlutterCountriesProcessor.getStatisticsQuestions(res, language);
                //cat2
                FlutterCountriesProcessor.getNeighbourCountriesQuestions(res, "cat2", language);
            }
            addQuestionCategory(GameIdEnum.countries, false,"cat5", "cat3", language,
                    new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0}, res);
            addQuestionCategory(GameIdEnum.countries, false,"cat4", "cat4", language,
                    new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0}, res);
            addQuestionCategory(GameIdEnum.quizgame, true,"cat0", "cat5", language,
                    QuizQuestionDifficultyLevel.values(), res);
            //cat6 - CAPITALS -> from allCountries ranking
            //cat7 - FLAGS -> from allCountries ranking
            addQuestionCategory(GameIdEnum.quizgame, true,"cat4", "cat8", language,
                    QuizQuestionDifficultyLevel.values(), res);
            //cat9 - MAPS -> from allCountries ranking
            res.append("  }\n\n");
        }

        System.out.println(res);
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
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + gameId + "/questions/"
                + language+ (fromTemp ? "/temp" : "") + "/diff" + diff.getIndex()  + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

}
