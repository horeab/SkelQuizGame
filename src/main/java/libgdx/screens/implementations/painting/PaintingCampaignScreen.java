package libgdx.screens.implementations.painting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionConfig;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.paintings.PaintingsCampaignLevelEnum;
import libgdx.implementations.paintings.PaintingsGame;
import libgdx.implementations.paintings.PaintingsQuestionCategoryEnum;
import libgdx.implementations.paintings.PaintingsSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class PaintingCampaignScreen extends AbstractScreen<PaintingsScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public PaintingCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        addTitle(table);
        table.add(createAllTable());
        table.setFillParent(true);
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }


    private void addTitle(Table table) {
        Image titleRaysImage = GraphicUtils.getImage(Resource.title_rays);
        new ActorAnimation(titleRaysImage, this).animateFastFadeInFadeOut();
        float titleWidth = ScreenDimensionsManager.getScreenWidth();
        float titleHeight = ScreenDimensionsManager.getNewHeightForNewWidth(titleWidth, titleRaysImage.getWidth(), titleRaysImage.getHeight());
        titleRaysImage.setWidth(titleWidth);
        titleRaysImage.setHeight(titleHeight);
        titleRaysImage.setY(ScreenDimensionsManager.getScreenHeightValue(49));
        addActor(titleRaysImage);
        Stack titleLabel = createTitleLabel();
        table.add(titleLabel)
                .width(titleWidth)
                .height(titleHeight)
                .row();
    }

    private Stack createTitleLabel() {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(appName).setFontConfig(new FontConfig(
                FontColor.WHITE.getColor(),
                FontColor.DARK_RED.getColor(),
                2f)).setWrappedLineLabel(ScreenDimensionsManager.getScreenWidthValue(40)).build());
        titleLabel.setFontScale(FontManager.calculateMultiplierStandardFontSize(getTitleMultiplier(appName)));
        titleLabel.setAlignment(Align.center);
        return createTitleStack(titleLabel);
    }

    private float getTitleMultiplier(String appName) {
        if (appName.length() > 16) {
            return 1.2f;
        } else if (appName.length() > 13) {
            return 1.6f;
        }
        {
            return 1.9f;
        }
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(PaintingsSpecificResource.title_clouds_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
    }

    private Table createAllTable() {
        Table table = new Table();
        int totalCat = PaintingsQuestionCategoryEnum.values().length;
        long totalStarsWon = 0;
        float btnHeight = ScreenDimensionsManager.getScreenHeightValue(12);
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(80);
        for (int i = 0; i < totalCat; i++) {
            final int finalIndex = i;
            PaintingsQuestionCategoryEnum categoryEnum = PaintingsQuestionCategoryEnum.values()[i];
            MyButton categBtn = new ButtonBuilder()
                    .setWrappedText(new SpecificPropertiesUtils().getQuestionCategoryLabel(categoryEnum.getIndex()), btnWidth / 1.1f)
                    .setButtonSkin(GameButtonSkin.valueOf("PAINTINGS_COLOR_CATEG" + i)).build();
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
                    CampaignLevel campaignLevel = PaintingsCampaignLevelEnum.valueOf("LEVEL_0_" + finalIndex);
                    CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                    QuestionConfig questionConfig = enumService.getQuestionConfig(PaintingGameScreen.TOTAL_QUESTIONS);
                    PaintingsGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
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
        if (campaignService.getFinishedCampaignLevels().size() == PaintingsCampaignLevelEnum.values().length) {
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
