package libgdx.xxutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import libgdx.constants.Language;
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomyDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.xxutils.astronomy.AstronomyQuestionProcessor;

public class LabelProcessor {


    public static void main(String[] args) throws IOException {

        List<GameIdEnum> gameIds = Arrays.asList(
                GameIdEnum.anatomy,
                GameIdEnum.countries,
                GameIdEnum.dopewars,
                GameIdEnum.history,
                GameIdEnum.quizgame,
                GameIdEnum.perstest,
                GameIdEnum.iqtest,
                GameIdEnum.astronomy,
                GameIdEnum.hangmanarena,
                GameIdEnum.buylow,
                GameIdEnum.paintings,
                GameIdEnum.periodictable,
                GameIdEnum.math
        );

        Map<Pair<String, String>, String> defaultLabels = getLabelsForLanguage(gameIds, new HashMap<>(), Language.en);

        //
        ////
//        List<Language> languages = Collections.singletonList(Language.en);
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
//        List<Language> languages = Arrays.asList(Language.en, Language.ro,
//                Language.de, Language.es, Language.it);
        ////
        //

//        translateNewLanguage(Language.ro, gameIds, defaultLabels);

//        for (Language l : languages) {
//            translateMissingLabels(l, gameIds, defaultLabels);
//        }
        formFlutterKeys(gameIds, defaultLabels, languages);
    }

    private static void translateMissingLabels(Language translateTo, List<GameIdEnum> gameIds, Map<Pair<String, String>, String> enLabels) {
        Map<String, String> newM = new HashMap<>();
        Map<Pair<String, String>, String> labelsForLanguage = getLabelsForLanguage(gameIds, enLabels, translateTo);
        Map<String, String> missingToTranslate = new HashMap<>();
        Map<Pair<String, String>, String> missingKeys = getMissingLabelKeys(labelsForLanguage, gameIds);

        //HARDCODE TRANSLATION
//        missingKeys.put(new MutablePair<>("score_iq", "l_score"), "Score");

        enLabels.forEach((key, value) -> {
            if (missingKeys.entrySet().stream().anyMatch(e -> e.getKey().getRight().equals(key.getRight()))) {
                missingToTranslate.put(key.getLeft(), value);
            }
        });

        String defaultLang = Language.en.toString();
        missingToTranslate.forEach((key, value) -> {
            System.out.println("Translating: " + key + "=" + value);
            String trans = null;
            try {
                trans = TranslateTool.translate(defaultLang, translateTo.toString(), value).trim();
//                trans = value;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERRROROOROROORR: " + key);
            }
            System.out.println("Translated: " + key + "=" + trans);
            newM.put(key, trans);
        });
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        newM.forEach((key, value) -> System.out.println(translateTo.toString() + "_" + key + "=" + value));
    }

    static boolean isHangmanSupported(Language language) {

        return !Arrays.asList(
                Language.ar,
                Language.he,
                Language.hi,
                Language.ja,
                Language.ko,
                Language.th,
                Language.zh,
                Language.vi).contains(language);
    }

    private static void formFlutterKeys(List<GameIdEnum> gameIds, Map<Pair<String, String>, String> defaultLabels, List<Language> languages) throws IOException {
        for (Language translateTo : languages) {


            Map<Pair<String, String>, String> labelsForLanguage = getLabelsForLanguage(gameIds, defaultLabels, translateTo);
            labelsForLanguage.putAll(getImplementationExtraLabels(translateTo));
            defaultLabels.putAll(getImplementationExtraLabels(translateTo));

            Map<Pair<String, String>, String> missingKeys = getMissingLabelKeys(labelsForLanguage, gameIds);

            if (!missingKeys.isEmpty()) {
                System.out.println();
                missingKeys.forEach((key, value) -> System.out.println(value));
                missingKeys.forEach((key, value) -> System.out.println(key.getLeft() + "=" + value));
                System.out.println();
                throw new RuntimeException("missing keys for lang " + translateTo);
            }

            Map<String, String> labelsWithKeys = new TreeMap<>();
            for (Map.Entry<Pair<String, String>, String> e : labelsForLanguage.entrySet()) {
                if (e == null || e.getKey() == null || e.getKey().getRight() == null) {
                    continue;
                }
                labelsWithKeys.put(e.getKey().getRight(), e.getValue());
            }

            Set<String> defaultLabelsKey = defaultLabels.keySet().stream().map(Pair::getRight).collect(Collectors.toSet());
            if (labelsWithKeys.keySet().size() != defaultLabelsKey.size()) {
                Set<String> diffToDisplay = new HashSet<>();
                Set<String> differences1 = new HashSet<>(labelsWithKeys.keySet());
                differences1.removeAll(defaultLabelsKey);
                Set<String> differences2 = new HashSet<>(defaultLabelsKey);
                differences2.removeAll(labelsWithKeys.keySet());

                diffToDisplay.addAll(differences1);
                diffToDisplay.addAll(differences2);
                if (!diffToDisplay.isEmpty()) {
                    throw new RuntimeException("missing keys " + diffToDisplay.toString() + " for " + translateTo);
                }
            }

            System.out.println(new Gson().toJson(labelsWithKeys));
            writeToFile(new Gson().toJson(labelsWithKeys), translateTo);
        }
    }

    private static Map<Pair<String, String>, String> getImplementationExtraLabels(Language lang) {
        List<String> planetsEnQuestions = AstronomyQuestionProcessor.getEnglishQuestions(AstronomyCategoryEnum.cat0, AstronomyDifficultyLevel._0);
        List<String> planetsLangQuestions = AstronomyQuestionProcessor.getQuestions(AstronomyCategoryEnum.cat0, AstronomyDifficultyLevel._0, lang);
        Map<Pair<String, String>, String> planets = new HashMap<>();
        int i = 0;
        TranslateQuestionProcessor.ImageClickQuestionParser imageClickQuestionParser = new TranslateQuestionProcessor.ImageClickQuestionParser();
        for (String pl : planetsEnQuestions) {
            String planetEn = imageClickQuestionParser.getQuestion(pl);
            planets.put(Pair.of(planetEn.toLowerCase().replace(" ", "_"),
                    "l_" + planetEn.toLowerCase().replace(" ", "_")), imageClickQuestionParser.getQuestion(planetsLangQuestions.get(i)));
            i++;
        }
        return planets;
    }

    public static void translateNewLanguage(Language translateTo, List<GameIdEnum> gameIds, Map<Pair<String, String>, String> enLabels) {
        Map<String, String> newM = new HashMap<>();

        enLabels.forEach((key, value) -> {
            System.out.println("Translating: " + key.getLeft() + "=" + value);
            String trans = null;
            try {
                trans = TranslateTool.translate(Language.en.toString(), translateTo.toString(), value).trim();
//                trans = value;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERRROROOROROORR: " + key.getLeft());
            }
            System.out.println("Translated: " + key.getLeft() + "=" + trans);
            newM.put(key.getLeft(), trans);
        });
        newM.forEach((key, value) -> System.out.println(key + "=" + value));
    }


    private static Map<Pair<String, String>, String> getMissingLabelKeys(Map<Pair<String, String>, String> labels, List<GameIdEnum> gameIds) {
        Map<Pair<String, String>, String> defaultLabels = getLabelsForLanguage(gameIds, new HashMap<>(), Language.en);
        List<String> missingKeys = defaultLabels.keySet().stream().map(Pair::getRight)
                .collect(Collectors.toList());
        missingKeys.removeAll(
                labels.keySet().stream().map(Pair::getRight).collect(Collectors.toList()));
        missingKeys.removeAll(
                labels.keySet().stream().map(Pair::getLeft).collect(Collectors.toList()));
        Map<Pair<String, String>, String> missingDefaultLabels = new HashMap<>();
        for (String k : missingKeys) {
            for (Map.Entry<Pair<String, String>, String> e : defaultLabels.entrySet()) {
                if (e.getKey().getRight().equals(k)) {
                    missingDefaultLabels.put(e.getKey(), e.getValue());
                    break;
                }
            }
        }
        return missingDefaultLabels;
    }

    public static void writeToFile(String json, Language language) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonString = gson.toJson(je);

        File myObj = new File("/Users/macbook/IdeaProjects/SkelQuizGame/src/main/java/libgdx/xxutils/labels/app_" + language + ".arb");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(prettyJsonString);
        myWriter.close();
    }

    public static Map<Pair<String, String>, String> getLabelsForLanguage(List<GameIdEnum> gameIds, Map<Pair<String, String>, String> defaultLabels, Language lang) {
        String mainPath = getMainLabelsPath(lang);
        String campPath = getMainGameLabelsPath(lang);

        Map<Pair<String, String>, String> labels = new TreeMap<>();

        addLabels(mainPath, null, labels);
        addLabels(campPath, null, labels);

        for (GameIdEnum gid : gameIds) {
            String gamePath = getGameImplementationLabelsPath(gid);
            addLabels(gamePath, lang, labels);
        }

        Map<Pair<String, String>, String> labelsWithOrigKeys = new TreeMap<>();
        if (!defaultLabels.isEmpty()) {
            for (Map.Entry<Pair<String, String>, String> e : labels.entrySet()) {
                labelsWithOrigKeys.put(new MutablePair<>(e.getKey().getLeft(), getDefaultLanguageKey(defaultLabels, e.getKey().getLeft())), e.getValue());
            }
        }

        return defaultLabels.isEmpty() ? labels : labelsWithOrigKeys;
    }

    private static String getDefaultLanguageKey(Map<Pair<String, String>, String> defaultLabels, String key) {

        for (Map.Entry<Pair<String, String>, String> e : defaultLabels.entrySet()) {
            if (e.getKey().getLeft().equals(key)) {
                return e.getKey().getRight();
            }
        }

        return null;
    }

    private static void addLabels(String path, Language lang, Map<Pair<String, String>, String> labels) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();


            while (line != null) {

                List<String> ignoreKeys = Arrays.asList("font_name", "privacy_policy", "facebook_share_btn");
                List<String> onlyKeys = new ArrayList<>();

                if (lang != null) {
                    onlyKeys.add(lang + "_");
                }

                boolean ignoreBool = false;
                for (String ignore : ignoreKeys) {
                    if (line.startsWith(ignore)) {
                        ignoreBool = true;
                        break;
                    }
                }

                for (String only : onlyKeys) {
                    ignoreBool = true;
                    if (line.startsWith(only)) {
                        ignoreBool = false;
                        break;
                    }
                }

                if (ignoreBool) {
                    line = reader.readLine();
                    continue;
                }

                if (line.contains("=")) {
                    String[] split = line.split("=");
                    String value = split[1];

                    value = value.replace("Highscore", "High Score");
                    value = value.replace("\u0027", "'");
                    value = value.replace("\\n", "\n");
                    value = value.replace(" ", " ");

                    String key = value.toLowerCase();

                    Map<String, String> replaceStrings = new HashMap<>();
                    replaceStrings.put(" ", "_");
                    replaceStrings.put("\n", "_");
                    replaceStrings.put(":", "_");
                    replaceStrings.put("-", "_");
                    replaceStrings.put("/", "_");
                    replaceStrings.put("\\", "_");
                    replaceStrings.put(",", "_");
                    replaceStrings.put("'", "");
                    replaceStrings.put("!", "");
                    replaceStrings.put("+", "");
                    replaceStrings.put("?", "");
                    replaceStrings.put(".", "");
                    replaceStrings.put("%", "");
                    replaceStrings.put("(", "");
                    replaceStrings.put(")", "");
                    replaceStrings.put("{0}", "param0");
                    replaceStrings.put("{1}", "param1");
                    replaceStrings.put("{2}", "param2");

                    for (Map.Entry<String, String> r : replaceStrings.entrySet()) {
                        key = key.replace(r.getKey(), r.getValue());
                    }
                    key = "l_" + key;
                    key = key.replace("__", "_");
                    key = key.replace("__", "_");
                    key = key.replace("__", "_");
                    key = key.replace("__", "_");

                    int maxKeyLength = 100;
                    if (key.length() > maxKeyLength) {
                        key = key.substring(0, maxKeyLength);
                    }

                    labels.put(new MutablePair<>(lang != null ? split[0].substring(split[0].indexOf("_") + 1).trim() : split[0].trim(), key.trim()), value);

                }

                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public static String getGameImplementationLabelsPath(GameIdEnum gid) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + gid.name() + "/labels/labels.properties";
    }

    private static String getMainGameLabelsPath(Language lang) {
        return "/Users/macbook/IdeaProjects/SkelGame/src/main/resources/tournament_resources/main/labels/labels_" + lang + ".properties";
    }

    private static String getMainLabelsPath(Language lang) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/main_resources/main/labels/main_labels_" + lang + ".properties";
    }
}
