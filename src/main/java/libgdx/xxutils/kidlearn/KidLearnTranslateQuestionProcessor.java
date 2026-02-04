package libgdx.xxutils.kidlearn;

import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KidLearnTranslateQuestionProcessor {

    static final List<Language> RTL_LANGS = Arrays.asList(Language.ar, Language.he);

    static final String GAME_ID = "kidlearn";
    static final String ROOT_PATH_OLD = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/to_translate/";
    static final String ROOT_PATH_NEW = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/temp/";

    static int nrOfTranslations = 0;

    public static void main(String[] args) throws IOException {

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages.remove(Language.de);
        languages.remove(Language.en);
        languages.remove(Language.ro);
//        List<Language> languages = Arrays.asList(Language.en);
//        List<Language> languages = Arrays.asList(Language.de, Language.en, Language.ro);

        List<KidLearnCateg> categs = FlutterKidLearnProcessor.getCategs();

        //TODO overwrite existing categs
//        List<KidLearnCateg> categs = Arrays.asList(new KidLearnCateg(
//                        "sci",
//                        "body",
//                        "cat5",
//                        Arrays.asList(
//                                QuizQuestionDifficultyLevel._1
//                        )
//                ),
//                new KidLearnCateg(
//                        "sci",
//                        "body",
//                        "cat6",
//                        Arrays.asList(
//                                QuizQuestionDifficultyLevel._0
//                        )
//                ));
        ////////////

        for (Language lang : languages) {
            for (KidLearnCateg kidLearnCateg : categs) {

                String newQuestionPath = getNewFilePathForLang(kidLearnCateg, lang);
                File dir = new File(newQuestionPath);
                if (dir.exists()) {
                    deleteDir(newQuestionPath);
                }

                dir.mkdirs();

                for (QuestionDifficulty diff : kidLearnCateg.diffs) {
                    List<String> enQuestions = readFileContents(getPathToQuestionToTranslate(getFileName(diff), kidLearnCateg));
                    List<String> translatedQuestions = new ArrayList<>();
                    for (String enQuestion : enQuestions) {
                        List<String> wordsToTranslate = kidLearnCateg.wordsToTranslate(enQuestion);
                        List<String> translatedWords = new ArrayList<>();

                        for (String word : wordsToTranslate) {
//                            translatedWords.add(word);
                            translatedWords.add(TranslateQuestionProcessor.translateWord(lang, word));
                        }

                        String formattedQuestion = kidLearnCateg.formQuestionFormat(enQuestion, lang, translatedWords.toArray(new String[0]));
                        translatedQuestions.add(formattedQuestion);
                    }


                    String returnValue = String.join("\n", translatedQuestions);

                    String filePath = newQuestionPath + "/" + getFileName(diff);
                    if (new File(filePath).exists()) {
                        deleteDir(filePath);
                    }

                    File myObj = new File(filePath);
                    myObj.createNewFile();
                    FileWriter myWriter = new FileWriter(myObj);
                    myWriter.write(returnValue);
                    myWriter.close();

                }
            }
        }
    }

    private static void deleteDir(String pathname) {
        File file    = new File(pathname);
        file.delete();
    }

    private static String getPathToQuestionToTranslate(String fileName, KidLearnCateg kidLearnCateg) {
        return ROOT_PATH_OLD + kidLearnCateg.rootCat + "/" + kidLearnCateg.type + "/" + fileName;
    }

    private static String getNewFilePathForLang(
            KidLearnCateg kidLearnCateg,
            Language language) {
        return ROOT_PATH_NEW + kidLearnCateg.rootCat + "/" + kidLearnCateg.type + "/" + language.toString();
    }

    private static String getFileName(QuestionDifficulty diff) {
        return "l" + diff.getIndex() + ".txt";
    }


    private static List<String> readFileContents(String path) throws IOException {
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
