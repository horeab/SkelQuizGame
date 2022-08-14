package libgdx.xxutils.iqgame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.constants.Language;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;

import static libgdx.xxutils.anatomy.AnatomyQuestionProcessor.categories;

public class IqFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

        StringBuilder res = new StringBuilder();

        List<Language> languages = Arrays.asList(Language.values());

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");


        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language));
            List<String> questions = getQuestions(language);
            TranslateQuestionProcessor.UniqueQuestionParser parser = new TranslateQuestionProcessor.UniqueQuestionParser();

            List<String> resQ = new ArrayList<>();
            for (String q : questions) {
                resQ.add(parser.formQuestion(language, parser.getQuestion(q), parser.getAnswer(q),
                        parser.getOptions(q), "", ""));
            }
            res.append(FlutterQuestionProcessor
                    .getQuestionsForCatAndDiff(AnatomyQuestionDifficultyLevel._0,
                            "cat5", resQ.stream().map(e -> "\"" + e + "\"").collect(Collectors.toList()).toString()));

            res.append("  }\n\n");
        }
        System.out.println(res);
    }

    private static List<String> getQuestions(Language lang) {
        String qPath = getLibgdxQuestionPath(lang.toString());
        BufferedReader reader;
        try {
            List<String> questionInfo = new ArrayList<>();
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            questionInfo.add(line);
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    questionInfo.add(line);
                }
            }

            reader.close();
            return questionInfo;

        } catch (IOException e) {
            return null;
        }
    }

    private static String getLibgdxQuestionPath(String lang) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/iqtest/cat5/q_" + lang + ".txt";
    }

}
