package libgdx.xxutils.history;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;
import libgdx.implementations.paintings.PaintingsQuestionCategoryEnum;
import libgdx.implementations.paintings.PaintingsQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;

public class HistoryFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

        ///
        boolean fromTemp = true;
        ///

        String tempDir = "aaatemp/";
        StringBuilder res = new StringBuilder();
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        List<Language> languages = Arrays.asList(Language.values());


        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n");
        for (Language language : languages) {

            res.append(getQuestionsHeader(language));

            for (QuestionDifficulty diff : HistoryDifficultyLevel.values()) {
                for (QuestionCategory category : HistoryCategoryEnum.values()) {
                    String qPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/history/questions/"
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

                        if (!questions.isEmpty()) {
                            res.append(getQuestionsForCatAndDiff(diff, category, questions.toString()));
                        }

                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
            res.append("}");
        }

        File myObj = new File("/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/flutter_q.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(res.toString());
        myWriter.close();
    }

    static String getQuestionsHeader(Language language) {
        return "void add" + language.name().toUpperCase() + "(Map<Language, Map<CategoryDifficulty, List<Question>>> result,\n" +
                "      HistoryGameQuestionConfig questionConfig) {\n" +
                "    var language = Language." + language.name() + ";\n";
    }

    static String getQuestionsForCatAndDiff(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory, String q) {

        return "addQuestions(\n" +
                "        result, //\n" +
                "        language, //\n" +
                "        questionConfig." + questionCategory.name() + ", //\n" +
                "        questionConfig." + questionDifficulty.name().replace("_", "diff") + ", //\n" +
                "        " + q + ");\n";
    }

}
