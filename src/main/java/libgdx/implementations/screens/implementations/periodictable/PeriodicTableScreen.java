package libgdx.implementations.screens.implementations.periodictable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.periodictable.PeriodicTableCreatorDependencies;
import libgdx.implementations.periodictable.PeriodicTableSpecificResource;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.gameservice.CreatorDependenciesContainer;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;

import java.util.List;

public class PeriodicTableScreen extends AbstractScreen<PeriodicTableScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private List<ChemicalElement> chemicalElements;

    public PeriodicTableScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
        chemicalElements = ((PeriodicTableCreatorDependencies) CreatorDependenciesContainer.getCreator(PeriodicTableCreatorDependencies.class)).getElements();
    }

    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(getAbstractScreen());
    }

    private Table createAllTable() {
        Table table = new Table();
        int periods = 7;
        int groups = 18;
        int secRows = 2;
        int secCols = 15;

        table.add(createTable(groups, periods)).row();
        table.add(createSecondaryTable(secRows, secCols));

        return table;
    }

    private Table createSecondaryTable(int rows, int cols) {
        float marginDimen = MainDimen.vertical_general_margin.getDimen() / 10;
        float sideDimen = getElSideDimen();
        Table table = new Table();
        int atomicNr = 57;
        for (int r = 0; r < rows; r++) {
            Table row = new Table();
            for (int c = 0; c < cols; c++) {
                Table col = new Table();
                ChemicalElement e = ChemicalElementsUtil.getElementByNr(atomicNr, chemicalElements);
                boolean isNotElement = e == null;
                if (atomicNr == 72) {
                    atomicNr = 89;
                    c--;
                } else if (!isNotElement) {
                    Res background = PeriodicTableSpecificResource.valueOf("eltype_" + e.getType() + "_background");
                    col.add(createElementInfoTable(e));
                    atomicNr++;
                    col.setBackground(GraphicUtils.getNinePatch(background));
                    row.add(col).pad(marginDimen).width(sideDimen).height(sideDimen);
                }
            }
            table.add(row).row();
        }
        return table;
    }

    private Table createTable(int groups, int periods) {
        float marginDimen = MainDimen.vertical_general_margin.getDimen() / 10;
        float sideDimen = getElSideDimen();
        Table table = new Table();
        int atomicNr = 1;
        for (int period = 1; period <= periods; period++) {
            Table row = new Table();
            for (int group = 1; group <= groups; group++) {
                Table col = new Table();
                ChemicalElement e = ChemicalElementsUtil.getElement(period, group, chemicalElements);
                boolean isNotElement = e == null;
                Res background = MainResource.transparent_background;
                if (atomicNr == 57) {
                    atomicNr = 72;
                } else if (atomicNr == 89) {
                    atomicNr = 104;
                } else if (atomicNr <= 118 && !isNotElement) {
                    background = PeriodicTableSpecificResource.valueOf("eltype_" + e.getType() + "_background");
                    ChemicalElement toDisplay = ChemicalElementsUtil.getElementByNr(atomicNr, chemicalElements);
                    col.add(createElementInfoTable(toDisplay));
                    atomicNr++;
                }
                col.setBackground(GraphicUtils.getNinePatch(background));
                row.add(col).pad(marginDimen).width(sideDimen).height(sideDimen);
            }
            table.add(row).row();
        }
        return table;
    }

    private Table createElementInfoTable(ChemicalElement toDisplay) {
        Table table = new Table();
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.BLACK, FontConfig.FONT_SIZE / 2))
                .setText(toDisplay.getAtomicNumber() + "")
                .build())).row();
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.BLACK, FontConfig.FONT_SIZE / 1.3f))
                .setText(toDisplay.getSymbol())
                .build()));
        return table;
    }

    private float getElSideDimen() {
        return ScreenDimensionsManager.getScreenHeightValue(9);
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
    }
}
