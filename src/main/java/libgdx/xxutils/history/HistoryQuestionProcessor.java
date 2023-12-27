package libgdx.xxutils.history;

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
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class HistoryQuestionProcessor {
    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/history/questions/";


    public static void main(String[] args) throws IOException {

//        translateAllLanguage();
        translateDescription();
    }

    private static void translateAllLanguage() throws IOException {

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.bg));
        for (Language lang : languages) {
            for (QuestionDifficulty diff : HistoryDifficultyLevel.values()) {
                Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> questionParsers = getQuestionParsers();
                for (QuestionCategory cat : questionParsers.keySet()) {
                    List<String> langQuestions = new ArrayList<>();
                    List<String> enQuestions = getEnglishQuestions(cat, diff, true);
                    TranslateQuestionProcessor.QuestionParser questionParser = questionParsers.get(cat);
                    for (String q : enQuestions) {
                        String question = questionParser.getQuestion(q);
                        String answer = questionParser.getAnswer(q);
                        List<String> options = questionParser.getOptions(q);
                        String prefix = questionParser.getQuestionPrefix(q);
                        String formQuestion = questionParser
                                .formQuestion(lang,
                                        TranslateQuestionProcessor.translateWord(lang, question),
                                        TranslateQuestionProcessor.translateWord(lang, answer),
                                        options.stream().map(
                                                option -> {
                                                    try {
                                                        return TranslateQuestionProcessor.translateWord(lang, option);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return option;
                                                }
                                        ).collect(Collectors.toList()),
                                        prefix, "");
                        if (!formQuestion.equals(q)) {
                            int i = 0;
                        }
                        langQuestions.add(formQuestion);
                    }

                    if (!langQuestions.isEmpty()) {
                        String allQ = String.join("\n", langQuestions);
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
    }

    private static Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> getQuestionParsers() {
        Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> qParsers = new HashMap<>();
        qParsers.put(HistoryCategoryEnum.cat0, new TimelineQuestionParser());
        qParsers.put(HistoryCategoryEnum.cat1, new TimelineQuestionParser());
        qParsers.put(HistoryCategoryEnum.cat2, new TranslateQuestionProcessor.DependentQuestionParser());
        qParsers.put(HistoryCategoryEnum.cat3, new TranslateQuestionProcessor.UniqueQuestionParser());
        qParsers.put(HistoryCategoryEnum.cat4, new TranslateQuestionProcessor.DependentQuestionParser());
        return qParsers;
    }

    private static void translateDescription() throws IOException {
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages = languages.subList(languages.indexOf(Language.it), languages.size());
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.ro));
        Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> questionParsers = getQuestionParsers();
        for (Language lang : languages) {
            for (QuestionDifficulty diff : HistoryDifficultyLevel.values()) {
                for (QuestionCategory cat : Arrays.asList(HistoryCategoryEnum.cat1, HistoryCategoryEnum.cat4)) {
                    List<String> enQuestions = getEnglishQuestions(cat, diff, true);
                    List<String> langQuestions = getQuestions(cat, diff, lang, true);
                    List<String> langQuestionsWithDescr = new ArrayList<>();
                    TranslateQuestionProcessor.QuestionParser questionParser = questionParsers.get(cat);
                    int i = 0;
                    for (String q : enQuestions) {
                        String[] split = q.split(":");
                        String descr = split[split.length - 1];
                        String translatedDescr = lang != Language.en
                                ? TranslateQuestionProcessor.translateWord(lang, descr)
                                : descr;
                        String langQ = langQuestions.get(i);

                        String question = questionParser.getQuestion(langQ);
                        String answer = questionParser.getAnswer(langQ);
                        List<String> options = questionParser.getOptions(langQ);
                        String prefix = questionParser.getQuestionPrefix(langQ);

                        langQuestionsWithDescr.add(
                                questionParser.formQuestion(lang, question, answer, options, prefix, translatedDescr)
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

    static List<String> getEnglishQuestions(QuestionCategory cat, QuestionDifficulty diff, boolean isTemp) {
        return getQuestions(cat, diff, Language.en, isTemp);
    }

    static List<String> getQuestions(QuestionCategory cat, QuestionDifficulty diff, Language lang, boolean isTemp) {

        String qPath = getLibgdxQuestionPath(lang, isTemp, cat.name(), diff);
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

    public static class TimelineQuestionParser extends TranslateQuestionProcessor.QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s:%s";
            Object[] array = {question, correctAnswer, explanation};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[1];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Collections.emptyList();
        }

        @Override
        public String getQuestion(String rawString) {
            return rawString.split(":", -1)[0];
        }

        @Override
        public String getQuestionPrefix(String rawString) {
            return "";
        }

        @Override
        public String getQuestionExplanation(String rawString) {
            return "";
        }
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "aaatemp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

}