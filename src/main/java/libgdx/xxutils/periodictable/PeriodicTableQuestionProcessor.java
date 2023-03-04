package libgdx.xxutils.periodictable;

import org.apache.commons.lang3.tuple.MutablePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.campaign.QuestionCategory;
import libgdx.constants.Language;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.xxutils.TranslateQuestionProcessor;

public class PeriodicTableQuestionProcessor {

    public static final String ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/periodictable/";

    public static void main(String[] args) throws IOException {

        ///////
        ///////
        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
//        languages.remove(Language.en);
//        languages.remove(Language.ro);

//        List<Language> languages = Arrays.asList(
//                Language.ro
//        );

        ///////
        ///////
        ///////
        ///////
        ///////

        for (Language lang : languages) {
//            new File(ROOT_PATH + "temp/" + lang.toString()).mkdir();

            moveQuestionCat(lang);
        }
    }

    private static void moveQuestionCat(Language lang) throws IOException {

        List<String> elements = getElementTranslation(lang);

        String returnValue = String.join("\n", elements);

        System.out.println("writeee" + returnValue);
        String newFilePath = getTempQuestionPath(lang);
        System.out.println("newFilePath " + newFilePath);

        File myObj = new File(newFilePath);
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(returnValue);
        myWriter.close();
    }

    private static String translate(Language lang, String text) throws IOException {
        return TranslateQuestionProcessor.translateWord(lang, text);
    }

    public static List<String> getElementTranslation(Language language) {
        String qPath = getLibgdxLabelPath();
        BufferedReader reader;
        List<String> labels = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(qPath));
            String line = reader.readLine();
            int startindex = 1;
            while (line != null) {
                String prefix = language + "_periodictable_";
                if (line.startsWith(prefix)) {
                    int currentIndex = Integer.parseInt(line.split("=")[0].replace(prefix, ""));
                    if (currentIndex != startindex) {
                        throw new RuntimeException("wrong element index " + currentIndex);
                    }
                    String element = line.split("=")[1];
                    labels.add(element);
                    startindex++;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
        return labels;
    }

    private static Map<QuestionCategory, MutablePair<TranslateQuestionProcessor.QuestionParser, TranslateQuestionProcessor.QuestionParser>> getQuestionParsers() {
        Map<QuestionCategory, MutablePair<TranslateQuestionProcessor.QuestionParser, TranslateQuestionProcessor.QuestionParser>> qParsers = new HashMap<>();
        qParsers.put(JudeteleRomCategoryEnum.cat0, new MutablePair(new TranslateQuestionProcessor.OldImageClickQuestionParser(), new TranslateQuestionProcessor.ImageClickQuestionParser()));
        qParsers.put(JudeteleRomCategoryEnum.cat1, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(JudeteleRomCategoryEnum.cat2, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(JudeteleRomCategoryEnum.cat3, new MutablePair(new TranslateQuestionProcessor.OldDependentQuestionParser(), new TranslateQuestionProcessor.DependentQuestionParser()));
        qParsers.put(JudeteleRomCategoryEnum.cat4, new MutablePair(new TranslateQuestionProcessor.OldHangmanQuestionParser(), new TranslateQuestionProcessor.HangmanQuestionParser()));
        return qParsers;
    }

    private static String getLibgdxLabelPath() {
        return ROOT_PATH + "/labels/elements.properties";
    }

    private static String getTempQuestionPath(Language language) {
        return ROOT_PATH + "/temp/" + language + "_elements.txt";
    }

}