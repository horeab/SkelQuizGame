package libgdx.xxutils;

import libgdx.xxutils.kidlearn.KidLearnQuestionDifficultyLevel;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.astronomy.AstronomyDifficultyLevel;
import libgdx.implementations.geoquiz.QuizQuestionCategoryEnum;
import libgdx.implementations.geoquiz.QuizQuestionDifficultyLevel;

public class TranslateQuestionProcessor {

    static final List<Language> RTL_LANGS = Arrays.asList(Language.ar, Language.he);

    static final String GAME_ID = "kidlearn";
    static final String ROOT_PATH_OLD = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/questions/";
    static final String ROOT_PATH_NEW = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + GAME_ID + "/questions/";

    static int nrOfTranslations = 0;

    public static void main(String[] args) throws IOException {

//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.ro);
//        languages.remove(Language.en);
        List<Language> languages = Arrays.asList(Language.en, Language.ro);

        for (Language lang : languages) {

            List<QuestionDifficulty> difficulties = Arrays.asList(KidLearnQuestionDifficultyLevel.values());
            for (QuestionDifficulty diff : difficulties) {

                String newQuestionPath = getNewFilePathForLang(diff, lang);

                if (new File(newQuestionPath).exists()) {
                    deleteTemp(newQuestionPath);
                }
                new File(newQuestionPath).mkdirs();

                for (QuizQuestionCategoryEnum category : QuizQuestionCategoryEnum.values()) {

                    moveQuestionToNewFolderAndFormat(lang, diff, category);
//                    translateQuestion(lang, diff, specificLangPathDirectory, category);
                }
            }
        }
    }

    public static void moveQuestionToNewFolderAndFormat(Language langToMove,
                                                        QuestionDifficulty diff,
                                                        QuizQuestionCategoryEnum category) throws IOException {

        if (category != QuizQuestionCategoryEnum.cat0 && category != QuizQuestionCategoryEnum.cat4) {
            return;
        }

        Map<QuestionCategory, QuestionParser> qParsers = getQuestionParsers();
        Map<QuestionCategory, QuestionParser> oldQParsers = getOldQuestionParsers();
        String fileName = getFileName(diff, category);
        String oldQuestionPath = getOldFilePathForLang(diff, fileName, langToMove);
        Path path = Paths.get(oldQuestionPath);
        Map<QuestionCategory, String> prefixForMovedQuestions = getPrefixForMovedQuestions();

        String newQuestionPath = getNewFilePathForLang(diff, langToMove);

        boolean exists = Files.exists(path);

        if (exists) {

            QuestionParser newQuestionParser = qParsers.get(category);
            QuestionParser oldQuestionParser = oldQParsers.get(category);
            List<String> linesFromFile = readFileContents(oldQuestionPath);

            List<String> movedFile = new ArrayList<>();
            List<Language> notTranslatedQLangs = Arrays.asList(Language.ar, Language.bg, Language.he,
                    Language.sl, Language.sr);
            for (String line : linesFromFile) {
                try {
                    String question = oldQuestionParser.getQuestion(line);
                    String answer = oldQuestionParser.getAnswer(line);
                    if (notTranslatedQLangs.contains(langToMove)) {
                        System.out.println("translating " + langToMove.toString() + " " + answer);
                        answer = translateWord(langToMove, answer);
                    }
                    //

                    List<String> options = new ArrayList<>();
                    if (category == QuizQuestionCategoryEnum.cat4) {
                        String qNr = line.split(":", -1)[0];
                        if (diff.name().equals(QuizQuestionDifficultyLevel._1.name())
                                && Arrays.asList("12", "15").contains(qNr)
                                ||
                                diff.name().equals(QuizQuestionDifficultyLevel._2.name())
                                        && Arrays.asList("26", "27", "28").contains(qNr)
                                ||
                                diff.name().equals(QuizQuestionDifficultyLevel._3.name())
                                        && Arrays.asList("29", "32", "33").contains(qNr)) {
                            options = oldQuestionParser.getOptions(line);
                        }
                    }
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

                    movedFile.add(newQuestionParser.formQuestion(langToMove, question, answer, options, "", ""));
                } catch (Exception ex) {
                    System.out.println(line);
                    throw ex;
                }
            }

            String returnValue = String.join("\n", movedFile);

            File myObj = new File(newQuestionPath + "/" + fileName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(returnValue);
            myWriter.close();
        }
    }

    private static void deleteTemp(String pathname) {
        File folder = new File(pathname);
        Arrays.stream(Objects.requireNonNull(folder.listFiles())).forEach(File::delete);
        folder.delete();
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

                    translatedFiles.add(questionParser.formQuestion(langTranslateTo, question, answer, options, prefix, ""));
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

    private static String getNewFilePathForLang(QuestionDifficulty diff, Language language) {
        return ROOT_PATH_NEW + language.toString() + "/temp/diff" + diff.getIndex();
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

    public static String translateWord(Language translateTo, String text) throws IOException {
        String res = null;
        if (text != null) {
            res = translateCountry(translateTo, text);
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
//                    res = text;
                    res = translateTo == Language.en ? text :
                            TranslateTool.translate(Language.en.toString(), translateTo.toString(), text).trim();
                    System.out.println("translated: " + text + " ___to___ " + res + " ----- as nr " + nrOfTranslations);
                }
            } else {
                System.out.println("translated country " + text + " to " + res);
            }
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
        String rootPath = FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s/countries.txt";
        List<String> countries = readFileContents(String.format(rootPath, translateTo.toString()));
        return countries.stream()
                .map(e -> e.split(":")[0])
                .map(e -> e.split("----")[0])
                .collect(Collectors.toList());
    }

    public abstract static class QuestionParser {

        public abstract String getAnswer(String rawString);

        public abstract List<String> getOptions(String rawString);

        public abstract String getQuestion(String rawString);

        public abstract String getQuestionPrefix(String rawString);

        public abstract String getQuestionExplanation(String rawString);

        public abstract String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation);

        public static String formQuestion(String format, Object[] params, Language language) {
            List<String> split = Arrays.stream(format.split("%s")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            String res = "";
            for (int i = 0; i < params.length; i++) {
                Object text = params[i];

                String rtlSeparator = "";
                if (RTL_LANGS.contains(language)) {
                    rtlSeparator = "\u200e";
                }
                res = rtlSeparator + res + rtlSeparator + text + rtlSeparator;
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

    public static class DependentQuestionParser extends QuestionParser {

        private static String delim = "::";

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s" + delim + "%s" + delim + "%s" + delim + "%s" + delim + "%s";
            Object[] array = {question, correctAnswer, String.join(",", options), prefix, explanation};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(delim, -1)[1];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(delim, -1)[2].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        public String getQuestion(String rawString) {
            return rawString.split(delim, -1)[0];
        }

        @Override
        public String getQuestionPrefix(String rawString) {
            return rawString.split(delim, -1)[3];
//            return "";
        }

        @Override
        public String getQuestionExplanation(String rawString) {
            return rawString.split(delim, -1)[4];
        }
    }

    public static class UniqueQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s::%s::%s";
            Object[] array = {question, String.join("##", options), correctAnswer};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split("::", -1)[2];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split("::", -1)[1].split("##", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        public String getQuestion(String rawString) {
            return rawString.split("::", -1)[0];
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


    public static class OldUniqueQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s:%s";
            Object[] array = {question, String.join("##", options), correctAnswer};
            return formQuestion(format, array, language);
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

        @Override
        public String getQuestionExplanation(String rawString) {
            return "";
        }
    }

    static class TimelineQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s";
            Object[] array = {question, correctAnswer};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[1];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[1].split(",", -1))
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

    public static class OldDependentQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            return "";
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[2];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[3].split(",", -1))
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

        @Override
        public String getQuestionExplanation(String rawString) {
            String[] split = rawString.split(":", -1);
            return split.length >= 6 ? rawString.substring(StringUtils.ordinalIndexOf(rawString, ":", 5) + 1) : "";
        }
    }

    public static class ImageClickQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s:%s";
            Object[] array = {question, String.join(",", options), prefix};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[0];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[3].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        public String getQuestion(String rawString) {
            return rawString.split(":", -1)[2];
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

    public static class OldImageClickQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            return "";
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[0];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Arrays.stream(rawString.split(":", -1)[3].split(",", -1))
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }

        @Override
        public String getQuestion(String rawString) {
            return rawString.split(":", -1)[2];
        }

        @Override
        public String getQuestionPrefix(String rawString) {
            return rawString.split(":", -1)[4];
        }

        @Override
        public String getQuestionExplanation(String rawString) {
            return "";
        }
    }


    public static class HangmanQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            String format = "%s:%s";
            Object[] array = {question, correctAnswer};
            return formQuestion(format, array, language);
        }

        @Override
        public String getAnswer(String rawString) {
            return "";
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Collections.emptyList();
        }

        @Override
        public String getQuestion(String rawString) {
            return "";
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

    public static class OldHangmanQuestionParser extends QuestionParser {

        @Override
        public String formQuestion(Language language, String question, String correctAnswer, List<String> options, String prefix, String explanation) {
            return "";
        }

        @Override
        public String getAnswer(String rawString) {
            return rawString.split(":", -1)[2];
        }

        @Override
        public List<String> getOptions(String rawString) {
            return Collections.emptyList();
        }

        @Override
        public String getQuestion(String rawString) {
            return rawString.split(":", -1)[1];
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
