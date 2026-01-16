package libgdx.xxutils.kidlearn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

public class FlutterKidLearnProcessor {

    public static final String ROOT_PATH = "/Users/horea.bucerzan/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/kidlearn/";

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


        List<KidLearnCateg> categs = getCategs();
        for (Language lang : languages) {
            for (KidLearnCateg cat : categs) {
                addQuestionCategory(cat, lang, res);
            }
            res.append("}\n");
        }
        System.out.println(res);
    }

    private static void addQuestionCategory(KidLearnCateg kidLearnCateg,
                                            Language language,
                                            StringBuilder res) {
        for (QuestionDifficulty diff : kidLearnCateg.diffs) {
            String qPath = getLibgdxQuestionPath(language, false, kidLearnCateg.rootCat, kidLearnCateg.type, diff);

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
                        .getQuestionsForCatAndDiff(diff, kidLearnCateg.flutterCat, questions.toString()));

                reader.close();
            } catch (IOException e) {
            }
        }
    }

    private static List<KidLearnCateg> getCategs() {

        List<KidLearnCateg> categs = Arrays.asList(

                //Math
                //cat0
                //cat1
                new KidLearnCateg(
                        "sci",
                        "recy",
                        "cat2",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._1
                        )
                ),
                new KidLearnCateg(
                        "sci",
                        "feed",
                        "cat3",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._0
                        )
                ),
                new KidLearnCateg(
                        "eng",
                        "words",
                        "cat4",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._0,
                                QuizQuestionDifficultyLevel._1,
                                QuizQuestionDifficultyLevel._2
                        )
                ),
                new KidLearnCateg(
                        "sci",
                        "body",
                        "cat5",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._1
                        )
                ),
                new KidLearnCateg(
                        "sci",
                        "body",
                        "cat6",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._0
                        )
                ),
                new KidLearnCateg(
                        "sci",
                        "body",
                        "cat7",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._2
                        )
                ),
                new KidLearnCateg(
                        "eng",
                        "hangman",
                        "cat8",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._0,
                                QuizQuestionDifficultyLevel._1,
                                QuizQuestionDifficultyLevel._2
                        )
                ),
                new KidLearnCateg(
                        "eng",
                        "verb",
                        "cat9",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._0,
                                QuizQuestionDifficultyLevel._1
                        )
                ),
                new KidLearnCateg(
                        "sci",
                        "state",
                        "cat10",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._2
                        )
                )
        );
        return categs;
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

class KidLearnCateg {
    public String rootCat;
    public String type;
    public String flutterCat;
    public List<QuestionDifficulty> diffs;

    public KidLearnCateg(String rootCat, String type, String flutterCat, List<QuestionDifficulty> diffs) {
        this.rootCat = rootCat;
        this.type = type;
        this.flutterCat = flutterCat;
        this.diffs = diffs;
    }
}
