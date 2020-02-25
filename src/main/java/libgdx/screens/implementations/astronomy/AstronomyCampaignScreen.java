package libgdx.screens.implementations.astronomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ButtonWithIconBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.labelimage.LabelImageConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.anatomy.AnatomyGame;
import libgdx.implementations.astronomy.AstronomyCampaignLevelEnum;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.conthistory.ConthistoryCampaignLevelEnum;
import libgdx.implementations.conthistory.ConthistoryCategoryEnum;
import libgdx.implementations.conthistory.ConthistoryGame;
import libgdx.implementations.conthistory.ConthistorySpecificResource;
import libgdx.implementations.skelgame.*;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.anatomy.AnatomyGameScreen;
import libgdx.utils.ScreenDimensionsManager;
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
        table.add(createTitleStack(titleLabel)).height(btnHeight / 2).padTop(dimen * 2).row();
        long totalStarsWon = 0;
        addCategButtons(table);

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

    private void addCategButtons(Table table) {
        table.row();
        addButtonToTable(table, AstronomyCampaignLevelEnum.LEVEL_0_0);
        table.row();
        Table btnTable1 = new Table();
        addButtonToTable(btnTable1, AstronomyCampaignLevelEnum.LEVEL_0_1);
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(40));
        addButtonToTable(btnTable1, AstronomyCampaignLevelEnum.LEVEL_0_2);
        table.add(btnTable1);
        table.row();
        Table btnTable2 = new Table();
        addButtonToTable(btnTable2, AstronomyCampaignLevelEnum.LEVEL_0_3);
        btnTable2.add().width(ScreenDimensionsManager.getScreenWidthValue(15));
        addButtonToTable(btnTable2, AstronomyCampaignLevelEnum.LEVEL_0_4);
        table.add(btnTable2).padBottom(MainDimen.vertical_general_margin.getDimen() * 2);
    }

    private Cell<MyButton> addButtonToTable(Table table, AstronomyCampaignLevelEnum campaignLevel) {
        MyButton categButton = createCategButton(campaignLevel);
        return table.add(categButton).width(categButton.getWidth()).height(categButton.getHeight())
                .padBottom(MainDimen.vertical_general_margin.getDimen() * 5f);
    }

    private MyButton createCategButton(final CampaignLevel campaignLevel) {
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("ASTRONOMY_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .animateZoomInZoomOut()
                .setAnimateZoomInZoomOutAmount(0.05f)
                .setFontScale(FontManager.getSmallFontDim())
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(new CampaignLevelEnumService(campaignLevel).getLabelText())
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                QuestionConfig questionConfig = enumService.getQuestionConfig(AnatomyGameScreen.TOTAL_QUESTIONS);
                AstronomyGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
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
