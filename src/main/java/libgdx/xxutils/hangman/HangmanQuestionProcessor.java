package libgdx.xxutils.hangman;

import libgdx.xxutils.FlutterQuestionProcessor;
import org.apache.commons.lang3.StringUtils;

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
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangmanarena.HangmanArenaQuestionDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class HangmanQuestionProcessor {


    public static final String ROOT_PATH = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
            "/implementations/hangmanarena/questions/";

    public static void main(String[] args) throws IOException {

//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages.remove(Language.ar);
        languages.remove(Language.he);
        languages.remove(Language.hi);
        languages.remove(Language.ja);
        languages.remove(Language.ko);
        languages.remove(Language.th);
        languages.remove(Language.vi);
        languages.remove(Language.zh);

        List<HangmanArenaQuestionDifficultyLevel> diffs = new ArrayList<>(Arrays.asList(HangmanArenaQuestionDifficultyLevel.values()));
        for (Language language : languages) {
            for (HangmanArenaQuestionDifficultyLevel diff : diffs) {

//                new File(ROOT_PATH + "temp/" + language.toString())
//                        .mkdir();
//                new File(ROOT_PATH + "temp/" + language.toString() + "/diff"
//                        + diff.getIndex())
//                        .mkdir();
                for (HangmanQuestionCategoryEnum cat : Arrays.asList(
                        HangmanQuestionCategoryEnum.cat0,
                        HangmanQuestionCategoryEnum.cat1,
                        HangmanQuestionCategoryEnum.cat2,
                        HangmanQuestionCategoryEnum.cat3,
                        HangmanQuestionCategoryEnum.cat4
                )) {
                    List<String> enQuestions = getEnglishQuestionCategory(cat, diff);

                    List<String> translatedQuestions = new ArrayList<>();
                    for (String enQ : enQuestions) {
                        translatedQuestions.add(TranslateQuestionProcessor.translateWord(language, enQ).trim());
                    }

                    String newFilePath = getLibgdxQuestionPath(language, false, cat.name(), diff);
                    System.out.println("newFilePath " + newFilePath);

                    File myObj = new File(newFilePath);
                    myObj.createNewFile();
                    FileWriter myWriter = new FileWriter(myObj);
                    String returnValue = String.join("\n", translatedQuestions);
                    myWriter.write(returnValue);
                    myWriter.close();

                }
            }
        }
    }

    private static List<String> getEnglishQuestionCategory(QuestionCategory cat,
                                                           QuestionDifficulty diff) {
        List<String> questions = new ArrayList<>();
        if (StringUtils.isNotBlank(cat.toString())) {
            String qPath = getLibgdxQuestionPath(Language.en, false, cat.toString(), diff);
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(qPath));
                String line = reader.readLine();
                while (line != null) {
                    questions.add(line);
                    line = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
            }
        }
        return questions;
    }


    private static String getLibgdxQuestionPath(Language language, boolean temp, String flutterCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language.toString() + "/diff" + diff.getIndex() + "/"
                + "questions_diff" + diff.getIndex() + "_" + flutterCat + ".txt";
    }
}
