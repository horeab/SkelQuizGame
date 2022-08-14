package libgdx.xxutils.astronomy;

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
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomyDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class AstronomyQuestionProcessor {

    public static final List<Language> NEWLANGS = Arrays.asList(
            Language.ar,
            Language.bg,
            Language.he,
            Language.sr,
            Language.sl
    );


    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/astronomy/questions/";

    public static void main(String[] args) throws IOException {

        ///////
        ///////
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.en);
//        languages.remove(Language.ro);


        List<Language> languages = Arrays.asList(
                Language.de
//                Language.en,
//                Language.ro
        );
        ///////
        ///////
        ///////
        ///////
        ///////

        Map<QuestionCategory, QuestionCategory> oldNewCategMapping = categories();
        for (Language lang : languages) {
            new File(ROOT_PATH + "temp/" + lang.toString())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AstronomyDifficultyLevel._0.getIndex())
                    .mkdir();
            for (QuestionDifficulty diff : Collections.singletonList(AstronomyDifficultyLevel._0)) {
                for (QuestionCategory cat : oldNewCategMapping.keySet()) {
                    moveQuestionCat(lang, cat, diff,
                            oldNewCategMapping.get(cat),
                            diff);
                }
            }
        }
    }

    private static void moveQuestionCat(
            Language lang,
            QuestionCategory cat, QuestionDifficulty diff,
            QuestionCategory targetCat, QuestionDifficulty targetDiff) throws IOException {

        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers(targetDiff).get(targetCat);
        TranslateQuestionProcessor.QuestionParser oldQuestionParser = getOldQuestionParsers().get(cat);

        List<String> enQuestions = getEnglishQuestions(cat, diff);
        List<String> questions = new ArrayList<>();

        boolean translate = cat != AstronomyCategoryEnum.cat0 || NEWLANGS.contains(lang);

        for (String enQuestion : enQuestions) {
//            String question = oldQuestionParser.getQuestion(enQuestion);
            String question = translate(lang, oldQuestionParser.getQuestion(enQuestion), translate);
            String answer = translate(lang, oldQuestionParser.getAnswer(enQuestion), translate);
            List<String> options = oldQuestionParser.getOptions(enQuestion).stream().map(
                    e -> {
                        try {
                            return translate(lang, e, translate);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());


            questions.add(newQuestionParser
                    .formQuestion(lang, question, answer, options, oldQuestionParser.getQuestionPrefix(enQuestion),
                            lang == Language.en ? oldQuestionParser.getQuestionExplanation(enQuestion) : "xxx"));
        }
        String returnValue = String.join("\n", questions);

        if (questions.isEmpty()) {
            int jj = 0;
        }

        System.out.println("writeee" + returnValue);
        String newFilePath = getLibgdxQuestionPath(lang, true, targetCat.name(), targetDiff);
        System.out.println("newFilePath " + newFilePath);

        File myObj = new File(newFilePath);
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(returnValue);
        myWriter.close();
    }

    private static String translate(Language lang, String text, boolean translate) throws IOException {
        return translate ? TranslateQuestionProcessor.translateWord(lang, text) : text;
    }

    static List<String> getEnglishQuestions(QuestionCategory cat, QuestionDifficulty diff) {

        String qPath = getLibgdxQuestionPath(Language.en, false, cat.name(), diff);
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

    private static Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> getQuestionParsers(QuestionDifficulty questionDifficulty) {
        Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> qParsers = new HashMap<>();
        qParsers.put(AstronomyCategoryEnum.cat0, new TranslateQuestionProcessor.ImageClickQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat7, new TranslateQuestionProcessor.UniqueQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat8, new TranslateQuestionProcessor.UniqueQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat9, new TranslateQuestionProcessor.DependentQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat10, new TranslateQuestionProcessor.UniqueQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat11, new TranslateQuestionProcessor.DependentQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat12, new TranslateQuestionProcessor.DependentQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat13, new TranslateQuestionProcessor.DependentQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat14, new TranslateQuestionProcessor.DependentQuestionParser());
        qParsers.put(AstronomyCategoryEnum.cat15, new TranslateQuestionProcessor.DependentQuestionParser());
        return qParsers;
    }

    private static Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> getOldQuestionParsers() {
        Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> qParsers = new HashMap<>();
        TranslateQuestionProcessor.OldImageClickQuestionParser oldImageClickQuestionParser = new TranslateQuestionProcessor.OldImageClickQuestionParser();
        TranslateQuestionProcessor.OldUniqueQuestionParser uniqueQuestionParser = new TranslateQuestionProcessor.OldUniqueQuestionParser();
        TranslateQuestionProcessor.OldDependentQuestionParser dependentQuestionParser = new TranslateQuestionProcessor.OldDependentQuestionParser();
        qParsers.put(AstronomyCategoryEnum.cat0, oldImageClickQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat1, uniqueQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat2, uniqueQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat3, dependentQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat4, dependentQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat5, uniqueQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat6, dependentQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat7, dependentQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat8, dependentQuestionParser);
        qParsers.put(AstronomyCategoryEnum.cat9, dependentQuestionParser);
        return qParsers;
    }

    public static Map<QuestionCategory, QuestionCategory> categories() {
        Map<QuestionCategory, QuestionCategory> oldNewCategMapping = new HashMap<>();
        oldNewCategMapping.put(AstronomyCategoryEnum.cat0, AstronomyCategoryEnum.cat0);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat1, AstronomyCategoryEnum.cat7);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat2, AstronomyCategoryEnum.cat8);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat3, AstronomyCategoryEnum.cat9);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat4, AstronomyCategoryEnum.cat11);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat5, AstronomyCategoryEnum.cat10);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat6, AstronomyCategoryEnum.cat12);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat7, AstronomyCategoryEnum.cat13);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat8, AstronomyCategoryEnum.cat14);
        oldNewCategMapping.put(AstronomyCategoryEnum.cat9, AstronomyCategoryEnum.cat15);
        return oldNewCategMapping;
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

}