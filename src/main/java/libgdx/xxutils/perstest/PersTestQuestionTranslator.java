package libgdx.xxutils.perstest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import libgdx.constants.Language;
import libgdx.xxutils.FlutterQuestionProcessor;
import libgdx.xxutils.TranslateQuestionProcessor;
import libgdx.xxutils.geoquiz.FlutterCountriesProcessor;

public class PersTestQuestionTranslator {


    public static void main(String[] args) throws IOException {

        //
        ////
//        List<Language> languages = Collections.singletonList(Language.ro);
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        languages.remove(Language.ar);
        languages.remove(Language.bg);
        languages.remove(Language.cs);
        languages.remove(Language.da);
        languages.remove(Language.de);
        languages.remove(Language.en);
        languages.remove(Language.ro);
        ////
        //

        for (Language language : languages) {
            String[] cats = {"cat0", "cat1", "cat2"};
            for (String cat : cats) {
                List<String> questions = FlutterCountriesProcessor.readFileContents(getEnQuestionPath(cat));
                List<String> translatedQuestions = new ArrayList<>();
                for (String q : questions) {
//                    translatedQuestions.add(TranslateQuestionProcessor.translateWord(language, q));
                    translatedQuestions.add(q);
                }
                writeToFile(String.join("\n", translatedQuestions), cat, language);
            }
        }
    }

    public static void writeToFile(String qStrings, String cat, Language language) throws IOException {

        File myObj = new File(FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/perstest/questions/" + cat + "/" + language.toString() + "_questions.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(qStrings);
        myWriter.close();
    }

    public static String getEnQuestionPath(String cat) {
        return FlutterQuestionProcessor.ROOT_FOLDER + "/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/perstest/questions/" + cat + "/en_questions.txt";
    }
}
