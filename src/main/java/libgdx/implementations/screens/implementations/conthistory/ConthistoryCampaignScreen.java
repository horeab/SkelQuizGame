package libgdx.implementations.screens.implementations.conthistory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

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
import libgdx.controls.labelimage.LabelImageConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.conthistory.ConthistoryCampaignLevelEnum;
import libgdx.implementations.conthistory.ConthistoryCategoryEnum;
import libgdx.implementations.conthistory.ConthistoryGame;
import libgdx.implementations.conthistory.ConthistorySpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
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

    private int scrollPanePositionInit = 0;
    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private ScrollPane scrollPane;
    private Integer scrollToLevel;
    private float levelHeight;

    public ConthistoryCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }


    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        final int maxOpenedLevel = allCampaignLevelStores.size();
        scrollToLevel = maxOpenedLevel > 1 ? maxOpenedLevel - 1 : null;
        levelHeight = getBtnHeightValue() + MainDimen.horizontal_general_margin.getDimen();

        Table table = new Table();
        table.setFillParent(true);
        scrollPane = new ScrollPane(createAllTable());
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).expand();
        addActor(table);
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(ConthistorySpecificResource.title_clouds_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
    }

    private Table createAllTable() {
        Table table = new Table();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_GREEN.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 2.4f,
                        4f))
                .setText(Game.getInstance().getAppInfoService().getAppName()).build());

        float btnHeight = getBtnHeightValue();
        float dimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createTitleStack(titleLabel)).height(btnHeight / 2).pad(dimen * 2).row();
        int totalCat = ConthistoryCategoryEnum.values().length;
        long totalStarsWon = 0;
        float btnWidth = ScreenDimensionsManager.getScreenWidth(80);
        for (int i = 0; i < totalCat; i++) {
            final int finalIndex = i;
            ConthistoryCategoryEnum categoryEnum = ConthistoryCategoryEnum.values()[i];
            LabelImageConfigBuilder labelImageConfigBuilder = new LabelImageConfigBuilder()
                    .setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(categoryEnum.getIndex()))
                    .setWrappedLineLabel(btnWidth)
                    .setFontConfig(new FontConfig(
                            FontColor.BLACK.getColor(),
                            FontColor.DARK_RED.getColor(),
                            FontConfig.FONT_SIZE * 2.5f,
                            1f));
            MyButton categBtn = new ButtonBuilder()
                    .setWrappedText(labelImageConfigBuilder)
                    .setButtonSkin(GameButtonSkin.valueOf("CONTHISTORY_COLOR_CATEG" + i)).build();
            final int maxOpenedLevel = allCampaignLevelStores.size();
            if (i >= maxOpenedLevel) {
                categBtn.setDisabled(true);
            }
            for (CampaignStoreLevel level : allCampaignLevelStores) {
                long starsWon = -1;
                if (CampaignLevelEnumService.getCategory(level.getName()) == categoryEnum.getIndex()) {
                    starsWon = level.getScore();
                    totalStarsWon = totalStarsWon + starsWon;
                    ConthistorySpecificResource categImg = null;

                    if (starsWon == QuizStarsService.NR_OF_STARS_TO_DISPLAY) {
                        categImg = ConthistorySpecificResource.success_star;
                    } else if (level.getStatus() == CampaignLevelStatusEnum.FINISHED.getStatus()) {
                        categImg = ConthistorySpecificResource.success;
                    }
                    if (categImg != null) {
                        Image image = GraphicUtils.getImage(categImg);
                        float imgDimen = dimen * 7;
                        categBtn.add(image).padBottom(dimen).width(imgDimen).height(imgDimen);
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

    private float getBtnHeightValue() {
        return ScreenDimensionsManager.getScreenHeight(50);
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
        if (scrollPane != null && scrollPanePositionInit < 2 && scrollToLevel != null) {
            scrollPane.setScrollY(scrollToLevel * levelHeight
                    //Title height
                    + getBtnHeightValue() / 2);
            scrollPanePositionInit++;
        }
    }
}
