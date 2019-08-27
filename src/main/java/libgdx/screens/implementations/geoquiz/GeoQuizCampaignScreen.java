package libgdx.screens.implementations.geoquiz;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import libgdx.campaign.*;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ButtonWithIconBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.QuizQuestionCategoryEnum;
import libgdx.implementations.skelgame.QuizQuestionDifficultyLevel;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
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
        CampaignLevel campaignLevel = CampaignLevelEnumService.getCampaignLevelForDiffAndCat(difficultyLevel, categoryEnum);
        Table allTable = new Table();
        Res levelIcon = new CampaignLevelEnumService(campaignLevel).getIcon();
        CampaignStoreLevel maxFinishedLevel = campaignService.getMaxFinishedLevel(allCampaignLevelStores);
        int maxOpenedLevel = maxFinishedLevel != null ? maxFinishedLevel.getLevel() : -1;
        boolean levelLocked = (maxOpenedLevel + 1) < campaignLevel.getIndex();
        if (levelLocked) {
            levelIcon = MainResource.crown;
        } else if (campaignLevel.getIndex() <= maxOpenedLevel) {
            int starsWon = campaignService.getCampaignLevel(campaignLevel.getIndex(), allCampaignLevelStores).getStarsWon();
            if (starsWon == QuizStarsService.NR_OF_STARS_TO_DISPLAY) {
                allTable.setBackground(GraphicUtils.getNinePatch(MainResource.sound_on));
            } else {
                allTable.setBackground(GraphicUtils.getNinePatch(MainResource.sound_off));
            }
        }
        MyButton myButton = new ButtonWithIconBuilder("", levelIcon).build();
        myButton.setFillParent(true);
        if (!levelLocked) {
            myButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showCampaignGameScreen(new GameContextService().createGameContext(new CampaignLevelEnumService(campaignLevel).getQuestionConfig()), campaignLevel);
                }
            });
        } else {
            myButton.setTouchable(Touchable.disabled);
        }
        allTable.add(myButton).height(ICON_DIMEN).width(ICON_DIMEN);
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
