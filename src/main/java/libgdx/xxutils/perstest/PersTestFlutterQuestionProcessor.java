package libgdx.xxutils.perstest;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.constants.Language;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

public class PersTestFlutterQuestionProcessor {


    private static final String QUESTION_CONFIG_FILE_NAME = "PersTestGameQuestionConfig";
    private static final String[] CATEGS = {"cat0"};

    public static void main(String[] args) throws IOException {

//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        List<Language> languages = Arrays.asList(Language.values());

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language, FlutterQuestionProcessor.QUESTION_CONFIG_FILE_NAME));

            addQuestionCategory("cat0", "cat0", language, res);
            addQuestionCategory("cat1", "cat1", language, res);
            addQuestionCategory("cat2", "cat2", language, res);
            res.append("  }\n\n");
        }

        System.out.println(res);
    }

    private static void addQuestionCategory(String libGdxCat, String flutterCat,
                                            Language language, StringBuilder res) {
        if (StringUtils.isNotBlank(flutterCat)) {
            String qPath = getLibgdxQuestionPath(language, libGdxCat);
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
                        .getQuestionsForCatAndDiff(QuizQuestionDifficultyLevel._0, flutterCat, questions.toString()));

                reader.close();
            } catch (IOException e) {
            }
        }
    }

    private static String getLibgdxQuestionPath(Language language, String libGdxCat) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/perstest/questions/"
                + libGdxCat + "/" + language + "_questions.txt";
    }

}
