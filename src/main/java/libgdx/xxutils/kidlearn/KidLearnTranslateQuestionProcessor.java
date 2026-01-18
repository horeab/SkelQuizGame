package libgdx.xxutils.kidlearn;

import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KidLearnTranslateQuestionProcessor {

    static final List<Language> RTL_LANGS = Arrays.asList(Language.ar, Language.he);

    static final String GAME_ID = "kidlearn";
    static final String ROOT_PATH_OLD = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/to_translate/";
    static final String ROOT_PATH_NEW = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/temp/";

    static int nrOfTranslations = 0;

    public static void main(String[] args) throws IOException {

//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.ro);
//        languages.remove(Language.en);
        List<Language> languages = Arrays.asList(Language.en);

        List<KidLearnCateg> categs = FlutterKidLearnProcessor.getCategs();

        for (Language lang : languages) {
            for (KidLearnCateg kidLearnCateg : categs) {

                String newQuestionPath = getNewFilePathForLang(kidLearnCateg, lang);
                if (new File(newQuestionPath).exists()) {
                    deleteTemp(newQuestionPath);
                }
                new File(newQuestionPath).mkdirs();

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

                    File myObj = new File(newQuestionPath + "/" + getFileName(diff));
                    myObj.createNewFile();
                    FileWriter myWriter = new FileWriter(myObj);
                    myWriter.write(returnValue);
                    myWriter.close();

                }
            }
        }
    }

    private static void deleteTemp(String pathname) {
        File folder = new File(pathname);
        Arrays.stream(Objects.requireNonNull(folder.listFiles())).forEach(File::delete);
        folder.delete();
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
