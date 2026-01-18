package libgdx.xxutils.kidlearn;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        List<Language> languages = Arrays.asList(Language.en, Language.ro);
//        List<Language> languages = Arrays.asList(Language.en);

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");

        ///////////QUESTIONS////////////////

        List<KidLearnCateg> categs = getCategs();
        for (Language language : languages) {
            res.append(FlutterQuestionProcessor.getQuestionsHeader(language, FlutterQuestionProcessor.QUESTION_CONFIG_FILE_NAME));

            for (KidLearnCateg cat : categs) {
                addQuestionCategory(cat, language, res);
            }
            res.append("  }\n\n");
        }

        System.out.println(res);
    }

    private static void addQuestionCategory(KidLearnCateg kidLearnCateg,
                                            Language language,
                                            StringBuilder res) {
        for (QuestionDifficulty diff : kidLearnCateg.diffs) {
            String qPath = getLibgdxQuestionPath(language, true, kidLearnCateg.rootCat, kidLearnCateg.type, diff);

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

    public static List<KidLearnCateg> getCategs() {

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
                        "voc",
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
                        "voc",
                        "hangman",
                        "cat8",
                        Arrays.asList(
                                QuizQuestionDifficultyLevel._0,
                                QuizQuestionDifficultyLevel._1,
                                QuizQuestionDifficultyLevel._2
                        )
                ),
                new KidLearnCateg(
                        "voc",
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

    private static String getLibgdxQuestionPath(Language language, boolean temp, String bigCat, String type, QuestionDifficulty diff) {
        return ROOT_PATH  + (temp ? "temp/" : "") + bigCat + "/" + type + "/" + language + "/l" + diff.getIndex() + ".txt";
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

    public List<String> wordsToTranslate(String enQuestion) {
        if ("feed".equals(this.type)) {
            return Arrays.asList(enQuestion.split(":"));
        } else if ("recy".equals(this.type)) {
            return Collections.singletonList(enQuestion.split(":")[1]);
        } else if ("body".equals(this.type)) {
            return Collections.singletonList(enQuestion.split(":")[0]);
        } else if ("state".equals(this.type)) {
            return Collections.singletonList(enQuestion.split(":")[1]);
        } else if ("hangman".equals(this.type)) {
            return Collections.singletonList(enQuestion);
        } else if ("verb".equals(this.type)) {
            return Collections.singletonList(enQuestion.split(":")[1]);
        } else if ("words".equals(this.type)) {
            return Collections.singletonList(enQuestion);
        }
        throw new IllegalStateException("no wordsToTranslate config for " + this);
    }

    public String formQuestionFormat(String enQuestion, Language lang, String... translatedWords) {
        if ("feed".equals(this.type)) {
            assert (translatedWords.length == 2);
            String format = "%s:%s";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format, translatedWords, lang);
        } else if ("recy".equals(this.type)) {
            assert (translatedWords.length == 1);
            String format = "%s:%s";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format,
                    Arrays.asList(enQuestion.split(":")[0],
                            translatedWords[0]).toArray(), lang);
        } else if ("body".equals(this.type)) {
            String[] split = enQuestion.split(":");
            assert (translatedWords.length == 1);
            assert (split.length == 2);
            String format = "%s:%s:%s:";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format,
                    Arrays.asList(translatedWords[0], split[1], split[2]).toArray(), lang);
        } else if ("state".equals(this.type)) {
            assert (translatedWords.length == 1);
            String format = "%s:%s";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format,
                    Arrays.asList(enQuestion.split(":")[0],
                            translatedWords[0]).toArray(), lang);
        } else if ("hangman".equals(this.type)) {
            assert (translatedWords.length == 1);
            String format = "%s";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format,
                    Collections.singletonList(translatedWords[0]).toArray(), lang);
        } else if ("verb".equals(this.type)) {
            assert (translatedWords.length == 1);
            String format = "%s:%s";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format,
                    Arrays.asList(enQuestion.split(":")[0],
                            translatedWords[0]).toArray(), lang);
        } else if ("words".equals(this.type)) {
            assert (translatedWords.length == 1);
            String format = "%s";
            return TranslateQuestionProcessor.QuestionParser.formQuestion(format,
                    Collections.singletonList(translatedWords[0]).toArray(), lang);
        }
        throw new IllegalStateException("no format question found for " + this);
    }

    @Override
    public String toString() {
        return "KidLearnCateg{" +
                "rootCat='" + rootCat + '\'' +
                ", type='" + type + '\'' +
                ", flutterCat='" + flutterCat + '\'' +
                ", diffs=" + diffs +
                '}';
    }
}
