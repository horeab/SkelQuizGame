package libgdx.xxutils.anatomy;

import org.apache.commons.lang3.StringUtils;

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
import libgdx.implementations.anatomy.AnatomyQuestionCategoryEnum;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.xxutils.TranslateQuestionProcessor;

public class AnatomyQuestionProcessor {

    static final int TOTAL_FLUTTER_NR_OF_CATS = 12;

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/anatomy/questions/";

    public static void main(String[] args) throws IOException {
        translateDescription();
    }

    public static void main2(String[] args) throws IOException {

        ///////
        ///////
//        List<Language> languages = Arrays.asList(Language.values());
        List<Language> languages = Arrays.asList(Language.ro);
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
                    + AnatomyQuestionDifficultyLevel._0.getIndex())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AnatomyQuestionDifficultyLevel._1.getIndex())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AnatomyQuestionDifficultyLevel._2.getIndex())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AnatomyQuestionDifficultyLevel._3.getIndex())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AnatomyQuestionDifficultyLevel._4.getIndex())
                    .mkdir();
            for (QuestionDifficulty diff : Arrays.asList(AnatomyQuestionDifficultyLevel._0,
                    AnatomyQuestionDifficultyLevel._1)) {
                for (QuestionCategory cat : oldNewCategMapping.keySet()) {

                    AnatomyQuestionDifficultyLevel targetDiff = AnatomyQuestionDifficultyLevel._0;
                    if (diff == AnatomyQuestionDifficultyLevel._0) {
                        if (cat.getIndex() >= TOTAL_FLUTTER_NR_OF_CATS * 2) {
                            continue;
                        }
                        targetDiff = cat.getIndex() >= TOTAL_FLUTTER_NR_OF_CATS
                                ? AnatomyQuestionDifficultyLevel._1
                                : AnatomyQuestionDifficultyLevel._0;
                    } else if (diff == AnatomyQuestionDifficultyLevel._1) {
                        if (cat.getIndex() < TOTAL_FLUTTER_NR_OF_CATS) {
                            continue;
                        } else if (cat.getIndex() < TOTAL_FLUTTER_NR_OF_CATS * 2) {
                            targetDiff = AnatomyQuestionDifficultyLevel._2;
                        } else if (cat.getIndex() < TOTAL_FLUTTER_NR_OF_CATS * 3) {
                            targetDiff = AnatomyQuestionDifficultyLevel._3;
                        }
                    }

                    moveQuestionCat(lang, cat, diff,
                            oldNewCategMapping.get(cat),
                            targetDiff);
                }
            }
        }
    }

    private static void translateDescription() throws IOException {
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.de);
//        languages.remove(Language.en);
//        languages.remove(Language.ro);
//        languages.removeAll(Arrays.asList(Language.values())
//                .subList(0, Arrays.asList(Language.values()).indexOf(Language.it)));
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.id));
        for (Language lang : languages) {
            for (QuestionDifficulty diff : Arrays.asList(AnatomyQuestionDifficultyLevel._0)) {
                for (QuestionCategory cat : Arrays.asList(
                        AnatomyQuestionCategoryEnum.cat7
                )) {
//                for (QuestionCategory cat : getQuestionParsers(diff).keySet()) {
                    List<String> enQuestions = getEnglishQuestions(cat, diff, true);
                    List<String> langQuestions = getQuestions(cat, diff, lang, true);
                    List<String> langQuestionsWithDescr = new ArrayList<>();
                    int i = 0;
                    for (String q : enQuestions) {

                        String[] split = q.split(":");
                        String descr = split[split.length - 1];
                        String translatedDescr = lang != Language.en
                                ? TranslateQuestionProcessor.translateWord(lang, descr)
                                : descr;
                        String langQ = langQuestions.get(i);
                        langQ = langQ + ":" + translatedDescr;
                        langQuestionsWithDescr.add(langQ);
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

    private static void moveQuestionCat(
            Language lang,
            QuestionCategory cat, QuestionDifficulty diff,
            QuestionCategory targetCat, QuestionDifficulty targetDiff) throws IOException {


        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers(targetDiff).get(targetCat);
        TranslateQuestionProcessor.QuestionParser oldQuestionParser = getOldQuestionParsers().get(cat);

        List<String> enQuestions = getEnglishQuestions(cat, diff, true);
        List<String> questions = new ArrayList<>();
        int i = 0;
        for (String enQuestion : enQuestions) {
            String question;
            String answer;
            List<String> options;
            if (diff == AnatomyQuestionDifficultyLevel._0) {
                if (lang != Language.en
                        && cat.getIndex() >= TOTAL_FLUTTER_NR_OF_CATS) {
                    question = TranslateQuestionProcessor.translateWord(lang,
                            oldQuestionParser.getQuestion(enQuestion)).trim();
                    answer = oldQuestionParser.getAnswer(enQuestion);
                    options = oldQuestionParser.getOptions(enQuestion);
                } else if (cat.getIndex() >= TOTAL_FLUTTER_NR_OF_CATS) {
                    question = oldQuestionParser.getQuestion(enQuestion);
                    answer = oldQuestionParser.getAnswer(enQuestion);
                    options = oldQuestionParser.getOptions(enQuestion);
                } else {
                    List<String> firstCatQuestions = getFirstCatQuestions(diff, cat, lang,
                            cat.getIndex());
                    question = firstCatQuestions.get(i).split(":")[2];

//                    if (Arrays.asList(
//                            Language.ar,
//                            Language.bg,
//                            Language.he,
//                            Language.sr,
//                            Language.sl
//                    ).contains(lang)) {
                    question = TranslateQuestionProcessor.translateWord(lang, question);
//                    }

                    answer = oldQuestionParser.getAnswer(enQuestion);
                    options = oldQuestionParser.getOptions(enQuestion);
                }
                questions.add(newQuestionParser
                        .formQuestion(lang, question, answer, options, oldQuestionParser.getQuestionPrefix(enQuestion), ""));
            } else if (diff == AnatomyQuestionDifficultyLevel._1) {
                if (cat.getIndex() >= TOTAL_FLUTTER_NR_OF_CATS) {
                    if (cat.getIndex() < TOTAL_FLUTTER_NR_OF_CATS * 2) {
                        List<String> firstCatQuestions = getFirstCatQuestions(diff, cat, lang,
                                cat.getIndex() - TOTAL_FLUTTER_NR_OF_CATS);
                        question = lang != Language.en
                                ? TranslateQuestionProcessor.translateWord(lang, enQuestion)
                                : enQuestion;
                        answer = firstCatQuestions.get(i);
                        options = Collections.emptyList();
                        questions.add(newQuestionParser
                                .formQuestion(lang, question, answer, options, oldQuestionParser.getQuestionPrefix(enQuestion), ""));
                    } else if (cat.getIndex() < TOTAL_FLUTTER_NR_OF_CATS * 3) {
                        List<String> firstCatQuestions = getFirstCatQuestions(diff, cat, lang,
                                cat.getIndex() - TOTAL_FLUTTER_NR_OF_CATS * 2);
                        question = lang != Language.en
                                ? TranslateQuestionProcessor.translateWord(lang, enQuestion.split(":")[0])
                                : enQuestion;
                        answer = firstCatQuestions.get(i);
                        options = Arrays.asList(enQuestion.split(":")[1].split(","));
                        questions.add(newQuestionParser
                                .formQuestion(lang, question, answer, options, oldQuestionParser.getQuestionPrefix(enQuestion), ""));
                    }
                }
            }
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

    static List<String> getFirstCatQuestions(QuestionDifficulty diff,
                                             QuestionCategory cat,
                                             Language language,
                                             int firstCategoryIndex) {

        String qPath = getLibgdxQuestionPath(language, false,
                "cat" + firstCategoryIndex, diff);
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

    private static Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> getQuestionParsers(QuestionDifficulty questionDifficulty) {
        Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> qParsers = new HashMap<>();
        TranslateQuestionProcessor.QuestionParser parser;
        if (questionDifficulty == AnatomyQuestionDifficultyLevel._0) {
            parser = new TranslateQuestionProcessor.ImageClickQuestionParser();
        } else if (questionDifficulty == AnatomyQuestionDifficultyLevel._1) {
            parser = new AnatomyNewDependentQuestionParser();
        } else {
            parser = new AnatomyDiseaseDependentQuestionParser();
        }
        qParsers.put(AnatomyQuestionCategoryEnum.cat0, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat1, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat2, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat3, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat4, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat5, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat6, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat7, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat8, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat9, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat10, parser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat11, parser);
        return qParsers;
    }

    private static Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> getOldQuestionParsers() {
        Map<QuestionCategory, TranslateQuestionProcessor.QuestionParser> qParsers = new HashMap<>();
        AnatomyDependentQuestionParser anatomyDependentQuestionParser = new AnatomyDependentQuestionParser();
        TranslateQuestionProcessor.OldImageClickQuestionParser oldImageClickQuestionParser = new TranslateQuestionProcessor.OldImageClickQuestionParser();
        qParsers.put(AnatomyQuestionCategoryEnum.cat0, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat1, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat2, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat3, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat4, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat5, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat6, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat7, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat8, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat9, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat10, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat11, oldImageClickQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat12, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat13, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat14, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat15, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat16, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat17, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat18, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat19, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat20, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat21, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat22, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat23, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat24, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat25, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat26, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat27, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat28, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat29, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat30, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat31, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat32, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat33, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat34, anatomyDependentQuestionParser);
        qParsers.put(AnatomyQuestionCategoryEnum.cat35, anatomyDependentQuestionParser);
        return qParsers;
    }

    public static Map<QuestionCategory, QuestionCategory> categories() {
        Map<QuestionCategory, QuestionCategory> oldNewCategMapping = new HashMap<>();
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat0, AnatomyQuestionCategoryEnum.cat0);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat1, AnatomyQuestionCategoryEnum.cat1);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat2, AnatomyQuestionCategoryEnum.cat2);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat3, AnatomyQuestionCategoryEnum.cat3);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat4, AnatomyQuestionCategoryEnum.cat4);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat5, AnatomyQuestionCategoryEnum.cat5);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat6, AnatomyQuestionCategoryEnum.cat6);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat7, AnatomyQuestionCategoryEnum.cat7);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat8, AnatomyQuestionCategoryEnum.cat8);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat9, AnatomyQuestionCategoryEnum.cat9);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat10, AnatomyQuestionCategoryEnum.cat10);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat11, AnatomyQuestionCategoryEnum.cat11);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat12, AnatomyQuestionCategoryEnum.cat0);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat13, AnatomyQuestionCategoryEnum.cat1);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat14, AnatomyQuestionCategoryEnum.cat2);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat15, AnatomyQuestionCategoryEnum.cat3);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat16, AnatomyQuestionCategoryEnum.cat4);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat17, AnatomyQuestionCategoryEnum.cat5);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat18, AnatomyQuestionCategoryEnum.cat6);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat19, AnatomyQuestionCategoryEnum.cat7);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat20, AnatomyQuestionCategoryEnum.cat8);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat21, AnatomyQuestionCategoryEnum.cat9);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat22, AnatomyQuestionCategoryEnum.cat10);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat23, AnatomyQuestionCategoryEnum.cat11);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat24, AnatomyQuestionCategoryEnum.cat0);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat25, AnatomyQuestionCategoryEnum.cat1);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat26, AnatomyQuestionCategoryEnum.cat2);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat27, AnatomyQuestionCategoryEnum.cat3);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat28, AnatomyQuestionCategoryEnum.cat4);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat29, AnatomyQuestionCategoryEnum.cat5);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat30, AnatomyQuestionCategoryEnum.cat6);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat31, AnatomyQuestionCategoryEnum.cat7);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat32, AnatomyQuestionCategoryEnum.cat8);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat33, AnatomyQuestionCategoryEnum.cat9);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat34, AnatomyQuestionCategoryEnum.cat10);
        oldNewCategMapping.put(AnatomyQuestionCategoryEnum.cat35, AnatomyQuestionCategoryEnum.cat11);
        return oldNewCategMapping;
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }


    public static class AnatomyDependentQuestionParser extends TranslateQuestionProcessor.QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            return "";
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[3];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[2].split("##", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        public String getQuestion(String rawString) {
            try {

                return rawString.split(":", -1)[1];
            } catch (Exception e) {
                return null;
            }
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

    public static class AnatomyNewDependentQuestionParser extends TranslateQuestionProcessor.QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s";
            Object[] array = {question, String.join(",", options), prefix};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return "";
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[2].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
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


    public static class AnatomyDiseaseDependentQuestionParser extends TranslateQuestionProcessor.QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s:%s:";
            Object[] array = {question, correctAnswer, String.join(",", options), prefix};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return "";
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[2].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
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
}
