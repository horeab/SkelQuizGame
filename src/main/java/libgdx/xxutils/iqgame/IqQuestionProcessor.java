package libgdx.xxutils.iqgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import libgdx.constants.Language;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;

public class IqQuestionProcessor {


    public static void main(String[] args) throws IOException {

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages.remove(Language.en);
        languages.remove(Language.ro);

        List<String> questions = getQuestions(Language.en);
        TranslateQuestionProcessor.UniqueQuestionParser parser = new TranslateQuestionProcessor.UniqueQuestionParser();

        for (Language lang : languages) {
            List<String> resQ = new ArrayList<>();
            for (String q : questions) {
                String question = TranslateQuestionProcessor.translateWord(lang, parser.getQuestion(q));
                String answer = TranslateQuestionProcessor.translateWord(lang, parser.getAnswer(q));
                List<String> options = parser.getOptions(q).stream().map(
                        e -> {
                            try {
                                return TranslateQuestionProcessor.translateWord(lang, e);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        }).collect(Collectors.toList());
                resQ.add(parser.formQuestion(lang, question, answer, options, "", ""));
            }


            String returnValue = String.join("\n", resQ);

            System.out.println("writeee" + returnValue);
            String newFilePath = getLibgdxQuestionPath(lang.name());
            System.out.println("newFilePath " + newFilePath);

            File myObj = new File(newFilePath);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(returnValue);
            myWriter.close();
        }
    }

    private static List<String> getQuestions(Language lang) {
        String qPath = getLibgdxQuestionPath(lang.toString());
        BufferedReader reader;
        try {
            List<String> questionInfo = new ArrayList<>();
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            questionInfo.add(line);
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    questionInfo.add(line);
                }
            }

            reader.close();
            return questionInfo;

        } catch (IOException e) {
            return null;
        }
    }

    private static String getLibgdxQuestionPath(String lang) {
        return "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/iqtest/cat5/q_" + lang.toLowerCase() + ".txt";
    }

}
