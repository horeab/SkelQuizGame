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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import libgdx.constants.Language;
import libgdx.implementations.skelgame.GameIdEnum;

public class LabelProcessor {


    public static void main(String[] args) throws IOException {

        List<GameIdEnum> gameIds = Arrays.asList(GameIdEnum.history, GameIdEnum.quizgame, GameIdEnum.countries, GameIdEnum.perstest);

        Map<Pair<String, String>, String> defaultLabels = getLabelsForLanguage(gameIds, new HashMap<>(), Language.en);

        //
        ////
//        List<Language> languages = Collections.singletonList(Language.en);
//        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        List<Language> languages = Arrays.asList(Language.en, Language.ro);
        ////
        //

//        translateNewLanguage(Language.ro, gameIds, defaultLabels);
        translateMissingLabels(Language.zh, gameIds, defaultLabels);
//        formFlutterKeys(gameIds, defaultLabels, languages);
    }


    private static void translateMissingLabels(Language translateTo, List<GameIdEnum> gameIds, Map<Pair<String, String>, String> enLabels) {
        Map<String, String> newM = new HashMap<>();
        Map<Pair<String, String>, String> labelsForLanguage = getLabelsForLanguage(gameIds, enLabels, translateTo);
        Map<String, String> missingToTranslate = new HashMap<>();
        Map<Pair<String, String>, String> missingKeys = getMissingLabelKeys(labelsForLanguage, gameIds);

        missingKeys.entrySet().removeIf(e -> e.getKey().getRight().equals("l_a_b_c_d_e_f_g_h_i_j_k_l_m_n_o_p_q_r_s_t_u_v_w_x_y_z"));
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

            Map<Pair<String, String>, String> missingKeys = getMissingLabelKeys(labelsForLanguage, gameIds);
            if (!isHangmanSupported(translateTo)) {
                missingKeys.entrySet().removeIf(e -> e.getKey().getRight().equals("l_a_b_c_d_e_f_g_h_i_j_k_l_m_n_o_p_q_r_s_t_u_v_w_x_y_z"));
            }

            if (!missingKeys.isEmpty()) {
                System.out.println();
                missingKeys.forEach((key, value) -> System.out.println(value));
                missingKeys.forEach((key, value) -> System.out.println(key.getLeft()+"="+value));
                System.out.println();
                throw new RuntimeException("missing keys for lang " + translateTo);
            }

            Map<String, String> labelsWithKeys = new TreeMap<>();
            for (Map.Entry<Pair<String, String>, String> e : labelsForLanguage.entrySet()) {
                labelsWithKeys.put(e.getKey().getRight(), e.getValue());
            }
            System.out.println(new Gson().toJson(labelsWithKeys));
            writeToFile(new Gson().toJson(labelsWithKeys), translateTo);
        }
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
