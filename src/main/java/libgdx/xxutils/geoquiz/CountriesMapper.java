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
import libgdx.xxutils.LabelProcessor;

public class CountriesMapper {

    public static void main(String[] args) throws IOException {

        Map<IndexMapping, String> mappings = createMappings();

//        List<Language> langs = Arrays.asList(Language.values());
//        langs.remove(Language.en);

        List<Language> langs = Arrays.asList(Language.en, Language.ro);

//        addCountryRanking();

        for (Language lang : langs) {
//            createNewCountriesFile(mappings.keySet(), lang);
//            moveCapitalsToNewPos(mappings.keySet(), lang);
            moveQuestions(mappings.keySet(), lang);
//            moveSynonymsToNewPos(mappings.keySet(), lang);
        }
    }

    private static void addCountryRanking() throws IOException {
        List<String> englishCountries2 = FlutterCountriesProcessor.getFromCountriesFolder(Language.en, "countries_2");
        List<String> res = new ArrayList<>();
        List<String> countriesRanked = Arrays.stream(ranks.split("\n"))
                .map(e -> e.split("\t")[0]).collect(Collectors.toList());

        countriesRanked.removeIf(e -> !englishCountries2.stream().map(c -> c.replace("----", "")
                .replace("****", "")).collect(Collectors.toList()).contains(e));

        for (String c : englishCountries2) {
            boolean foundC = false;
            for (String ranked : countriesRanked) {
                if (ranked.equals(c.replace("----", "").replace("****", ""))) {
                    int rank = countriesRanked.indexOf(ranked);
                    res.add(c + ":" + rank);
                    foundC = true;
                    break;
                }
            }
            if (!foundC) {
                throw new RuntimeException("not found " + c);
            }
        }
        res.forEach(e -> System.out.println(e));
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

        for (CountriesCategoryEnum cat : CountriesCategoryEnum.values()) {
            String rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                    "/implementations/countries/questions/aquestions/diff0/questions_diff0_cat%s.txt";

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

                        String testStart = beforeDoubleDotText;
                        if (newIndexLines.stream().anyMatch(e -> e.startsWith(testStart + ":"))) {
                            throw new RuntimeException("already contains " + Integer.parseInt(line.split(":")[0]));
                        }
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

            if (cat == CountriesCategoryEnum.cat0 || cat == CountriesCategoryEnum.cat1 || cat == CountriesCategoryEnum.cat3) {
                rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                        "/implementations/countries/questions/aquestions/diff0/temp/questions_diff0_cat%s.txt";
            }

            if (cat == CountriesCategoryEnum.cat4 || cat == CountriesCategoryEnum.cat5) {
                rootPath = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources/tournament_resources" +
                        "/implementations/countries/questions/" + language + "/diff0/questions_diff0_cat%s.txt";
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
                if (c.equals(c2.replace("----", "")
                        .replace("****", "").split(":")[0])) {
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


    static final String ranks = "China\n" +
            "India\n" +
            "United States\n" +
            "Brazil\n" +
            "Russia\n" +
            "Mexico\n" +
            "Japan\n" +
            "Iran\n" +
            "Germany\n" +
            "Turkey\n" +
            "France\n" +
            "United Kingdom\n" +
            "Thailand\n" +
            "Italy\n" +
            "Egypt\n" +
            "South Africa\n" +
            "South Korea\n" +
            "Spain\n" +
            "Argentina\n" +
            "Belgium\n" +
            "Ukraine\n" +
            "Poland\n" +
            "Canada\n" +
            "Austria\n" +
            "Switzerland\n" +
            "Netherlands\n" +
            "Indonesia\n" +
            "Greece\n" +
            "Portugal\n" +
            "Australia\n" +
            "Colombia\n" +
            "Algeria\n" +
            "Croatia\n" +
            "Morocco\n" +
            "Vietnam\n" +
            "Sweden\n" +
            "Nigeria\n" +
            "Chile\n" +
            "Hungary\n" +
            "Romania\n" +
            "Saudi Arabia\n" +
            "Bulgaria\n" +
            "Denmark\n" +
            "Finland\n" +
            "Ireland\n" +
            "Norway\n" +
            "Serbia\n" +
            "Malaysia\n" +
            "Peru\n" +
            "Georgia\n" +
            "Cuba\n" +
            "Ghana\n" +
            "Kazakhstan\n" +
            "Iraq\n" +
            "Afghanistan\n" +
            "Israel\n" +
            "Iceland\n" +
            "Bosnia and Herzegovina\n" +
            "Armenia\n" +
            "Slovenia\n" +
            "New Zealand\n" +
            "Albania\n" +
            "Slovakia\n" +
            "Venezuela\n" +
            "Czech Republic\n" +
            "Ecuador\n" +
            "Senegal\n" +
            "Nepal\n" +
            "Somalia\n" +
            "Azerbaijan\n" +
            "Tunisia\n" +
            "Mongolia\n" +
            "Pakistan\n" +
            "Philippines\n" +
            "Sri Lanka\n" +
            "Kenya\n" +
            "Cambodia\n" +
            "Lithuania\n" +
            "Mali\n" +
            "Montenegro\n" +
            "Belarus\n" +
            "Cameroon\n" +
            "Uzbekistan\n" +
            "Myanmar\n" +
            "Uruguay\n" +
            "Jamaica\n" +
            "Bolivia\n" +
            "Uganda\n" +
            "Ivory Coast\n" +
            "Cyprus\n" +
            "Madagascar\n" +
            "Haiti\n" +
            "Angola\n" +
            "Qatar\n" +
            "North Korea\n" +
            "Tanzania\n" +
            "Yemen\n" +
            "Niger\n" +
            "Ethiopia\n" +
            "Costa Rica\n" +
            "Syria\n" +
            "Jordan\n" +
            "Dominican Republic\n" +
            "United Arab Emirates\n" +
            "Honduras\n" +
            "Tajikistan\n" +
            "Monaco\n" +
            "Chad\n" +
            "Mozambique\n" +
            "Estonia\n" +
            "Singapore\n" +
            "Zambia\n" +
            "Benin\n" +
            "Papua New Guinea\n" +
            "Guinea\n" +
            "Togo\n" +
            "Paraguay\n" +
            "Laos\n" +
            "Bangladesh\n" +
            "Rwanda\n" +
            "Burkina Faso\n" +
            "Libya\n" +
            "Lebanon\n" +
            "Kyrgyzstan\n" +
            "Turkmenistan\n" +
            "Malawi\n" +
            "Sudan\n" +
            "Central African Republic\n" +
            "Palestine\n" +
            "Democratic Republic of the Congo\n" +
            "Oman\n" +
            "Liechtenstein\n" +
            "Guatemala\n" +
            "South Sudan\n" +
            "Burundi\n" +
            "Liberia\n" +
            "Zimbabwe\n" +
            "Kuwait\n" +
            "Panama\n" +
            "Sierra Leone\n" +
            "Luxembourg\n" +
            "Eritrea\n" +
            "San Marino\n" +
            "Moldova\n" +
            "Namibia\n" +
            "Gambia\n" +
            "Mauritania\n" +
            "Botswana\n" +
            "Gabon\n" +
            "Congo\n" +
            "North Macedonia\n" +
            "Lesotho\n" +
            "Latvia\n" +
            "Guinea-Bissau\n" +
            "Bahrain\n" +
            "Equatorial Guinea\n" +
            "East Timor\n" +
            "Trinidad and Tobago\n" +
            "Mauritius\n" +
            "Djibouti\n" +
            "Eswatini\n" +
            "Comoros\n" +
            "Fiji\n" +
            "Guyana\n" +
            "El Salvador\n" +
            "Nicaragua\n" +
            "Bhutan\n" +
            "Solomon Islands\n" +
            "Suriname\n" +
            "Cape Verde\n" +
            "Malta\n" +
            "Brunei\n" +
            "Belize\n" +
            "Bahamas\n" +
            "Maldives\n" +
            "Vanuatu\n" +
            "Vatican City\n" +
            "Barbados\n" +
            "São Tomé and Príncipe\n" +
            "Samoa\n" +
            "Saint Lucia\n" +
            "Kiribati\n" +
            "Grenada\n" +
            "Saint Vincent and the Grenadines\n" +
            "Micronesia\n" +
            "Tonga\n" +
            "Seychelles\n" +
            "Antigua and Barbuda\n" +
            "Andorra\n" +
            "Dominica\n" +
            "Marshall Islands\n" +
            "Saint Kitts and Nevis\n" +
            "Palau\n" +
            "Nauru\n" +
            "Tuvalu";

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
