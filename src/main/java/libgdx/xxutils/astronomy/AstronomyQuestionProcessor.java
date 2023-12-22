package libgdx.xxutils.astronomy;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/astronomy/questions/";

    public static void main(String[] args) throws IOException {

        ///////
        ///////
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.en);
//        languages.remove(Language.ro);

        List<Language> languages = Arrays.asList(
//                Language.el
                Language.en
//                Language.es
//                Language.fi
//                Language.fr
//                Language.hi
//                Language.hr
//                Language.hu
//                Language.it
//                Language.ro
//                Language.sl
        );

        ///////
        ///////
        ///////
        ///////
        ///////

        formatCateg17Q();

//        Map<QuestionCategory, QuestionCategory> oldNewCategMapping = categories();
//        for (Language lang : languages) {
//            new File(ROOT_PATH + "temp/" + lang.toString())
//                    .mkdir();
//            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
//                    + AstronomyDifficultyLevel._0.getIndex())
//                    .mkdir();
//
//            List<String> planetsEnQuestions = getEnglishQuestions(AstronomyCategoryEnum.cat0, AstronomyDifficultyLevel._0);
//            List<String> planetsLangQuestions = getQuestions(AstronomyCategoryEnum.cat0, AstronomyDifficultyLevel._0, lang);
//            Map<String, String> planets = new HashMap<>();
//            int i = 0;
//            TranslateQuestionProcessor.ImageClickQuestionParser imageClickQuestionParser = new TranslateQuestionProcessor.ImageClickQuestionParser();
//            for (String pl : planetsEnQuestions) {
//                planets.put(imageClickQuestionParser.getQuestion(pl), imageClickQuestionParser.getQuestion(planetsLangQuestions.get(i)));
//                i++;
//            }
//
//            for (QuestionDifficulty diff : Collections.singletonList(AstronomyDifficultyLevel._0)) {
//                for (QuestionCategory cat : oldNewCategMapping.keySet()) {
//                    moveQuestionCat(lang, cat, diff,
//                            oldNewCategMapping.get(cat),
//                            diff, planets);
//                }
//            }
//        }
    }

    private static void formatCateg17Q() throws IOException {
        for (Language lang : Language.values()) {
//        for (Language lang : Arrays.asList(Language.en)) {

            String newFilePath = getLibgdxQuestionPath(lang, true, AstronomyCategoryEnum.cat17.name(), AstronomyDifficultyLevel._0);
            BufferedReader reader;
            List<String> questions = new ArrayList<>();
            try {
                reader = new BufferedReader(new FileReader(newFilePath));
                String line = reader.readLine();
                while (line != null) {
                    questions.add(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
            }
            System.out.println(questions);

            String newFilePathToWrite = getLibgdxQuestionPath(lang, true, AstronomyCategoryEnum.cat18.name(), AstronomyDifficultyLevel._0);
            File myObj = new File(newFilePathToWrite);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(questions.stream().map(e -> e.split("=")[1]).collect(Collectors.joining("\n")));
            myWriter.close();
        }

    }

    private static void moveQuestionCat(
            Language lang,
            QuestionCategory cat, QuestionDifficulty diff,
            QuestionCategory targetCat, QuestionDifficulty targetDiff, Map<String, String> planets) throws IOException {

        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers(targetDiff).get(targetCat);
        TranslateQuestionProcessor.QuestionParser oldQuestionParser = getOldQuestionParsers().get(cat);

        List<String> enQuestions = getEnglishQuestions(cat, diff);
        List<String> questions = new ArrayList<>();

        int i = 0;
        for (String enQuestion : enQuestions) {
//            String question = oldQuestionParser.getQuestion(enQuestion);
            String questionEn = oldQuestionParser.getQuestion(enQuestion);
            String question;
            if (planets.containsKey(questionEn)) {
                question = planets.get(questionEn);
            } else {
                question = translate(lang, questionEn);
            }
            String answerEn = oldQuestionParser.getAnswer(enQuestion);
            String answer;
            if (planets.containsKey(answerEn)) {
                answer = planets.get(answerEn);
            } else {
                answer = translate(lang, answerEn);
            }
            String explanationEn = oldQuestionParser.getQuestionExplanation(enQuestion);
            String explanation = StringUtils.isBlank(explanationEn) ? "" : translate(lang, explanationEn);
            List<String> options = oldQuestionParser.getOptions(enQuestion).stream().map(
                    e -> {
                        try {
                            return planets.getOrDefault(e, translate(lang, e));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());


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
        String newFilePath = getLibgdxQuestionPath(lang, true, targetCat.name(), targetDiff);
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

    public static List<String> getEnglishQuestions(QuestionCategory cat, QuestionDifficulty diff) {

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
        oldNewCategMapping.put(AstronomyCategoryEnum.cat18, AstronomyCategoryEnum.cat18);
        return oldNewCategMapping;
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }

}