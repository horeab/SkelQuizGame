package libgdx.screens.implementations.geoquiz;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.QuizQuestionCategoryEnum;
import libgdx.implementations.skelgame.QuizQuestionDifficultyLevel;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.Resource;
import libgdx.resources.dimen.Dimen;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screens.QuizScreenManager;

import java.util.List;

public class GeoQuizCampaignScreen extends AbstractScreen<QuizScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private int TOTAL_STARS = 5;
    private float ICON_DIMEN = MainDimen.horizontal_general_margin.getDimen() * 7.5f;

    public GeoQuizCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable());
        addActor(table);
    }

    private Table createAllTable() {
        Table table = new Table();
        for (QuizQuestionDifficultyLevel difficultyLevel : QuizQuestionDifficultyLevel.values()) {
            table.add(createCategoriesTable(difficultyLevel)).padBottom(MainDimen.vertical_general_margin.getDimen() * 2);
            table.row();
        }
        return table;
    }

    private Table createCategoriesTable(QuizQuestionDifficultyLevel difficultyLevel) {
        Table table = new Table();
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));

        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        for (QuizQuestionCategoryEnum categoryEnum : QuizQuestionCategoryEnum.values()) {
            Table categoryTable = createCategoryTable(difficultyLevel, categoryEnum);
            table.add(categoryTable).pad(horizontalGeneralMarginDimen).height(categoryTable.getHeight()).width(categoryTable.getWidth());
        }
        return table;
    }

    private Table createCategoryTable(QuizQuestionDifficultyLevel difficultyLevel, QuizQuestionCategoryEnum categoryEnum) {
        Table allTable = new Table();
        allTable.setBackground(GraphicUtils.getNinePatch(MainResource.background_texture));
        Table table = new Table();
        table.setFillParent(true);
        CampaignLevel campaignLevel = CampaignLevelEnumService.getCampaignLevelForDiffAndCat(difficultyLevel, categoryEnum);
        Res levelIcon = new CampaignLevelEnumService(campaignLevel).getIcon();
        if (campaignService.getMaxOpenedLevel(allCampaignLevelStores).getLevel() < campaignLevel.getIndex()) {
            levelIcon = MainResource.crown;
        }
        table.setBackground(GraphicUtils.getNinePatch(levelIcon));
        allTable.add(table).height(ICON_DIMEN).width(ICON_DIMEN);
        allTable.setHeight(ICON_DIMEN);
        allTable.setWidth(ICON_DIMEN);
        return allTable;
    }

    private Table createTotalStarsTable() {
        Table table = new Table();
        float imgSideDim = MainDimen.vertical_general_margin.getDimen() * 4f;
        MyWrappedLabel myLabel = new MyWrappedLabel(campaignService.getTotalWonStars(allCampaignLevelStores) + "/" + TOTAL_STARS);
        myLabel.setFontScale(FontManager.getBigFontDim());
        myLabel.padTop(MainDimen.vertical_general_margin.getDimen() / 2);
        myLabel.padRight(MainDimen.vertical_general_margin.getDimen());
        table.add(myLabel);
        return table;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
