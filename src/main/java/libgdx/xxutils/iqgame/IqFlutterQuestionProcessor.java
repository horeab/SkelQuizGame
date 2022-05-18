package libgdx.xxutils.iqgame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import libgdx.campaign.QuestionCategory;
import libgdx.constants.Language;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

import static libgdx.xxutils.anatomy.AnatomyQuestionProcessor.categories;

public class IqFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

        StringBuilder res = new StringBuilder();

        res.append("    return result;\n" +
                "  }\n\n");

        for (int i = 0; i < 17; i++) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(Language.en));
            addQuestion(i, res);
            res.append("  }\n\n");
        }
        System.out.println(res);
    }

    private static void addQuestion(int qNr, StringBuilder res) {
        String qPath = getLibgdxQuestionPath(qNr);
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
                    .getQuestionsForCatAndDiff(AnatomyQuestionDifficultyLevel._0,
                            "cat3", questions.toString()));

            reader.close();
        } catch (IOException e) {
        }
    }

    private static String getLibgdxQuestionPath(int qNr) {
        return "/Users/macbook/IdeaProjects/SkelClassicGame/src/main/resources/tournament_resources" +
                "/implementations/iqtest/questions/numberseq/q" + qNr + "a.txt";
    }

}
