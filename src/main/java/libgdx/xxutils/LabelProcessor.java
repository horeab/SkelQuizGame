package libgdx.xxutils;

import com.google.gson.Gson;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import libgdx.constants.Language;
import libgdx.implementations.skelgame.GameIdEnum;

class LabelProcessor {

    public static void main(String[] args) {

        List<GameIdEnum> gameIds = Arrays.asList(GameIdEnum.history);

        Map<Pair<String, String>, String> defaultLabels = getLabelsForLanguage(gameIds, new HashMap<>(), Language.en);

        Map<Pair<String, String>, String> labels = getLabelsForLanguage(gameIds, defaultLabels, Language.en);

        if (labels.size() != defaultLabels.size()) {
            throw new RuntimeException("missing keys");
        }

        Map<String, String> labelsWithKeys = new TreeMap<>();
        for (Map.Entry<Pair<String, String>, String> e : labels.entrySet()) {
            labelsWithKeys.put(e.getKey().getRight(), e.getValue());
        }
        System.out.println(new Gson().toJson(labelsWithKeys));
    }

    private static Map<Pair<String, String>, String> getLabelsForLanguage(List<GameIdEnum> gameIds, Map<Pair<String, String>, String> defaultLabels, Language lang) {
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

        throw new RuntimeException("no key found " + key);
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

                    String key = value.toLowerCase();

                    Map<String, String> replaceStrings = new HashMap<>();
                    replaceStrings.put(" ", "_");
                    replaceStrings.put("\n", "_");
                    replaceStrings.put(":", "_");
                    replaceStrings.put("-", "_");
                    replaceStrings.put("'", "");
                    replaceStrings.put("!", "");
                    replaceStrings.put("+", "");
                    replaceStrings.put("?", "");
                    replaceStrings.put(".", "");
                    replaceStrings.put("%", "");
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

                    labels.put(new MutablePair<>(lang != null ? split[0].substring(split[0].indexOf("_") + 1).trim() : split[0].trim(), key.trim()), value);

                }

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getGameImplementationLabelsPath(GameIdEnum gid) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/" + gid.name() + "/labels/labels.properties";
    }

    private static String getMainGameLabelsPath(Language lang) {
        return "/Users/macbook/IdeaProjects/SkelGame/src/main/resources/tournament_resources/main/labels/labels_" + lang + ".properties";
    }

    private static String getMainLabelsPath(Language lang) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/main_resources/main/labels/main_labels_" + lang + ".properties";
    }
}
