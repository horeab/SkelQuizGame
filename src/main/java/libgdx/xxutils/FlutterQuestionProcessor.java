package libgdx.xxutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.geoquiz.QuizQuestionCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;

public class FlutterQuestionProcessor {


    private static final GameIdEnum GAME_ID = GameIdEnum.anatomy;
    private static final String QUESTION_CONFIG_FILE_NAME = "AstronomyGameQuestionConfig";
    private static final QuestionDifficulty[] DIFFS = QuizQuestionDifficultyLevel.values();
    private static final QuestionCategory[] CATEGS = QuizQuestionCategoryEnum.values();

    public static void main(String[] args) {

        ///
        boolean fromTemp = true;
        ///


        String tempDir = "aaatemp/";
        StringBuilder res = new StringBuilder();
        List<Language> languages = Arrays.asList(Language.en, Language.ro);
//        List<Language> languages = Arrays.asList(Language.values());


        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n");
        for (Language language : languages) {

            res.append(getQuestionsHeader(language));

            for (QuestionDifficulty diff : DIFFS) {
                for (QuestionCategory category : CATEGS) {
                    String qPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/questions/"
                            + (fromTemp ? tempDir : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                            "_cat" + category.getIndex() + ".txt";

                    BufferedReader reader;
                    List<String> questions = new ArrayList<>();
                    try {
                        reader = new BufferedReader(new FileReader(qPath));
                        String line = reader.readLine();
                        while (line != null) {
                            questions.add("\"" + line + "\"");
                            line = reader.readLine();
                        }

                        res.append(getQuestionsForCatAndDiff(diff, category.name(), questions.toString()));

                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
            res.append("}");
        }

        System.out.println(res);
    }

    public static String getQuestionsHeader(Language language) {
        return "void add" + language.name().toUpperCase() + "(Map<Language, Map<CategoryDifficulty, List<Question>>> result,\n" +
                "      " + QUESTION_CONFIG_FILE_NAME + " questionConfig) {\n" +
                "    var language = Language." + language.name() + ";\n";
    }

    public static String getQuestionsForCatAndDiff(QuestionDifficulty questionDifficulty, String questionCategory, String q) {

        return "addQuestions(\n" +
                "        result, //\n" +
                "        language, //\n" +
                "        questionConfig." + questionCategory + ", //\n" +
                "        questionConfig." + questionDifficulty.name().replace("_", "diff") + ", //\n" +
                "        " + q + ");\n";
    }
}
