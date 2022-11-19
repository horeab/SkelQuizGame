package libgdx.xxutils.hangman;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.game.GameId;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangmanarena.HangmanArenaQuestionDifficultyLevel;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.LabelProcessor;

public class HangmanFlutterQuestionProcessor {


    public static void main(String[] args) throws IOException {

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages.remove(Language.ar);
        languages.remove(Language.he);
        languages.remove(Language.hi);
        languages.remove(Language.ja);
        languages.remove(Language.ko);
        languages.remove(Language.th);
        languages.remove(Language.vi);
        languages.remove(Language.zh);

//        languages = new ArrayList<>(Arrays.asList(Language.en));

        StringBuilder res = new StringBuilder();

        for (Language language : languages) {
            res.append("add" + language.name().toUpperCase() + "(result, questionConfig);\n");
        }
        res.append("    return result;\n" +
                "  }\n\n");


        List<HangmanArenaQuestionDifficultyLevel> diffs = new ArrayList<>(Arrays.asList(HangmanArenaQuestionDifficultyLevel.values()));
        for (Language language : languages) {
            List<String> commQ = new ArrayList<>();

            res.append(FlutterQuestionProcessor.getQuestionsHeader(language));

            for (HangmanArenaQuestionDifficultyLevel diff : diffs) {
                for (HangmanQuestionCategoryEnum cat : Arrays.asList(
                        HangmanQuestionCategoryEnum.cat0,
                        HangmanQuestionCategoryEnum.cat1,
                        HangmanQuestionCategoryEnum.cat2,
                        HangmanQuestionCategoryEnum.cat3,
                        HangmanQuestionCategoryEnum.cat4
                )) {
                    commQ.addAll(addQuestionCategory(cat, diff, language, res, commQ));
                }
            }

            Set<String> dupl = findDuplicates(commQ);
            if (!dupl.isEmpty()) {
                throw new RuntimeException(language.toString() + ":  " + dupl.toString());
            }

            res.append("  }\n\n");
        }

        System.out.println(res);
    }

    public static Set<String> findDuplicates(List<String> listContainingDuplicates) {
        final Set<String> setToReturn = new HashSet<>();
        final Set<String> set1 = new HashSet<>();

        for (String s : listContainingDuplicates) {
            if (!set1.add(s.toLowerCase())) {
                setToReturn.add(s.toLowerCase());
            }
        }
        return setToReturn;
    }

    private static List<String> addQuestionCategory(QuestionCategory flutterCat,
                                                    QuestionDifficulty diff,
                                                    Language language,
                                                    StringBuilder res,
                                                    List<String> commQ) {

        List<Language> ignLangs = Arrays.asList(Language.cs, Language.fr, Language.it, Language.nl);

        Map<Pair<String, String>, String> defaultLabels = LabelProcessor.getLabelsForLanguage(Collections.singletonList(GameIdEnum.hangmanarena),
                new HashMap<>(), language);
        List<String> hangmanarena_available_letters = Arrays.asList(defaultLabels.get(
                defaultLabels.keySet().stream().filter(e -> e.getLeft().equals("hangmanarena_available_letters"))
                        .findFirst().get()).split(","));
        List<String> allLettersLowerCase = hangmanarena_available_letters.stream().map(String::toLowerCase).collect(Collectors.toList());
        List<String> allLettersUpperCase = hangmanarena_available_letters.stream().map(String::toUpperCase).collect(Collectors.toList());

        List<String> questions = new ArrayList<>();
        if (StringUtils.isNotBlank(flutterCat.toString())) {
            String qPath = getLibgdxQuestionPath(language, flutterCat.toString(), diff);
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(qPath));
                String line = reader.readLine();

                while (line != null) {

                    boolean qContained = false;
                    for (String s : commQ) {
                        if (s.toLowerCase().trim().replace("\"", "").equals(line.toLowerCase().trim())) {
                            qContained = true;
                            break;
                        }
                    }

                    if (!qContained) {

                        String word = StringUtils.capitalize(line.trim());

                        for (int i = 0; i < word.length(); i++) {
                            String s = Character.toString(word.toCharArray()[i]);
                            if (!ignLangs.contains(language) && s.trim().length() > 0 && !s.equals("-") && !s.equals("'")) {
                                if (!allLettersLowerCase.contains(s.toLowerCase())) {
                                    throw new IllegalStateException(language + " " + word + " LOW Letter not contained in word " + s);
                                }
                                if (!allLettersUpperCase.contains(s.toUpperCase())) {
                                    throw new IllegalStateException(language + " " + word + " UPP Letter not contained in word " + s);
                                }
                            }
                        }

                        if (word.length() < 3) {
                            throw new IllegalStateException(language + " word too short " + word + " " + flutterCat + diff);
                        }

                        questions.add("\"" + word + "\"");

                    }

                    line = reader.readLine();
                }

                if (questions.size() < 5) {
                    throw new IllegalStateException(language + " not enough qs " + flutterCat + diff);
                }

                res.append(FlutterQuestionProcessor
                        .getQuestionsForCatAndDiff(diff, flutterCat.toString(), questions.toString()));

                reader.close();
            } catch (IOException e) {
                int i = 0;
            }
        }
        return questions;
    }

    private static String getLibgdxQuestionPath(Language language, String flutterCat, QuestionDifficulty diff) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/hangmanarena/questions/" + language.toString() + "/diff" + diff.getIndex() + "/"
                + "questions_diff" + diff.getIndex() + "_" + flutterCat + ".txt";
    }

}
