package libgdx.xxutils.paintings;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.paintings.PaintingsQuestionCategoryEnum;
import libgdx.implementations.paintings.PaintingsQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

public class PaintingsFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

        List<Language> languages = Arrays.asList(Language.en);

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        List<PaintingsQuestionDifficultyLevel> diffs = new ArrayList<>(Arrays.asList(PaintingsQuestionDifficultyLevel.values()));
        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language,
                    "FamPaintGameQuestionConfig"));

            for (PaintingsQuestionDifficultyLevel diff : diffs) {
                for (PaintingsQuestionCategoryEnum cat : PaintingsQuestionCategoryEnum.values()) {
                    addQuestionCategory(cat, diff, language, res);
                }
            }
            res.append("  }\n\n");
        }

        System.out.println(res);
    }

    private static void addQuestionCategory(QuestionCategory flutterCat,
                                            QuestionDifficulty diff,
                                            Language language, StringBuilder res) {
        if (StringUtils.isNotBlank(flutterCat.toString())) {
            String qPath = getLibgdxQuestionPath(language, flutterCat.toString(), diff);
            BufferedReader reader;
            List<String> questions = new ArrayList<>();
            try {
                reader = new BufferedReader(new FileReader(qPath));
                String line = reader.readLine();
                while (line != null) {
                    for (int i = 0; i < 100; i++) {
                        line = line.replace("[" + i + "]", "");
                        line = line.replace("[N " + i + "]", "");
                    }
                    line = line.replace("(d)", "");
                    questions.add("\"" + line + "\"");
                    line = reader.readLine();
                }

                res.append(FlutterQuestionProcessor
                        .getQuestionsForCatAndDiff(diff, flutterCat.toString(), questions.toString()));

                reader.close();
            } catch (IOException e) {
                int i = 0;
            }
        }
    }

    private static String getLibgdxQuestionPath(Language language, String flutterCat, QuestionDifficulty diff) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/paintings/questions/temp/" + language.toString() + "/diff" + diff.getIndex() + "/"
                + "questions_diff" + diff.getIndex() + "_" + flutterCat + ".txt";
    }

}
