package libgdx.implementations.screens.implementations.animals;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.labelimage.InAppPurchaseTable;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangmanarena.CampaignLevelButtonBuilder;
import libgdx.implementations.hangmanarena.HangmanArenaGame;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;

import java.util.List;

public class AnimalsCampaignScreen extends AbstractScreen<AnimalsScreenManager> {

    private int scrollPanePositionInit = 0;
    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private ScrollPane scrollPane;
    private Integer scrollToLevel;
    private int nrOfLevels;
    private float levelHeight;

    public AnimalsCampaignScreen() {
        allCampaignLevelStores = campaignService.getFinishedCampaignLevels();
    }

    @Override
    public void buildStage() {
        nrOfLevels = ((CampaignLevel[]) EnumUtils.getValues(HangmanArenaGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum())).length;
        scrollToLevel = allCampaignLevelStores.size();
        levelHeight = MainDimen.vertical_general_margin.getDimen() * 18;
        scrollPane = new ScrollPane(createAllScroll());

        Table table = new Table();
        table.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).expand();
        scrollPane.scrollTo(0, 2210, 0, 0);
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Stack createAllScroll() {
        Table backgroundsTable = new Table();
        Table dottedLinesTable = new Table();
        Table levelIconsTable = new Table();
        CampaignLevel[] levels = (CampaignLevel[]) EnumUtils.getValues(HangmanArenaGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum());
        boolean leftToRight = true;
        int i = 0;
        float itemTableWidth = ScreenDimensionsManager.getScreenWidthValue(95);
        while (i < nrOfLevels) {
            Table backgroundTable = new Table();
            Table dottedLineTable = new Table();
            Table levelIconTable = new Table();
            Res dottedLineResource;
            Res backgroundTexture = getBackgroundTexture(levels[i]);
            if (i == 0) {
                addLevelButtons(levelIconTable, i, 1, levels[i], levels[i + 1]);
                dottedLineResource = HangmanArenaSpecificResource.table_down_center_left_dotted_line;
                i += 2;
            } else if (i + 1 > nrOfLevels - 1) {
                addLevelButtons(levelIconTable, i, -1, levels[i], null);
                dottedLineResource = (nrOfLevels - 1) % 2 == 0 ? HangmanArenaSpecificResource.table_up_center_left_dotted_line : HangmanArenaSpecificResource.table_center_right_dotted_line;
                i += 1;
            } else {
                dottedLineResource = leftToRight ? HangmanArenaSpecificResource.table_left_right_dotted_line : HangmanArenaSpecificResource.table_right_left_dotted_line;
                leftToRight = !leftToRight;
                addLevelButtons(levelIconTable, i, leftToRight ? 1 : -1, levels[i], levels[i + 1]);
                i += 2;
            }
            backgroundTable.setBackground(GraphicUtils.getNinePatch(backgroundTexture));
            dottedLineTable.setBackground(GraphicUtils.getNinePatch(dottedLineResource));
            backgroundsTable.add(backgroundTable).height(levelHeight).width(itemTableWidth).row();
            dottedLinesTable.add(dottedLineTable).height(levelHeight).width(itemTableWidth).row();
            levelIconsTable.add(levelIconTable).height(levelHeight).width(itemTableWidth).row();
        }
        Table backgroundTable = new Table();
        backgroundTable.setBackground(GraphicUtils.getNinePatch(getBackgroundTexture(levels[nrOfLevels - 1])));
        backgroundsTable.add(backgroundTable).height(levelHeight).width(itemTableWidth).row();
        dottedLinesTable.add().height(levelHeight).width(itemTableWidth).row();
        levelIconsTable.add().height(levelHeight).width(itemTableWidth).row();
        Stack stack = new Stack();
        stack.addActor(backgroundsTable);
        stack.addActor(dottedLinesTable);
        stack.addActor(levelIconsTable);
        return stack;
    }

    public HangmanArenaSpecificResource getBackgroundTexture(CampaignLevel level) {
        return EnumUtils.getEnumValue(HangmanArenaSpecificResource.class, "campaign_level_" + new CampaignLevelEnumService(level).getDifficulty() + "_background");
    }

    private void addLevelButtons(Table lineTable, int i, int sidePadDirection, CampaignLevel level1, CampaignLevel level2) {
        Table levelsTable = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float padTopAllTable = verticalGeneralMarginDimen;

//        boolean extraContentLocked = (i >= (AnimalsQuestionCategoryEnum.values().length)) && !Utils.isValidExtraContent();
        boolean extraContentLocked = false;
        InAppPurchaseTable inAppPurchaseTable = new InAppPurchaseTable();
        if (level1 != null) {
//            Table buttonTable = createButtonTable(level1, extraContentLocked);
//            levelsTable.add(extraContentLocked ? inAppPurchaseTable
//                    .create(buttonTable, new Runnable() {
//                        @Override
//                        public void run() {
//                            screenManager.showCampaignScreen();
//                        }
//                    }) : buttonTable).row();
//        }
//        if (level2 != null) {
//            Table buttonTable = createButtonTable(level2, extraContentLocked);
//            levelsTable.add(extraContentLocked ? inAppPurchaseTable
//                    .create(buttonTable, new Runnable() {
//                        @Override
//                        public void run() {
//                            screenManager.showCampaignScreen();
//                        }
//                    }) : buttonTable).padTop(verticalGeneralMarginDimen).padRight(sidePadDirection * MainDimen.horizontal_general_margin.getDimen() * 26).row();
//            padTopAllTable = verticalGeneralMarginDimen * 9;
        }

        lineTable.add(levelsTable).padTop(padTopAllTable).fillX();
    }

    private Table createButtonTable(CampaignLevel levelEnum, boolean extraContentLocked) {
        Table levelTable = new Table();
        CampaignStoreLevel campaignLevel = campaignService.getCampaignLevel(levelEnum.getIndex(), allCampaignLevelStores);
        boolean levelLocked = (campaignLevel == null && levelEnum.getIndex() != scrollToLevel) || extraContentLocked;
        MyButton levelBtn = new CampaignLevelButtonBuilder(getAbstractScreen(), levelEnum, campaignLevel)
                .setLevelLocked(levelLocked).build();
        levelTable.add(levelBtn).width(levelBtn.getWidth()).height(levelBtn.getHeight()).row();
        levelTable.setWidth(levelBtn.getWidth());
        levelTable.setHeight(levelBtn.getHeight());
        return levelTable;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
//         scrollPanePositionInit needs to be used otherwise the scrollTo wont work
        if (scrollPane != null && scrollPanePositionInit < 2) {
            scrollPane.setScrollY((scrollToLevel / 2) * levelHeight);
            scrollPanePositionInit++;
        }
    }

}
