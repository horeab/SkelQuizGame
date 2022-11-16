package libgdx.xxutils.hangman;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.constants.Language;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangmanarena.HangmanArenaQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;

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
                        questions.add("\"" + StringUtils.capitalize(line.trim()) + "\"");
                    }

                    line = reader.readLine();
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
