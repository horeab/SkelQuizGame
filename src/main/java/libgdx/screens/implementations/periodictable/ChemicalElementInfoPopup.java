package libgdx.screens.implementations.periodictable;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.popup.MyPopup;
import libgdx.implementations.periodictable.PeriodicTableCategoryEnum;
import libgdx.implementations.periodictable.PeriodicTableDifficultyLevel;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;

public class ChemicalElementInfoPopup extends MyPopup<AbstractScreen, CampaignScreenManager> {


    private ChemicalElement chemicalElement;
    private CampaignStoreService campaignStoreService = new CampaignStoreService();

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
        text.append(chemicalElement.getName()).append("\n");
        text.append(" ").append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_0")).append(": ")
                .append(getInfoString(PeriodicTableCategoryEnum.cat0, chemicalElement.getSymbol())).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_1")).append(": ")
                .append(getInfoString(PeriodicTableCategoryEnum.cat1, ChemicalElementsUtil.getDiscoveredBy(chemicalElement.getDiscoveredBy()))).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_2")).append(": ")
                .append(getInfoString(PeriodicTableCategoryEnum.cat2,ChemicalElementsUtil.getYear(String.valueOf(chemicalElement.getYearOfDiscovery())))).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_3")).append(": ")
                .append(getInfoString(PeriodicTableCategoryEnum.cat3, chemicalElement.getAtomicWeight())).append("\n");
        text.append(SpecificPropertiesUtils.getText("periodictable_question_category_4")).append(": ")
                .append(getInfoString(PeriodicTableCategoryEnum.cat4, chemicalElement.getDensity())).append("\n");
        return text.toString();
    }

    private String getInfoString(PeriodicTableCategoryEnum categoryEnum, String val) {
        String suffix = categoryEnum == PeriodicTableCategoryEnum.cat4 ? " g/cm3" : "";
        suffix = categoryEnum == PeriodicTableCategoryEnum.cat3 ? " u" : suffix;
//        return campaignStoreService.isQuestionAlreadyPlayed(PeriodicTableContainers.getQuestionId(chemicalElement.getAtomicNumber(), categoryEnum, PeriodicTableDifficultyLevel._0))
//                ? val + suffix :
//                "???";
        return val + suffix;
    }
}