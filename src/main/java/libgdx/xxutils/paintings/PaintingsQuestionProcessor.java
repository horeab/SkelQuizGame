package libgdx.xxutils.paintings;

import org.apache.commons.lang3.tuple.MutablePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.paintings.PaintingsQuestionCategoryEnum;
import libgdx.implementations.paintings.PaintingsQuestionDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class PaintingsQuestionProcessor {

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/paintings/questions/";

    public static void main(String[] args) throws IOException {

        ///////
        ///////
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.en);
//        languages.remove(Language.ro);

        List<Language> languages = Arrays.asList(
                Language.sr
        );

        ///////
        ///////
        ///////
        ///////
        ///////

        for (Language lang : languages) {
            new File(ROOT_PATH + "temp/" + lang.toString())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + PaintingsQuestionDifficultyLevel._0.getIndex())
                    .mkdir();

            for (QuestionDifficulty diff : Collections.singletonList(PaintingsQuestionDifficultyLevel._0)) {
                for (QuestionCategory cat : PaintingsQuestionCategoryEnum.values()) {
                    moveQuestionCat(lang, cat, diff);
                }
            }
        }
    }

    private static void moveQuestionCat(
            Language lang,
            QuestionCategory cat, QuestionDifficulty diff) throws IOException {

        TranslateQuestionProcessor.QuestionParser oldQuestionParser = getQuestionParsers().get(cat).left;
        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers().get(cat).right;

        List<String> enQuestions = getQuestions(cat, diff);
        List<String> questions = new ArrayList<>();

        int i = 0;
        for (String enQuestion : enQuestions) {
            String question = translate(lang, oldQuestionParser.getQuestion(enQuestion));
            String answer = translate(lang, oldQuestionParser.getAnswer(enQuestion));
            String explanation = translate(lang, oldQuestionParser.getQuestionExplanation(enQuestion));
            List<String> options = new ArrayList<>(oldQuestionParser.getOptions(enQuestion));
            questions.add(newQuestionParser
                    .formQuestion(lang, question, answer, options, oldQuestionParser.getQuestionPrefix(enQuestion),
                            explanation));
            i++;
        }

        String returnValue = String.join("\n", questions);

        if (questions.isEmpty()) {
            int jj = 0;
        }

        System.out.println("writeee" + returnValue);
        String newFilePath = getLibgdxQuestionPath(lang, true, cat.name(), diff);
        System.out.println("newFilePath " + newFilePath);

        File myObj = new File(newFilePath);
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(returnValue);
        myWriter.close();
    }

    private static String translate(Language lang, String text) throws IOException {
        return TranslateQuestionProcessor.translateWord(lang, text);
    }

    public static List<String> getQuestions(QuestionCategory cat, QuestionDifficulty diff) {
        return getQuestions(cat, diff, Language.en);
    }

    public static List<String> getQuestions(QuestionCategory cat, QuestionDifficulty diff, Language language) {
        String qPath = getLibgdxQuestionPath(language, false, cat.name(), diff);
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

    private static Map<QuestionCategory, MutablePair<TranslateQuestionProcessor.QuestionParser, TranslateQuestionProcessor.QuestionParser>> getQuestionParsers() {
        Map<QuestionCategory, MutablePair<TranslateQuestionProcessor.QuestionParser, TranslateQuestionProcessor.QuestionParser>> qParsers = new HashMap<>();
        qParsers.put(PaintingsQuestionCategoryEnum.cat0, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(PaintingsQuestionCategoryEnum.cat1, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(PaintingsQuestionCategoryEnum.cat2, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(PaintingsQuestionCategoryEnum.cat3, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(PaintingsQuestionCategoryEnum.cat4, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        return qParsers;
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

}