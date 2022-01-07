package libgdx.xxutils.geoquiz;

import org.apache.commons.lang3.mutable.MutableInt;

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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import libgdx.constants.Language;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.xxutils.LabelProcessor;

public class CountriesMapper {

    public static void main(String[] args) throws IOException {

        Map<IndexMapping, String> mappings = createMappings();

//        List<Language> langs = Arrays.asList(Language.values());
//        langs.remove(Language.en);

        List<Language> langs = Arrays.asList(Language.ro);

        for (Language lang : langs) {
//            createNewCountriesFile(mappings.keySet(), lang);
//            moveCapitalsToNewPos(mappings.keySet(), lang);
            moveQuestions(mappings.keySet(), lang);
//            moveSynonymsToNewPos(mappings.keySet(), lang);
        }
    }

    private static void createNewCountriesFile(Set<IndexMapping> mappings, Language language) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s";

        List<String> oldCountries = FlutterCountriesProcessor.getCountries(language);
        List<String> newCountries = FlutterCountriesProcessor.getCountries(language);

        MutableInt i = new MutableInt(0);
        for (String oC : oldCountries) {
            int newIndex = mappings.stream().filter(e -> e.oldIndex == i.getValue()).findFirst().get().newIndex;
            newCountries.set(newIndex, oC);
            i.setValue(i.getValue() + 1);
        }

        File myObj = new File(String.format(rootPath, language.toString()) + "/countries.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(newCountries.stream().collect(Collectors.joining("\n")));
        myWriter.close();
    }

    private static void moveQuestions(Set<IndexMapping> mappings, Language language) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                "/implementations/countries/questions/aquestions/diff0/questions_diff0_cat%s.txt";

        for (CountriesCategoryEnum cat : CountriesCategoryEnum.values()) {

            if (cat == CountriesCategoryEnum.cat2) {
                continue;
            }

            List<String> lines = FlutterCountriesProcessor.readFileContents(String.format(rootPath, cat.getIndex()));

            List<String> newIndexLines = new ArrayList<>();
            for (String line : lines) {
                String newIndexLine = "";
                if (cat == CountriesCategoryEnum.cat0 || cat == CountriesCategoryEnum.cat1) {
                    int cIndex = Integer.parseInt(line.split(":")[0]) - 1;
                    int newIndex = mappings.stream().filter(e -> e.oldIndex == cIndex).findFirst().get().newIndex;
                    newIndexLine = newIndex + ":" + line.split(":")[1];
                } else {
                    String beforeDoubleDotText;
                    if (cat == CountriesCategoryEnum.cat3) {
                        int mainIndex = Integer.parseInt(line.split(":")[0]) - 1;
                        beforeDoubleDotText = String.valueOf(mappings.stream().filter(e -> e.oldIndex == mainIndex).findFirst().get().newIndex);
                    } else {
                        int mainIndex = Integer.parseInt(line.split(":")[0]);
                        if (cat == CountriesCategoryEnum.cat4) {
                            beforeDoubleDotText = getLabel(language, "countries_emp_" + mainIndex);
                        } else {
                            beforeDoubleDotText = getLabel(language, "countries_geo_" + mainIndex);
                        }
                    }
                    List<String> optIndex = Arrays.stream(line.split(":")[1].split(","))
                            .map(e -> {
                                int oldIndex = Integer.parseInt(e) - 1;
                                return String.valueOf(mappings.stream().filter(ee -> ee.oldIndex == oldIndex).findFirst().get().newIndex);
                            }).collect(Collectors.toList());
                    newIndexLine = beforeDoubleDotText + ":" + optIndex.stream().collect(Collectors.joining(","));
                }
                newIndexLines.add(newIndexLine);
            }

            if (!newIndexLines.isEmpty()) {
                File myObj = new File(String.format(rootPath, cat.getIndex()));
                myObj.createNewFile();
                FileWriter myWriter = new FileWriter(myObj);
                myWriter.write(newIndexLines.stream().collect(Collectors.joining("\n")));
                myWriter.close();
            }
        }

        List<String> syns = FlutterCountriesProcessor.getFromCountriesFolder(language, "synonyms");

        int index = 0;
        for (String s : syns) {
            int cIndex = Integer.parseInt(s.split(":")[0]) - 1;
            int newIndex = mappings.stream().filter(e -> e.oldIndex == cIndex).findFirst().get().newIndex;
            syns.set(index, newIndex + ":" + s.split(":")[1]);
            index++;
        }
    }

    private static String getLabel(Language language, String labelKey) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(LabelProcessor.getGameImplementationLabelsPath(GameIdEnum.countries)));
            String line = reader.readLine();
            while (line != null) {
                if (line.split("=")[0].equals(language + "_" + labelKey)) {
                    return line.split("=")[1];
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("not found " + labelKey);
        }
        throw new RuntimeException("not found " + labelKey);
    }

    private static void moveSynonymsToNewPos(Set<IndexMapping> mappings, Language language) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s";
        List<String> syns = FlutterCountriesProcessor.getFromCountriesFolder(language, "synonyms");

        int index = 0;
        for (String s : syns) {
            int cIndex = Integer.parseInt(s.split(":")[0]) - 1;
            int newIndex = mappings.stream().filter(e -> e.oldIndex == cIndex).findFirst().get().newIndex;
            syns.set(index, newIndex + ":" + s.split(":")[1]);
            index++;
        }


        File myObj = new File(String.format(rootPath, language.toString()) + "/synonyms.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(syns.stream().collect(Collectors.joining("\n")));
        myWriter.close();
    }

    private static void moveCapitalsToNewPos(Set<IndexMapping> mappings, Language language) throws IOException {
        String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources/implementations/countries/questions/%s";
        List<String> capitals = FlutterCountriesProcessor.getFromCountriesFolder(language, "capitals");
        List<String> capitalsToMove = FlutterCountriesProcessor.getFromCountriesFolder(language, "capitals");

        MutableInt i = new MutableInt(0);
        MutableInt cIndex = new MutableInt(0);
        for (String oC : capitals) {
            if (oC.contains(":")) {
                continue;
            }
            if (i.getValue() == 176) {
                cIndex.setValue(cIndex.getValue() + 1);
            }
            int newIndex = mappings.stream().filter(e -> e.oldIndex == cIndex.getValue()).findFirst().get().newIndex;
            capitalsToMove.set(i.getValue(), newIndex + ":" + oC);
            i.setValue(i.getValue() + 1);
            cIndex.setValue(cIndex.getValue() + 1);
        }


        File myObj = new File(String.format(rootPath, language.toString()) + "/capitals.txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(capitalsToMove.stream().collect(Collectors.joining("\n")));
        myWriter.close();
    }

    static Map<IndexMapping, String> createMappings() throws IOException {
        List<String> englishCountries = FlutterCountriesProcessor.getCountries(Language.en);
        List<String> englishCountries2 = FlutterCountriesProcessor.getFromCountriesFolder(Language.en, "countries_2");

        Map<IndexMapping, String> mappings = new HashMap<>();

        int i = 0;
        for (String c : englishCountries) {
            int j = 0;
            boolean found = false;
            for (String c2 : englishCountries2) {
                if (c.equals(c2.replace("----", ""))) {
                    mappings.put(new IndexMapping(i, j), c);
                    found = true;
                    break;
                }
                j++;
            }
            if (!found) {
                throw new RuntimeException("not found  " + c);
            }
            i++;
        }
        return mappings;
    }

    static class IndexMapping {

        int oldIndex;
        int newIndex;

        public IndexMapping(int oldIndex, int newIndex) {
            this.oldIndex = oldIndex;
            this.newIndex = newIndex;
        }

        @Override
        public String toString() {
            return
                    "oi=" + oldIndex +
                            ", ni=" + newIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexMapping that = (IndexMapping) o;
            return oldIndex == that.oldIndex &&
                    newIndex == that.newIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(oldIndex, newIndex);
        }
    }

}
