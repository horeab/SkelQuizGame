package libgdx.xxutils.anatomy;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.anatomy.AnatomyQuestionCategoryEnum;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

import static libgdx.xxutils.anatomy.AnatomyQuestionProcessor.categories;

public class AnatomyFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

//        List<Language> languages = Arrays.asList(Language.en);
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        List<Language> languages = Arrays.asList(Language.values());

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        Map<QuestionCategory, QuestionCategory> catsM = categories();
        List<AnatomyQuestionDifficultyLevel> diffs = new ArrayList<>(Arrays.asList(AnatomyQuestionDifficultyLevel.values()));
        diffs.remove(AnatomyQuestionDifficultyLevel._4);
        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language, FlutterQuestionProcessor.QUESTION_CONFIG_FILE_NAME));

            for (AnatomyQuestionDifficultyLevel diff : diffs) {
                for (AnatomyQuestionCategoryEnum cat : AnatomyQuestionCategoryEnum.values()) {
                    addQuestionCategory(cat, catsM.get(cat), diff, language, res);
                }
            }
            res.append("  }\n\n");
        }

//        System.out.println(res);

        formLatinTranslation();
    }

    private static void addQuestionCategory(QuestionCategory libGdxCat, QuestionCategory flutterCat,
                                            QuestionDifficulty diff,
                                            Language language, StringBuilder res) {
        if (StringUtils.isNotBlank(flutterCat.toString())) {
            String qPath = getLibgdxQuestionPath(language.toString(), libGdxCat.toString(), diff);
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
                        .getQuestionsForCatAndDiff(diff, flutterCat.toString(), questions.toString()));

                reader.close();
            } catch (IOException e) {
            }
        }
    }

    private static void formLatinTranslation() {
        for (QuestionDifficulty diff : Collections.singleton(AnatomyQuestionDifficultyLevel._0)) {
            for (QuestionCategory cat : AnatomyQuestionCategoryEnum.values()) {
                List<String> enQuestions = getEnglishQuestions(cat, diff);
                List<String> laQuestions = getLatinQuestions(cat);
                int i = 0;
                for (String enq : enQuestions) {
                    System.out.println("\"" + enq.split(":")[0] + "\":\"" + laQuestions.get(i) + "\",");
                    i++;
                }
            }
        }
    }

    static List<String> getEnglishQuestions(QuestionCategory cat, QuestionDifficulty diff) {

        String qPath = getLibgdxQuestionPath(Language.en.toString(), cat.name(), diff);
        BufferedReader reader;
        List<String> questions = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            while (line != null) {
                questions.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
        return questions;
    }

    static List<String> getLatinQuestions(QuestionCategory cat) {
        String qPath = getLibgdxQuestionPath("la", cat.name(), AnatomyQuestionDifficultyLevel._0);
        BufferedReader reader;
        List<String> questions = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            while (line != null) {
                questions.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
        return questions;
    }

    private static String getLibgdxQuestionPath(String language, String libGdxCat, QuestionDifficulty diff) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/anatomy/questions/temp/" + language + "/diff" + diff.getIndex() + "/"
                + "questions_diff" + diff.getIndex() + "_" + libGdxCat + ".txt";
    }

}
