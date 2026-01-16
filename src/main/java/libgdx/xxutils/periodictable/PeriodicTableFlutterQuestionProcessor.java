package libgdx.xxutils.periodictable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.constants.Language;
import libgdx.implementations.periodictable.PeriodicTableCategoryEnum;
import libgdx.implementations.periodictable.PeriodicTableDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

public class PeriodicTableFlutterQuestionProcessor {

    public static void main(String[] args) throws IOException {

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        List<Language> languages = Arrays.asList(Language.en);

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language,
                    "PeriodicTableGameQuestionConfig"));

            addQuestionCategory(language, res);
            res.append("  }\n\n");
        }

        System.out.println(res);
    }

    private static void addQuestionCategory(
            Language language, StringBuilder res) {
        String qPath = getLibgdxQuestionPath(language);
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
                    .getQuestionsForCatAndDiff(PeriodicTableDifficultyLevel._0, PeriodicTableCategoryEnum.cat0.toString(), questions.toString()));

            reader.close();
        } catch (IOException e) {
            int i = 0;
        }
    }

    private static String getLibgdxQuestionPath(Language language) {
        return FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/periodictable/temp/" + language.toString() + "_elements.txt";
    }

}
