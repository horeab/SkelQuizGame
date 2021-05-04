package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.labelimage.InAppPurchaseTable;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomyCampaignLevelEnum;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.screens.implementations.anatomy.AnatomyGameScreen;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import java.util.List;

public class AstronomyCampaignScreen extends AbstractScreen<AstronomyScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public AstronomyCampaignScreen() {
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
        table.add(createAllTable()).expand();
        addActor(table);
        SoundUtils.addSoundTable(getAbstractScreen(), null);
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(AstronomySpecificResource.title_clouds_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
    }

    private Table createAllTable() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
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
        table.add(createTitleStack(titleLabel)).height(btnHeight / 2).padTop(dimen * 9).row();
        long totalStarsWon = 0;
        table.row();
        table.add(addCategButtons()).height(ScreenDimensionsManager.getScreenHeightValue(80));

        if (campaignService.getFinishedCampaignLevels().size() == AstronomyCampaignLevelEnum.values().length) {
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

    private Table addCategButtons() {
        Table table = new Table();
        Table btnTable0 = new Table();
        addButtonToTable(btnTable0, AstronomyCampaignLevelEnum.LEVEL_0_0, AstronomyGameType.SOLAR_SYSTEM);
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
        addButtonToTable(btnTable0, AstronomyCampaignLevelEnum.LEVEL_0_1, AstronomyGameType.ASTRONOMY_QUIZ);
        table.add(btnTable0).padBottom(ScreenDimensionsManager.getScreenHeightValue(5));
        table.row();
        Table btnTable1 = new Table();
        addButtonToTable(btnTable1, AstronomyCampaignLevelEnum.LEVEL_0_2, AstronomyGameType.ASTRONOMY_IMAGES_QUIZ);
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(20));
        addButtonToTable(btnTable1, AstronomyCampaignLevelEnum.LEVEL_0_3, AstronomyGameType.FIND_PLANET);
        table.add(btnTable1).padBottom(ScreenDimensionsManager.getScreenHeightValue(20));
        InAppPurchaseTable inAppPurchaseTable = new InAppPurchaseTable();
//        if (!Utils.isValidExtraContent()) {
//            btnTable2 = inAppPurchaseTable.createForProVersion(btnTable2, false);
//            btnTable2.setWidth(ScreenDimensionsManager.getScreenWidth());
//        }
        return table;

    }

    private Cell<MyButton> addButtonToTable(Table table, AstronomyCampaignLevelEnum campaignLevel, AstronomyGameType astronomyGameType) {
        MyButton categButton = createCategButton(campaignLevel, astronomyGameType);
        return table.add(categButton).width(categButton.getWidth()).height(categButton.getHeight())
                .padBottom(MainDimen.vertical_general_margin.getDimen() * 5f);
    }

    private MyButton createCategButton(final CampaignLevel campaignLevel, AstronomyGameType astronomyGameType) {
        String labelText = new CampaignLevelEnumService(campaignLevel).getLabelText();
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("ASTRONOMY_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .setFontScale(FontManager.getSmallFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(labelText)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (astronomyGameType == AstronomyGameType.FIND_PLANET) {
                    CampaignLevel planetsCampaignLevel = AstronomyCampaignLevelEnum.LEVEL_0_0;
                    CampaignLevelEnumService enumService = new CampaignLevelEnumService(planetsCampaignLevel);
                    QuestionConfig questionConfig = enumService.getQuestionConfig(AnatomyGameScreen.TOTAL_QUESTIONS);
                    getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), planetsCampaignLevel, astronomyGameType);
                } else {
                    getScreenManager().showDetailedCampaignScreen(astronomyGameType);
                }
            }
        });
        return categBtn;
    }

    private float getBtnHeightValue() {
        return ScreenDimensionsManager.getScreenHeightValue(50);
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
