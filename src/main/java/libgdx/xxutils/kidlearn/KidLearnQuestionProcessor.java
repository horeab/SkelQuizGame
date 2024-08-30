package libgdx.xxutils.kidlearn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class KidLearnQuestionProcessor {

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelClassicGame/src/main/resources/tournament_resources/implementations/kidlearn/";

    public static void main(String[] args) throws IOException {

        ///////
        ///////
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.en);
//        languages.remove(Language.ro);

        List<Language> languages = Arrays.asList(
                Language.ro
        );

        ///////
        ///////
        ///////
        ///////
        ///////

        for (Language lang : languages) {
            for (QuestionDifficulty diff : KidLearnQuestionDifficultyLevel.values()) {
                for (String bigCat : Arrays.asList("sci", "eng")) {
                    List<String> cats = bigCat.equals("eng") ?
                            Arrays.asList("hangman", "verb", "words")
                            : Arrays.asList("body", "feed", "recy", "state");
                    for (String cat : cats) {
                        moveQuestionCat(lang, bigCat, cat, diff);
                    }
                }
            }
        }
    }


    private static void moveQuestionCat(
            Language lang,
            String bigCat,
            String cat,
            QuestionDifficulty diff) throws IOException {

        List<String> enQuestions = getEnglishQuestions(bigCat, cat, diff);
        List<String> langQuestions = getQuestions(bigCat, cat, diff, lang);

        List<String> questions = new ArrayList<>();

        int i = 0;
        for (String enQuestion : langQuestions) {
            questions.add(enQuestion);
            i++;
        }
        String returnValue = String.join("\n", questions);

        System.out.println("writeee" + returnValue);
        String newFilePath = getLibgdxQuestionPath(lang, true, bigCat, cat, diff);
        System.out.println("newFilePath " + newFilePath);

        new File(newFilePath.substring(0, newFilePath.lastIndexOf("/"))).mkdirs();

        File myObj = new File(newFilePath);
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(returnValue);
        myWriter.close();
    }

    private static String translate(Language lang, String text) throws IOException {
        return TranslateQuestionProcessor.translateWord(lang, text);
    }

    public static List<String> getEnglishQuestions(String bigCat, String cat, QuestionDifficulty diff) {

        return getQuestions(bigCat, cat, diff, Language.en);
    }

    public static List<String> getQuestions(String bigCat, String cat, QuestionDifficulty diff, Language language) {
        String qPath = getLibgdxQuestionPath(language, false, bigCat, cat, diff);
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

    private static String getLibgdxQuestionPath(Language language, boolean temp, String bigCat, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + "questions/" + (temp ? "temp/" : "") + bigCat + (bigCat.equals("eng") ? "" : "/" + language) + "/" + libGdxCat + "/l" + diff.getIndex() + ".txt";
    }

}