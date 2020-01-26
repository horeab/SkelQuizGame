package libgdx.screens.implementations.conthistory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

import javax.swing.event.ChangeEvent;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionConfig;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.conthistory.ConthistoryCampaignLevelEnum;
import libgdx.implementations.conthistory.ConthistoryCategoryEnum;
import libgdx.implementations.conthistory.ConthistoryGame;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class ConthistoryCampaignScreen extends AbstractScreen<ConthistoryScreenManager> {


    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public ConthistoryCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }


    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 3,
                        3f))
                .setText(Game.getInstance().getAppInfoService().getAppName()).build());

        table.add(titleLabel).padTop(MainDimen.vertical_general_margin.getDimen() * 2).colspan(2).row();
        table.add(createAllTable());
        addActor(table);
    }


    private Table createAllTable() {
        Table table = new Table();
        int totalCat = ConthistoryCategoryEnum.values().length;
        long totalStarsWon = 0;
        float btnHeight = ScreenDimensionsManager.getScreenHeightValue(12);
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(80);
        for (int i = 0; i < totalCat; i++) {
            final int finalIndex = i;
            ConthistoryCategoryEnum categoryEnum = ConthistoryCategoryEnum.values()[i];
            MyButton categBtn = new ButtonBuilder()
                    .setWrappedText(new SpecificPropertiesUtils().getQuestionCategoryLabel(categoryEnum.getIndex()), btnWidth / 1.1f)
                    .setButtonSkin(GameButtonSkin.valueOf("CONTHISTORY_COLOR_CATEG" + i)).build();
            for (CampaignStoreLevel level : allCampaignLevelStores) {
                long starsWon = -1;
                if (CampaignLevelEnumService.getCategory(level.getName()) == categoryEnum.getIndex()) {
                    starsWon = level.getScore();
                    totalStarsWon = totalStarsWon + starsWon;
                    if (starsWon == QuizStarsService.NR_OF_STARS_TO_DISPLAY) {
                        categBtn.setButtonSkin(GameButtonSkin.PAINTINGS_COLOR_CATEG_STAR);
                        break;
                    } else if (level.getStatus() == CampaignLevelStatusEnum.FINISHED.getStatus()) {
                        categBtn.setButtonSkin(GameButtonSkin.PAINTINGS_COLOR_CATEG_FINISHED);
                        break;
                    }
                }
            }
            categBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    CampaignLevel campaignLevel = ConthistoryCampaignLevelEnum.valueOf("LEVEL_0_" + finalIndex);
                    CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                    QuestionConfig questionConfig = enumService.getQuestionConfig(ConthistoryGameScreen.TOTAL_QUESTIONS);
                    ConthistoryGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
                }
            });
            Table btnTable = new Table();
            btnTable.add(categBtn)
                    .height(btnHeight)
                    .width(btnWidth);
            table.add(btnTable)
                    .height(btnHeight)
                    .width(btnWidth);
            table.row();
        }
        if (campaignService.getFinishedCampaignLevels().size() == ConthistoryCampaignLevelEnum.values().length) {
            CampaignStoreService campaignStoreService = new CampaignStoreService();
            String gameFinishedText = SkelGameLabel.game_finished.getText();
            if (campaignStoreService.getAllScoreWon() < totalStarsWon) {
                campaignStoreService.updateAllScoreWon(totalStarsWon);
                gameFinishedText = MainGameLabel.l_score_record.getText(totalStarsWon);
            }
            new LevelFinishedPopup(this, gameFinishedText).addToPopupManager();
        }
        return table;
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
