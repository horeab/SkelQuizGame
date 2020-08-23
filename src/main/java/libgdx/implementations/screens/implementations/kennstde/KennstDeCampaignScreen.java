package libgdx.implementations.screens.implementations.kennstde;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.kennstde.KennstDeCampaignLevelEnum;
import libgdx.implementations.kennstde.KennstDeGame;
import libgdx.implementations.kennstde.KennstDeQuestionCategoryEnum;
import libgdx.implementations.kennstde.KennstDeQuestionDifficultyLevel;
import libgdx.implementations.kennstde.KennstDeSpecificResource;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.hangman.HangmanScreenManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KennstDeCampaignScreen extends AbstractScreen<HangmanScreenManager> {

    public static int TOTAL_QUESTIONS = 6;
    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public KennstDeCampaignScreen() {
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
        table.add(createAllTable()).width(ScreenDimensionsManager.getScreenWidth());
        table.setBackground(GraphicUtils.getNinePatch(KennstDeSpecificResource.campaign_background_texture));
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Table createAllTable() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(
                FontColor.WHITE.getColor(),
                FontColor.BLACK.getColor(),
                FontConfig.FONT_SIZE * 2,
                4f)).setText(Game.getInstance().getAppInfoService().getAppName()).build());
        Stack stack = new Stack();
        Image titleBackr = GraphicUtils.getImage(KennstDeSpecificResource.title_background);
        stack.addActor(titleBackr);
        stack.addActor(titleLabel);
        table.add(stack)
                .padBottom(-verticalGeneralMarginDimen * 10)
                .width(ScreenDimensionsManager.getScreenWidthValue(90))
                .height(ScreenDimensionsManager.getNewHeightForNewWidth(ScreenDimensionsManager.getScreenWidthValue(90), titleBackr)).row();

        table.add(createCampaignButtons()).padTop(ScreenDimensionsManager.getScreenHeightValue(20));
        if (campaignService.getFinishedCampaignLevels().size() == KennstDeCampaignLevelEnum.values().length) {
            new LevelFinishedPopup(this, SkelGameLabel.game_finished.getText()).addToPopupManager();
        }
        return table;
    }

    private Table createCampaignButtons() {
        Table table = new Table();
        int totalLevels = KennstDeCampaignLevelEnum.values().length;
        for (int i = 0; i < totalLevels; i++) {
            Integer category = CampaignLevelEnumService.getCategory(KennstDeCampaignLevelEnum.values()[i].name());
            float rowHeight = ScreenDimensionsManager.getScreenHeightValue(15);
            if (i % 2 == 0) {
                table.add(createCampaignBtn(category)).width(ScreenDimensionsManager.getScreenWidth() / 2).height(rowHeight);
                table.add().width(ScreenDimensionsManager.getScreenWidth() / 2);
            } else {
                table.add();
                table.add(createCampaignBtn(category)).height(rowHeight);
            }
            table.row();
        }
        return table;
    }

    private Table createCampaignBtn(final int cat) {
        Table table = new Table();
        boolean btnUnlocked = false;
        boolean isStar = false;
        boolean isBtnCurrent = false;
        for (CampaignStoreLevel campaignStoreLevel : allCampaignLevelStores) {
            if (cat == CampaignLevelEnumService.getCategory(campaignStoreLevel.getName())) {
                if (campaignStoreLevel.getStatus() == CampaignLevelStatusEnum.IN_PROGRESS.getStatus()) {
                    isBtnCurrent = true;
                }
                if (campaignStoreLevel.getScore() == QuizStarsService.NR_OF_STARS_TO_DISPLAY) {
                    isStar = true;
                }
                btnUnlocked = true;
                break;
            }
        }
        if (btnUnlocked) {
            String label = new SpecificPropertiesUtils().getQuestionCategoryLabel(cat);
            MyButton btn = new ButtonBuilder().setText(label).build();
            float width = new GlyphLayout(Game.getInstance().getFontManager().getFont(), label).width + MainDimen.horizontal_general_margin.getDimen() * 2;
            float height = MainDimen.vertical_general_margin.getDimen() * 1.5f;
            Table btnTable = new Table();
            btnTable.setHeight(height);
            btnTable.setWidth(width);
            btnTable.setOrigin(Align.center);
            if (isBtnCurrent) {
                btnTable.setTransform(true);
                new ActorAnimation(btnTable, this).animateZoomInZoomOut(0.5f);
            }
            btnTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            btnTable.add(btn).width(width).height(height);
            table.add(btnTable);
            if (isStar) {
                float starDimen = MainDimen.horizontal_general_margin.getDimen() * 6;
                table.add(GraphicUtils.getImage(KennstDeSpecificResource.star)).width(starDimen).height(starDimen);
            }
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    CampaignLevel campaignLevel = CampaignLevelEnumService.getCampaignLevelForDiffAndCat(KennstDeQuestionDifficultyLevel._0, KennstDeQuestionCategoryEnum.valueOf("cat" + cat));
                    KennstDeGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(new CampaignLevelEnumService(campaignLevel).getQuestionConfig(TOTAL_QUESTIONS)), campaignLevel);
                }
            });
        }
        return table;
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
