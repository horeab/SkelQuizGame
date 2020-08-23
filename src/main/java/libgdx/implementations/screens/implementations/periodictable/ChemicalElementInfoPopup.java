package libgdx.implementations.screens.implementations.periodictable;

import libgdx.controls.popup.MyPopup;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;

public class ChemicalElementInfoPopup extends MyPopup<AbstractScreen, CampaignScreenManager> {


    private ChemicalElement chemicalElement;

    public ChemicalElementInfoPopup(AbstractScreen abstractScreen, ChemicalElement chemicalElement) {
        super(abstractScreen);
        this.chemicalElement = chemicalElement;
    }

    @Override
    public void addButtons() {
    }

    @Override
    public float getPrefWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(60);
    }

    @Override
    protected String getLabelText() {

        StringBuilder text = new StringBuilder();
        text.append(ChemicalElementsUtil.getName(chemicalElement.getAtomicNumber())).append("\n");
        text.append(" ").append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_3")).append(": ")
                .append(getInfoString(chemicalElement.getAtomicNumber()+"")).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_0")).append(": ")
                .append(getInfoString(chemicalElement.getSymbol())).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_1")).append(": ")
                .append(getInfoString(ChemicalElementsUtil.getDiscoveredBy(chemicalElement.getDiscoveredBy()))).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_2")).append(": ")
                .append(getInfoString(ChemicalElementsUtil.getYear(String.valueOf(chemicalElement.getYearOfDiscovery())))).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_atomic_weight")).append(": ")
                .append(getInfoString(chemicalElement.getAtomicWeight() + " u")).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_density")).append(": ")
                .append(getInfoString(chemicalElement.getDensity() + " g/cm3")).append("\n");
        return text.toString();
    }

    private String getInfoString(String val) {
        return val;
    }
}