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
import libgdx.implementations.flags.FlagsDifficultyLevel;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;
import libgdx.implementations.paintings.PaintingsQuestionCategoryEnum;
import libgdx.implementations.paintings.PaintingsQuestionDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class PaintingsQuestionProcessor {

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/paintings/questions/";

    public static void main(String[] args) throws IOException {

        ///////
        ///////
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages = Arrays.asList(Language.ro);

        ///////
        ///////
        ///////
        ///////
        ///////

//        for (Language lang : languages) {
//            new File(ROOT_PATH + "temp/" + lang.toString())
//                    .mkdir();
//            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
//                    + PaintingsQuestionDifficultyLevel._0.getIndex())
//                    .mkdir();
//
//            for (QuestionDifficulty diff : Collections.singletonList(PaintingsQuestionDifficultyLevel._0)) {
//                for (QuestionCategory cat : PaintingsQuestionCategoryEnum.values()) {
//                    moveQuestionCat(lang, cat, diff);
//                }
//            }
//        }

//        for (Language lang : languages) {
//            for (QuestionDifficulty diff : Collections.singletonList(PaintingsQuestionDifficultyLevel._0)) {
//                for (QuestionCategory cat : Collections.singletonList(PaintingsQuestionCategoryEnum.cat5)) {
//                    translateNewCat(lang, cat, diff);
//                }
//            }
//        }

        translateDescription();
    }


    private static void translateDescription() throws IOException {
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//                languages = languages.subList(languages.indexOf(Language.hr), languages.size());
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.ro));
//        languages.remove(Language.en);
//        languages.remove(Language.ro);
        Map<QuestionCategory, MutablePair<TranslateQuestionProcessor.QuestionParser, TranslateQuestionProcessor.QuestionParser>> questionParsers = getQuestionParsers();
        for (Language lang : languages) {
            for (QuestionDifficulty diff : PaintingsQuestionDifficultyLevel.values()) {
                for (QuestionCategory cat : Arrays.asList(PaintingsQuestionCategoryEnum.cat0)) {

                    List<String> enQuestions = getQuestions(cat, diff, Language.en, true);
                    List<String> langQuestions = getQuestions(cat, diff, lang, true);
                    List<String> langQuestionsWithDescr = new ArrayList<>();
                    TranslateQuestionProcessor.QuestionParser questionParser = questionParsers.get(cat).right;
                    int i = 0;
                    for (String enQ : enQuestions) {
                        String langQ = langQuestions.get(i);

                        String questionExplanation = questionParser.getQuestionExplanation(langQ);
                        questionExplanation = lang != Language.en && i == 11
                                ? TranslateQuestionProcessor.translateWord(lang, questionParser.getQuestionExplanation(enQ))
                                : questionExplanation;
                        String question = questionParser.getQuestion(langQ);
                        String answer = questionParser.getAnswer(langQ);
                        List<String> options = questionParser.getOptions(langQ);
                        String prefix = questionParser.getQuestionPrefix(langQ);
                        langQuestionsWithDescr.add(
                                questionParser.formQuestion(lang, question, answer, options, prefix, questionExplanation)
                        );
                        i++;
                    }

                    String allQ = String.join("\n", langQuestionsWithDescr);
                    System.out.println("writeee" + allQ);
                    String newFilePath = getLibgdxQuestionPath(lang, true, cat.name(), diff);
                    System.out.println("newFilePath " + newFilePath);

                    File myObj = new File(newFilePath);
                    myObj.createNewFile();
                    FileWriter myWriter = new FileWriter(myObj);
                    myWriter.write(allQ);
                    myWriter.close();
                }
            }
        }
    }

    private static void translateNewCat(Language lang,
                                        QuestionCategory cat, QuestionDifficulty diff) throws IOException {
        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers().get(cat).right;

        List<String> enQuestions = getQuestions(cat, diff, lang, true);
        List<String> questions = new ArrayList<>();

        int i = 0;
        for (String enQuestion : enQuestions) {
            String question = translate(lang, newQuestionParser.getQuestion(enQuestion));
            String answer = translate(lang, newQuestionParser.getAnswer(enQuestion));
            String explanation = translate(lang, newQuestionParser.getQuestionExplanation(enQuestion));
            List<String> options = new ArrayList<>(newQuestionParser.getOptions(enQuestion));
            questions.add(newQuestionParser
                    .formQuestion(lang, question, answer, options, newQuestionParser.getQuestionPrefix(enQuestion),
                            explanation));
            i++;
        }

        String returnValue = String.join("\n", questions);

        System.out.println("writeee" + returnValue);
        String newFilePath = getLibgdxQuestionPath(lang, true, cat.name(), diff);
        System.out.println("newFilePath " + newFilePath);

        File myObj = new File(newFilePath);
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(returnValue);
        myWriter.close();
    }

    private static void moveQuestionCat(
            Language lang,
            QuestionCategory cat, QuestionDifficulty diff) throws IOException {

        TranslateQuestionProcessor.QuestionParser oldQuestionParser = getQuestionParsers().get(cat).left;
        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers().get(cat).right;

        List<String> enQuestions = getQuestions(cat, diff, lang, false);
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
        return getQuestions(cat, diff, Language.en, false);
    }

    public static List<String> getQuestions(QuestionCategory cat, QuestionDifficulty diff, Language language, boolean temp) {
        String qPath = getLibgdxQuestionPath(language, temp, cat.name(), diff);
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
//        qParsers.put(PaintingsQuestionCategoryEnum.cat5, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        return qParsers;
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

}