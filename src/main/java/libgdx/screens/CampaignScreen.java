package libgdx.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.*;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.QuizGame;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;

import java.util.List;

public class CampaignScreen extends AbstractScreen {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private int NR_OF_LEVELS = 4;
    private float levelHeight;

    @Override
    protected void initFields() {
        super.initFields();
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
        levelHeight = MainDimen.vertical_general_margin.getDimen() * 12;
    }

    @Override
    public void buildStage() {
        initFields();
        Table table = new Table();
        table.setFillParent(true);

        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(new BackButtonBuilder().createScreenBackButton(this)).padLeft(-verticalGeneralMarginDimen * 7).width(verticalGeneralMarginDimen * 4f).height(verticalGeneralMarginDimen * 4f);
        table.add(createTotalStarsTable()).padLeft(-verticalGeneralMarginDimen * 10).row();
        table.add(createMain()).colspan(2).padTop(verticalGeneralMarginDimen * 10).expand();
//        table.setBackground(GraphicUtils.getNinePatch(Resource.campaign_screen_background));
        addActor(table);
    }

    private Table createTotalStarsTable() {
        Table table = new Table();
        float imgSideDim = MainDimen.vertical_general_margin.getDimen() * 4f;
        MyWrappedLabel myLabel = new MyWrappedLabel(campaignService.getTotalWonStars(allCampaignLevelStores) + "/" + NR_OF_LEVELS * 99);
        myLabel.setFontScale(FontManager.getBigFontDim());
        myLabel.padTop(MainDimen.vertical_general_margin.getDimen() / 2);
        myLabel.padRight(MainDimen.vertical_general_margin.getDimen());
        table.add(myLabel);
        table.padRight(MainDimen.horizontal_general_margin.getDimen() * 3);
        table.add(GraphicUtils.getImage(Resource.star_enabled)).height(imgSideDim).width(imgSideDim);
        return table;
    }

    private Stack createMain() {
        Table levelIconsTable = new Table();
        CampaignLevel[] levels = (CampaignLevel[]) EnumUtils.getValues(QuizGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum());
        boolean leftToRight = true;
        int i = 0;
        float itemTableWidth = ScreenDimensionsManager.getScreenWidthValue(95);
        while (i < NR_OF_LEVELS) {
            Table backgroundTable = new Table();
            Table levelIconTable = new Table();
            Resource backgroundTexture = new CampaignLevelEnumService(levels[i]).getBackgroundTexture();
            if (i == 0) {
                addLevelButtons(levelIconTable, levels[i], levels[i + 1]);
                i += 2;
            } else if (i + 1 > NR_OF_LEVELS - 1) {
                addLevelButtons(levelIconTable, levels[i], null);
                i += 1;
            } else {
                leftToRight = !leftToRight;
                addLevelButtons(levelIconTable, levels[i], levels[i + 1]);
                i += 2;
            }
            backgroundTable.setBackground(GraphicUtils.getNinePatch(backgroundTexture));
            levelIconsTable.add(levelIconTable).height(levelHeight).width(itemTableWidth).row();
        }
        levelIconsTable.add().height(levelHeight).width(itemTableWidth).row();
        Stack stack = new Stack();
        stack.addActor(levelIconsTable);
        return stack;
    }

    private void addLevelButtons(Table lineTable, CampaignLevel level1, CampaignLevel level2) {
        Table levelsTable = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        if (level1 != null) {
            levelsTable.add(createButtonTable(level1));
        }
        if (level2 != null) {
            levelsTable.add().pad(verticalGeneralMarginDimen * 3);
            levelsTable.add(createButtonTable(level2));
        }
        levelsTable.row();
        lineTable.add(levelsTable).fillX();
    }

    private Table createButtonTable(CampaignLevel levelEnum) {
        Table levelTable = new Table();
        CampaignStoreLevel campaignStoreLevel = campaignService.getCampaignLevel(levelEnum.getIndex(), allCampaignLevelStores);
        MyButton levelBtn = new CampaignLevelButtonBuilder(levelEnum, campaignStoreLevel).setLevelLocked(campaignStoreLevel == null).build();
        levelTable.add(levelBtn).width(levelBtn.getWidth()).height(levelBtn.getHeight()).row();
        levelTable.setWidth(levelBtn.getWidth());
        levelTable.setHeight(levelBtn.getHeight());
        return levelTable;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
