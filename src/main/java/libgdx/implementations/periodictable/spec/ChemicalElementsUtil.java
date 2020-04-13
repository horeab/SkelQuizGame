package libgdx.implementations.periodictable.spec;

import com.google.gson.Gson;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChemicalElementsUtil {


    public static List<ChemicalElement> processTextForChemicalElements() {
        List<ChemicalElement> chemicalElements = new ArrayList<>();
        Scanner scanner = new Scanner(getAllFileText());
        while (scanner.hasNextLine()) {
            chemicalElements.add(new Gson().fromJson(scanner.nextLine(), ChemicalElement.class));
        }
        return chemicalElements;
    }


    public static String getName( int atomicNr) {
        return SpecificPropertiesUtils.getText("periodictable_" + atomicNr);
    }

    public static String getDiscoveredBy(String val) {
        switch (val) {
            case "Middle East":
                val = SpecificPropertiesUtils.getText("periodictable_middle_east");
                break;
            case "Egyptians and Sumerians":
                val = SpecificPropertiesUtils.getText("periodictable_egypt_sum");
                break;
            case "Indian metallurgists":
                val = SpecificPropertiesUtils.getText("periodictable_indianmet");
                break;
            case "Middle-Eastern alchemists":
                val = SpecificPropertiesUtils.getText("periodictable_mideast_alch");
                break;
            case "Asia Minor":
                val = SpecificPropertiesUtils.getText("periodictable_asia_minor");
                break;
            case "Egyptians":
                val = SpecificPropertiesUtils.getText("periodictable_egypt");
                break;
        }
        return StringUtils.capitalize(val);
    }

    public static String getYear(String year) {
        if (year.contains("-")) {
            year = SpecificPropertiesUtils.getText("periodictable_year", year.replace("-", ""));
        }
        return year;
    }

    public static String getAllFileText() {
        return new QuestionConfigFileHandler().getFileText("questions/all.txt");
    }

    public static ChemicalElement getElement(int period, int group, List<ChemicalElement> chemicalElements) {
        for (ChemicalElement e : chemicalElements) {
            if (e.getPeriod() == period && e.getGroup() == group) {
                return e;
            }
        }
        return null;
    }

    public static ChemicalElement getElementByNr(int aNr, List<ChemicalElement> chemicalElements) {
        for (ChemicalElement e : chemicalElements) {
            if (e.getAtomicNumber() == aNr) {
                return e;
            }
        }
        return null;
    }
}
