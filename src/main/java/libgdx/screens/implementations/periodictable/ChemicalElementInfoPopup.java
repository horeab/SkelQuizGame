package libgdx.screens.implementations.periodictable;

import libgdx.controls.popup.MyPopup;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.skelgame.CampaignScreenManager;
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
        text.append(chemicalElement.getName()).append("\n");
        text.append(" ").append("\n");
        text.append("Symbol: ").append(chemicalElement.getSymbol()).append("\n");
        text.append("Discovered by: ").append(chemicalElement.getDiscoveredBy()).append("\n");
        text.append("Year of discovery: ").append(chemicalElement.getYearOfDiscovery()).append("\n");
        text.append("Atomic weight: ").append(chemicalElement.getAtomicWeight()).append(" u").append("\n");
        text.append("Density: ").append(chemicalElement.getDensity()).append(" g/cm3").append("\n");
        return text.toString();
    }


}