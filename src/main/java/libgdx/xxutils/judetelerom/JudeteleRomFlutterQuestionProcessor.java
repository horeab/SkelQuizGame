package libgdx.xxutils.judetelerom;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.implementations.judetelerom.JudeteleRomDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

import static libgdx.xxutils.astronomy.AstronomyQuestionProcessor.categories;

public class JudeteleRomFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

        List<Language> languages = Arrays.asList(Language.ro);

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        List<JudeteleRomDifficultyLevel> diffs = new ArrayList<>(Arrays.asList(JudeteleRomDifficultyLevel.values()));
        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language,
                    "JudeteleRomQuestionConfig"));

            for (JudeteleRomDifficultyLevel diff : diffs) {
                for (JudeteleRomCategoryEnum cat : JudeteleRomCategoryEnum.values()) {
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
                "/implementations/judetelerom/questions/temp/" + language.toString() + "/diff" + diff.getIndex() + "/"
                + "questions_diff" + diff.getIndex() + "_" + flutterCat + ".txt";
    }

}
