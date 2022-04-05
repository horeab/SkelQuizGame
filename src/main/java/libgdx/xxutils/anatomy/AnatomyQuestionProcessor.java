package libgdx.xxutils.anatomy;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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


    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/anatomy/questions/";

    public static void main(String[] args) throws IOException {

        List<Language> languages = Arrays.asList(Language.en);
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);

        Map<QuestionCategory, QuestionCategory> oldNewCategMapping = categories();
        for (Language lang : languages) {
            new File(ROOT_PATH + "temp/" + lang.toString()).mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AnatomyQuestionDifficultyLevel._0.getIndex())
                    .mkdir();
            new File(ROOT_PATH + "temp/" + lang.toString() + "/diff"
                    + AnatomyQuestionDifficultyLevel._1.getIndex())
                    .mkdir();
            for (QuestionDifficulty diff : Arrays.asList(AnatomyQuestionDifficultyLevel._0)) {
                for (QuestionCategory cat : oldNewCategMapping.keySet()) {
                    moveQuestionCat(lang, cat, diff,
                            oldNewCategMapping.get(cat), cat.getIndex() > 11
                                    ? AnatomyQuestionDifficultyLevel._1 :
                                    AnatomyQuestionDifficultyLevel._0);
                }
            }
        }

    }

    private static void moveQuestionCat(
            Language lang,
            QuestionCategory cat, QuestionDifficulty diff, QuestionCategory targetCat, QuestionDifficulty targetDiff) {


        TranslateQuestionProcessor.QuestionParser newQuestionParser = getQuestionParsers(targetDiff).get(targetCat);
        TranslateQuestionProcessor.QuestionParser oldQuestionParser = getOldQuestionParsers().get(cat);

        String qPath = getLibgdxQuestionPath(lang, false, cat.name(), diff);
        BufferedReader reader;
        List<String> enQuestions = getEnglishQuestions(cat, diff);
        List<String> questions = new ArrayList<>();
        int l = 0;
        try {
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            while (line != null) {

                System.out.println("xxxxxx" + line);

                String question;
                String answer;
                List<String> options;
                if (lang != Language.en && cat.getIndex() > 11) {
                    question = line;
                    answer = oldQuestionParser.getAnswer(enQuestions.get(l));
                    options = oldQuestionParser.getOptions(enQuestions.get(l));
                } else {
                    question = oldQuestionParser.getQuestion(line);
                    answer = oldQuestionParser.getAnswer(line);
                    options = oldQuestionParser.getOptions(line);
                }

                questions.add(newQuestionParser
                        .formQuestion(lang, question, answer, options, oldQuestionParser.getQuestionPrefix(line)));
                line = reader.readLine();
                l++;
            }
            String returnValue = String.join("\n", questions);
            System.out.println("writeee" + returnValue);
            String newFilePath = getLibgdxQuestionPath(lang, true, targetCat.name(), targetDiff);
            System.out.println("newFilePath " + newFilePath);

            File myObj = new File(newFilePath);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(returnValue);
            myWriter.close();


            reader.close();
        } catch (IOException e) {
        }
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
        TranslateQuestionProcessor.QuestionParser parser;
        if (questionDifficulty == AnatomyQuestionDifficultyLevel._0) {
            parser = new TranslateQuestionProcessor.ImageClickQuestionParser();
        } else {
            parser = new AnatomyNewDependentQuestionParser();
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
        return oldNewCategMapping;
    }

    private static String getLibgdxQuestionPath(Language language, boolean temp, String libGdxCat, QuestionDifficulty diff) {
        return ROOT_PATH + (temp ? "temp/" : "") + language + "/diff" + diff.getIndex() + "/questions_diff" + diff.getIndex() +
                "_" + libGdxCat + ".txt";
    }


    public static class AnatomyDependentQuestionParser extends TranslateQuestionProcessor.QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix) {
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
            return rawString.split(":", -1)[1];
        }

        @Override
        public String getQuestionPrefix(String rawString) {
            return "";
        }
    }

    public static class AnatomyNewDependentQuestionParser extends TranslateQuestionProcessor.QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix) {
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
    }
}
