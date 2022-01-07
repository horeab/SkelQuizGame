package libgdx.xxutils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
import libgdx.implementations.geoquiz.QuizQuestionCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;
import libgdx.implementations.skelgame.question.QuestionParser;

class TranslateQuestionProcessor {

    static final List<Language> RTL_LANGS = Arrays.asList(Language.ar, Language.he);

    static final String GAME_ID = "quizgame";
    static final String ROOT_PATH_OLD = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/questions/";
    static final String ROOT_PATH_NEW = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/questions/aaatemp/";

    static int nrOfTranslations = 0;

    public static void main(String[] args) throws IOException {

//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.ro);
//        languages.remove(Language.en);
        List<Language> languages = Arrays.asList(Language.en, Language.ro);

        for (Language lang : languages) {
            List<QuestionDifficulty> difficulties = Arrays.asList(HistoryDifficultyLevel.values());
            for (QuestionDifficulty diff : difficulties) {

                String specificLangPathDirectory = ROOT_PATH_OLD + "aaatemp/" + lang.toString() + "/diff" + diff.getIndex();
                new File(specificLangPathDirectory).mkdirs();

                for (QuizQuestionCategoryEnum category : QuizQuestionCategoryEnum.values()) {

                    moveQuestionToNewFolderAndFormat(lang, diff, specificLangPathDirectory, category);
//                    translateQuestion(lang, diff, specificLangPathDirectory, category);
                }
            }
        }
    }

    private static void moveQuestionToNewFolderAndFormat(Language langToMove,
                                                         QuestionDifficulty diff,
                                                         String specificLangPathDirectory,
                                                         QuizQuestionCategoryEnum category) throws IOException {

        if ((category == QuizQuestionCategoryEnum.cat1 || category == QuizQuestionCategoryEnum.cat3) && langToMove != Language.en) {
            return;
        }

        Map<QuestionCategory, QuestionParser> qParsers = getQuestionParsers();
        Map<QuestionCategory, QuestionParser> oldQParsers = getOldQuestionParsers();
        String fileName = getFileName(diff, category);
        String qPath = getOldFilePathForLang(diff, fileName, langToMove);
        Path path = Paths.get(qPath);
        Map<QuestionCategory, String> prefixForMovedQuestions = getPrefixForMovedQuestions();

        boolean exists = Files.exists(path);

        if (exists) {

            QuestionParser newQuestionParser = qParsers.get(category);
            QuestionParser oldQuestionParser = oldQParsers.get(category);
            List<String> linesFromFile = readFileContents(qPath);

            List<String> movedFile = new ArrayList<>();
            int lineNr = 0;
            for (String line : linesFromFile) {
                try {
                    String question = oldQuestionParser.getQuestion(line);
                    if (category == QuizQuestionCategoryEnum.cat2) {
                        question = question.replace("What is the capital of ", "").replace("?", "");
                    }
                    String answer = oldQuestionParser.getAnswer(line);
                    //
                    // check country names
                    if (Arrays.asList(QuizQuestionCategoryEnum.cat1,
                            QuizQuestionCategoryEnum.cat2,
                            QuizQuestionCategoryEnum.cat3)
                            .contains(category)) {
                        String cName;
                        if (Arrays.asList(QuizQuestionCategoryEnum.cat1,
                                QuizQuestionCategoryEnum.cat3)
                                .contains(category)) {
                            cName = answer;
                        } else {
                            cName = question;
                        }
                        String translated = translateCountry(langToMove, cName);
                        if (category == QuizQuestionCategoryEnum.cat2 && langToMove != Language.en) {
                            translated = loadEnglishValue(diff, category, lineNr, false);
                        }
                        if (translated == null) {
                            throw new RuntimeException("no country found for " + cName + " and cat " + category);
                        }
                        List<String> englishCountries = getCountries(Language.en);

                        if (Arrays.asList(QuizQuestionCategoryEnum.cat1,
                                QuizQuestionCategoryEnum.cat3)
                                .contains(category)) {
                            if (langToMove != Language.en) {
                                answer = translated;
                            } else {
                                answer = String.valueOf(englishCountries.indexOf(translated));
                            }
                        } else {
                            if (langToMove != Language.en) {
                                question = translated;
                            } else {
                                question = String.valueOf(englishCountries.indexOf(translated));
                            }
                        }
                    }

                    ///
                    //Not needed for GeoQuiz
                    List<String> options = new ArrayList<>();
                    ///
                    //
//                    List<String> options = oldQuestionParser.getOptions(line).stream()
//                            .map(e -> {
//                                try {
//                                    return translateWord(langToMove, e);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException();
//                                }
//                            }).collect(Collectors.toList());
                    String prefix = prefixForMovedQuestions.get(category);

                    movedFile.add(newQuestionParser.formQuestion(langToMove, question, answer, options, prefix));
                } catch (Exception ex) {
                    System.out.println(line);
                    throw ex;
                }
                lineNr++;
            }

            String returnValue = String.join("\n", movedFile);

            File myObj = new File(specificLangPathDirectory + "/" + fileName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(returnValue);
            myWriter.close();
        }
    }

    private static String loadEnglishValue(QuestionDifficulty diff,
                                           QuizQuestionCategoryEnum category,
                                           int lineNr,
                                           boolean returnAnswerValue) throws IOException {
        Map<QuestionCategory, QuestionParser> qParsers = getQuestionParsers();
        String fileName = getFileName(diff, category);
        String qPath = getNewFilePathForLang(diff, fileName, Language.en);
        List<String> linesFromFile = readFileContents(qPath);

        int i = 0;
        String res = "";
        for (String line : linesFromFile) {
            if (i == lineNr) {
                res = line;
                break;
            }
            i++;
        }

        if (StringUtils.isBlank(res)) {
            throw new RuntimeException("no question found for " + diff + " " + category + " line " + lineNr);
        }

        QuestionParser questionParser = qParsers.get(category);
        if (returnAnswerValue) {
            return questionParser.getAnswer(res);
        } else {
            return questionParser.getQuestion(res);
        }
    }

    private static void translateQuestion(Language langTranslateTo,
                                          QuestionDifficulty diff,
                                          String specificLangPathDirectory,
                                          QuizQuestionCategoryEnum category) throws IOException {

        Map<QuestionCategory, QuestionParser> qParsers = getQuestionParsers();
        String fileName = getFileName(diff, category);
        String qPath = getOldFilePathForLang(diff, fileName, Language.en);
        Path path = Paths.get(qPath);

        boolean exists = Files.exists(path);

        if (exists) {

            QuestionParser questionParser = qParsers.get(category);
            List<String> linesFromFile = readFileContents(qPath);

            List<String> translatedFiles = new ArrayList<>();
            for (String line : linesFromFile) {
                try {
                    String question = translateWord(langTranslateTo, questionParser.getQuestion(line));
                    String answer = translateWord(langTranslateTo, questionParser.getAnswer(line));
                    List<String> options = questionParser.getOptions(line).stream()
                            .map(e -> {
                                try {
                                    return translateWord(langTranslateTo, e);
                                } catch (IOException ex) {
                                    throw new RuntimeException();
                                }
                            }).collect(Collectors.toList());
                    String prefix = translateWord(langTranslateTo, questionParser.getQuestionPrefix(line));

                    translatedFiles.add(questionParser.formQuestion(langTranslateTo, question, answer, options, prefix));
                } catch (Exception ex) {
                    System.out.println(line);
                    throw ex;
                }
            }

            String returnValue = String.join("\n", translatedFiles);

            File myObj = new File(specificLangPathDirectory + "/" + fileName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(returnValue);
            myWriter.close();
        }
    }

    private static String getOldFilePathForLang(QuestionDifficulty diff, String fileName, Language language) {
        return ROOT_PATH_OLD + language.toString() + "/diff" + diff.getIndex() + "/" + fileName;
    }

    private static String getNewFilePathForLang(QuestionDifficulty diff, String fileName, Language language) {
        return ROOT_PATH_NEW + language.toString() + "/diff" + diff.getIndex() + "/" + fileName;
    }

    private static String getFileName(QuestionDifficulty diff, QuizQuestionCategoryEnum category) {
        return "questions_diff" + diff.getIndex() +
                "_cat" + category.getIndex() + ".txt";
    }

    private static Map<QuestionCategory, QuestionParser> getQuestionParsers() {
        Map<QuestionCategory, QuestionParser> qParsers = new HashMap<>();
        qParsers.put(QuizQuestionCategoryEnum.cat0, new DependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat1, new DependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat2, new DependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat3, new DependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat4, new DependentQuestionParser());
        return qParsers;
    }

    private static Map<QuestionCategory, QuestionParser> getOldQuestionParsers() {
        Map<QuestionCategory, QuestionParser> qParsers = new HashMap<>();
        qParsers.put(QuizQuestionCategoryEnum.cat0, new OldDependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat1, new OldDependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat2, new OldDependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat3, new OldDependentQuestionParser());
        qParsers.put(QuizQuestionCategoryEnum.cat4, new OldDependentQuestionParser());
        return qParsers;
    }

    private static Map<QuestionCategory, String> getPrefixForMovedQuestions() {
        Map<QuestionCategory, String> prefixes = new HashMap<>();
        prefixes.put(QuizQuestionCategoryEnum.cat0, "0");
        prefixes.put(QuizQuestionCategoryEnum.cat1, "1");
        prefixes.put(QuizQuestionCategoryEnum.cat2, "2");
        prefixes.put(QuizQuestionCategoryEnum.cat3, "3");
        prefixes.put(QuizQuestionCategoryEnum.cat4, "4");
        return prefixes;
    }

    private static String translateWord(Language translateTo, String text) throws IOException {
        String res = translateCountry(translateTo, text);
        if (res == null) {
            if (Arrays.stream(text.split(","))
                    .allMatch(e -> NumberUtils.isDigits(e.replace("-", "").trim()) || e.trim().equals("x"))) {
                res = text;
                System.out.println("is numbers " + res);
            } else if (NumberUtils.isDigits(text.replace("-", ""))) {
                res = text;
                System.out.println("is number " + res);
            } else if (StringUtils.isBlank(text)) {
                res = text;
            } else if (text.equals("x")) {
                res = text;
            } else {
                nrOfTranslations++;
                res = text;
//                res = TranslateTool.translate(Language.en.toString(), translateTo.toString(), text).trim();
                System.out.println("translated: " + text + " ___to___ " + res + " ----- as nr " + nrOfTranslations);
            }
        } else {
            System.out.println("translated country " + text + " to " + res);
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
        List<String> englishCountries = getCountries(Language.en);
        List<String> translatedCountries = getCountries(translateTo);

        countryName = countryName.replace("\u200E", "");
        if (englishCountries.contains(countryName)) {
            return translatedCountries.get(englishCountries.indexOf(countryName));
        }

        return null;
    }

    private static List<String> getCountries(Language translateTo) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/countries.txt";
        return readFileContents(String.format(rootPath, translateTo.toString()));
    }

    abstract static class QuestionParser {

        abstract String getAnswer(String rawString);

        abstract List<String> getOptions(String rawString);

        abstract String getQuestion(String rawString);

        abstract String getQuestionPrefix(String rawString);

        abstract String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix);

        String formQuestion(String format, Object[] params, Language language) {
            List<String> split = Arrays.stream(format.split("%s")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            String res = "";
            for (int i = 0; i < params.length; i++) {
                Object text = params[i];

                String rtlSeparator = "";
                if (RTL_LANGS.contains(language)) {
                    rtlSeparator = "\u200e";
                }
                if (detectRTL(text)) {
                    res = rtlSeparator + res + rtlSeparator + text + rtlSeparator;
                } else {
                    res = rtlSeparator + res + rtlSeparator + text + rtlSeparator;
                }
                if (i < split.size()) {
                    res = rtlSeparator + res + rtlSeparator + split.get(i) + rtlSeparator;
                }
            }
            return res;
        }

        public boolean detectRTL(Object s) {
            char[] chars = s.toString().toCharArray();
            for (char c : chars) {
                if (c >= 0x600 && c <= 0x6ff) {
                    return true;
                }
            }
            return false;
        }
    }

    static class DependentQuestionParser extends QuestionParser {

        @Override
        String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix) {
            String format = "%s:%s:%s:%s";
            Object[] array = {question, correctAnswer, String.join(",", options), prefix};
            return formQuestion(format, array, language);
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
        String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix) {
            String format = "%s::%s::%s";
            Object[] array = {question, String.join("##", options), correctAnswer};
            return formQuestion(format, array, language);
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

    static class TimelineQuestionParser extends QuestionParser {

        @Override
        String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix) {
            String format = "%s:%s";
            Object[] array = {question, correctAnswer};
            return formQuestion(format, array, language);
        }

        @Override
        String getAnswer(String rawString) {
            return rawString.split(":", -1)[1];
        }

        @Override
        List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[1].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        String getQuestion(String rawString) {
            return rawString.split(":", -1)[0];
        }

        @Override
        String getQuestionPrefix(String rawString) {
            return "";
        }
    }

    static class OldDependentQuestionParser extends QuestionParser {

        @Override
        String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix) {
            return "";
        }

        @Override
        String getAnswer(String rawString) {
            return rawString.split(":", -1)[2];
        }

        @Override
        List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[3].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        String getQuestion(String rawString) {
            return rawString.split(":", -1)[1];
        }

        @Override
        String getQuestionPrefix(String rawString) {
            return "";
        }
    }
}
