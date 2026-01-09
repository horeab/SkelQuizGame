package libgdx.xxutils.kidlearn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

public class FlutterKidLearnProcessor {

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelClassicGame/src/main/resources/tournament_resources/implementations/kidlearn/";

    private static final String QUESTION_CONFIG_FILE_NAME = "KidLearnGameQuestionConfig";
    private static final QuestionDifficulty[] DIFFS = QuizQuestionDifficultyLevel.values();
    private static final QuestionCategory[] CATEGS = CountriesCategoryEnum.values();

    public static void main(String[] args) throws IOException {

        ///
        boolean fromTemp = true;
        ///

//        List<Language> languages = Arrays.asList(Language.values());
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        List<Language> languages = Arrays.asList(Language.en);

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        ///////////QUESTIONS////////////////
        res.append(FlutterQuestionProcessor.getQuestionsHeader(Language.en, FlutterQuestionProcessor.QUESTION_CONFIG_FILE_NAME));

        List<String> catsAll = Arrays.asList("eng");
        Map<String, String> catFlutterCat = new HashMap<>();
        catFlutterCat.put("hangman", "cat8");
        catFlutterCat.put("verb", "cat9");

        for (Language lang : languages) {
            for (String bigCat : catsAll) {
                addQuestionCategory(bigCat, "hangman", catFlutterCat, lang,
                        new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0,
                                QuizQuestionDifficultyLevel._1,
                                QuizQuestionDifficultyLevel._2,}, res);
                addQuestionCategory(bigCat, "verb", catFlutterCat, lang,
                        new QuizQuestionDifficultyLevel[]{QuizQuestionDifficultyLevel._0,
                                QuizQuestionDifficultyLevel._1,}, res);
            }
            res.append("}\n");
        }
        System.out.println(res);
    }

    private static void addQuestionCategory(String bigCat,
                                            String cat,
                                            Map<String, String> catFlutterCat,
                                            Language language,
                                            QuestionDifficulty[] diffs,
                                            StringBuilder res) {
        for (QuestionDifficulty diff : diffs) {
            String qPath = getLibgdxQuestionPath(language, false, bigCat, cat, diff);

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
                        .getQuestionsForCatAndDiff(diff, catFlutterCat.get(cat), questions.toString()));

                reader.close();
            } catch (IOException e) {
            }
        }
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String bigCat, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + "questions/" + (temp ? "temp/" : "") + bigCat + (bigCat.equals("eng") ? "" : "/" + language) + "/" + libGdxCat + "/l" + diff.getIndex() + ".txt";
    }

    public static List<String> readFileContents(String path) throws IOException {
        FileReader file = new FileReader(path);
        String line = "";
        List<String> returnValue = new ArrayList<>();
        BufferedReader reader = new BufferedReader(file);
        try {
            while ((line = reader.readLine()) != null) {
                returnValue.add(line);
            }
        } finally {
            reader.close();
        }
        return returnValue;
    }

}
