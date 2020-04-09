package libgdx.implementations.periodictable.spec;

import com.google.gson.Gson;
import libgdx.campaign.QuestionConfigFileHandler;

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
