package libgdx.xxutils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;

class TranslateQuestionProcessor {

    static int nrOfTranslations = 0;

    public static void main(String[] args) throws IOException {

        List<Language> languages = Arrays.asList(Language.ro);
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/history/questions/";

        Map<Pair<QuestionDifficulty, QuestionCategory>, Pair<Integer, Integer>> questionsConfigToMove = new HashMap<>();
        //////////////////////////////
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._3, HistoryCategoryEnum.cat0),
                new MutablePair<>(0, 4)
        );
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._2, HistoryCategoryEnum.cat0),
                new MutablePair<>(5, 10)
        );
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._1, HistoryCategoryEnum.cat0),
                new MutablePair<>(11, 18)
        );
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._0, HistoryCategoryEnum.cat0),
                new MutablePair<>(19, 44)
        );
        //////////////////////////////
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._2, HistoryCategoryEnum.cat1),
                new MutablePair<>(0, 9)
        );
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._1, HistoryCategoryEnum.cat1),
                new MutablePair<>(10, 18)
        );
        questionsConfigToMove.put(
                new MutablePair<>(HistoryDifficultyLevel._0, HistoryCategoryEnum.cat1),
                new MutablePair<>(19, 33)
        );
        //////////////////////////////

        List<String> questionsConfigSourceCat0 = new ArrayList<>();
        List<String> questionsConfigSourceCat1 = new ArrayList<>();
        List<HistoryCategoryEnum> categoriesToMoveQuestions = Arrays.asList(HistoryCategoryEnum.cat0, HistoryCategoryEnum.cat1);

        Map<QuestionCategory, QuestionParser> qParsers = new HashMap<>();
        qParsers.put(HistoryCategoryEnum.cat2, new DependentQuestionParser());
        qParsers.put(HistoryCategoryEnum.cat3, new UniqueQuestionParser());
        qParsers.put(HistoryCategoryEnum.cat4, new DependentQuestionParser());

        for (Language lang : languages) {

            translateCountry(Language.de, "Belgium");

            for (QuestionDifficulty diff : HistoryDifficultyLevel.values()) {

                String specificLangPathDirectory = rootPath + "aaatemp/" + lang.toString() + "/diff" + diff.getIndex();
                new File(specificLangPathDirectory).mkdirs();

                for (HistoryCategoryEnum category : HistoryCategoryEnum.values()) {

                    String fileName = "questions_diff" + diff.getIndex() +
                            "_cat" + category.getIndex() + ".txt";

                    String qPath = rootPath + "en/diff" + diff.getIndex() + "/" + fileName;


                    if (diff == HistoryDifficultyLevel._0 && category == HistoryCategoryEnum.cat0) {
                        qPath = rootPath + lang.toString() + "/diff" + diff.getIndex() + "/" + fileName;
                        questionsConfigSourceCat0 = readFileContents(qPath);
                    } else if (diff == HistoryDifficultyLevel._0 && category == HistoryCategoryEnum.cat1) {
                        qPath = rootPath + lang.toString() + "/diff" + diff.getIndex() + "/" + fileName;
                        questionsConfigSourceCat1 = readFileContents(qPath);
                    }


                    Path path = Paths.get(qPath);

                    boolean exists = Files.exists(path);
                    System.out.println(exists);

                    if (exists) {
                        String returnValue;
                        if (category == HistoryCategoryEnum.cat0 || category == HistoryCategoryEnum.cat1) {

                            Pair<Integer, Integer> integerIntegerPair = questionsConfigToMove.get(new MutablePair<>(diff, category));
                            List<String> list;

                            if (category == HistoryCategoryEnum.cat0) {
                                list = questionsConfigSourceCat0;
                            } else {
                                list = questionsConfigSourceCat1;
                            }
                            returnValue = String.join("\n",
                                    list.subList(
                                            integerIntegerPair.getLeft(),
                                            integerIntegerPair.getRight() + 1));

                        } else {

                            QuestionParser questionParser = qParsers.get(category);
                            List<String> linesFromFile = readFileContents(qPath);

                            List<String> translatedFiles = new ArrayList<>();
                            for (String line : linesFromFile) {
                                try {
                                    String question = translateWord(lang, questionParser.getQuestion(line));
                                    String answer = translateWord(lang, questionParser.getAnswer(line));
                                    List<String> options = questionParser.getOptions(line).stream()
                                            .map(e -> {
                                                try {
                                                    return translateWord(lang, e);
                                                } catch (IOException ex) {
                                                    throw new RuntimeException();
                                                }
                                            }).collect(Collectors.toList());
                                    String prefix = translateWord(lang, questionParser.getQuestionPrefix(line));

                                    translatedFiles.add(questionParser.formQuestion(question, answer, options, prefix));
                                } catch (Exception ex) {
                                    System.out.println(line);
                                    throw ex;
                                }
                            }

                            returnValue = String.join("\n", translatedFiles);
                        }

                        File myObj = new File(specificLangPathDirectory + "/" + fileName);
                        myObj.createNewFile();
                        FileWriter myWriter = new FileWriter(myObj);
                        myWriter.write(returnValue);
                        myWriter.close();
                    }
                }
            }
        }
    }

    private static String translateWord(Language translateTo, String text) throws IOException {
        String res = translateCountry(translateTo, text);
        if (NumberUtils.isDigits(text)) {
            res = text;
            System.out.println("is number " + res);
        }
        if (StringUtils.isBlank(text)) {
            res = text;
        }
        if (res == null) {
            nrOfTranslations++;
//            res = text;
            res = TranslateTool.translate(Language.en.toString(), translateTo.toString(), text);
            System.out.println("translated: " + text + " ___to___ " + res + " ----- as nr " + nrOfTranslations);
        }
        return res;
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

    private static String translateCountry(Language translateTo, String countryName) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/countries.txt";
        List<String> englishCountries = readFileContents(String.format(rootPath, Language.en.toString()));
        List<String> translatedCountries = readFileContents(String.format(rootPath, translateTo.toString()));

        if (englishCountries.contains(countryName)) {
            return translatedCountries.get(englishCountries.indexOf(countryName));
        }

        return null;
    }

    abstract static class QuestionParser {

        abstract String getAnswer(String rawString);

        abstract List<String> getOptions(String rawString);

        abstract String getQuestion(String rawString);

        abstract String getQuestionPrefix(String rawString);

        abstract String formQuestion(String question, String correctAnswer, List<String> options, String prefix);
    }

    static class DependentQuestionParser extends QuestionParser {

        @Override
        String formQuestion(String question, String correctAnswer, List<String> options, String prefix) {
            String format = "%s:%s:%s:%s";
            return String.format(format, question, correctAnswer, String.join(",", options), prefix);
        }

        @Override
        String getAnswer(String rawString) {
            return rawString.split(":", -1)[1];
        }

        @Override
        List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[2].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        String getQuestion(String rawString) {
            return rawString.split(":", -1)[0];
        }

        @Override
        String getQuestionPrefix(String rawString) {
            return rawString.split(":", -1)[3];
        }
    }

    static class UniqueQuestionParser extends QuestionParser {

        @Override
        String formQuestion(String question, String correctAnswer, List<String> options, String prefix) {
            String format = "%s::%s::%s";
            return String.format(format, question, String.join("##", options), correctAnswer);
        }

        @Override
        String getAnswer(String rawString) {
            return rawString.split("::", -1)[2];
        }

        @Override
        List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split("::", -1)[1].split("##", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        String getQuestion(String rawString) {
            return rawString.split("::", -1)[0];
        }

        @Override
        String getQuestionPrefix(String rawString) {
            return "";
        }
    }
}
