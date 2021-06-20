package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Arrays;

import libgdx.campaign.CampaignLevelEnumService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class HistoryCampaignScreen extends AbstractScreen<HistoryScreenManager> {

    private HistoryPreferencesService historyPreferencesService;

    public HistoryCampaignScreen() {
        historyPreferencesService = new HistoryPreferencesService();
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

    private Table createAllTable() {
        Table table = new Table();
        String appName = Game.getInstance().getAppInfoService().getAppName();
        boolean titleContainsLongText = false;
        float smallScale = 1.9f;
        for (String t : appName.split(" ")) {
            if (t.length() >= 15) {
                smallScale = 1.5f;
            }
            if (t.length() >= 10) {
                titleContainsLongText = true;
                break;
            }
        }
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(ScreenDimensionsManager.getScreenWidth(68))
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_GREEN.getColor(),
                        FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * (titleContainsLongText ? smallScale : 2.4f),
                        4f))
                .setText(appName).build());
        float dimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createTitleStack(titleLabel)).height(dimen * 16).padTop(dimen * 2).row();
        addCategButtons(table);
        return table;
    }

    private void addCategButtons(Table table) {
        table.row();
        Table btnTable0 = new Table();
        addButtonToTable(btnTable0, HistoryCampaignLevelEnum.LEVEL_0_0, GameButtonSkin.HISTORY_CATEG0);
        btnTable0.row();
        addButtonToTable(btnTable0, HistoryCampaignLevelEnum.LEVEL_0_1, GameButtonSkin.HISTORY_CATEG1);
        table.add(btnTable0);
    }

    private Cell<MyButton> addButtonToTable(Table table, HistoryCampaignLevelEnum campaignLevel, ButtonSkin buttonSkin) {
        MyButton categButton = createCategButton(campaignLevel, buttonSkin);
        return table.add(categButton).width(getBtnWidthValue()).height(getBtnHeightValue())
                .padBottom(MainDimen.vertical_general_margin.getDimen() * 3f);
    }

    private float getBtnHeightValue() {
        return ScreenDimensionsManager.getScreenHeight(26);
    }

    private float getBtnWidthValue() {
        return ScreenDimensionsManager.getScreenWidth(80);
    }

    private MyButton createCategButton(final HistoryCampaignLevelEnum campaignLevel, ButtonSkin buttonSkin) {
        String labelText = new CampaignLevelEnumService(campaignLevel).getLabelText();
        MyButton categBtn = new ButtonBuilder()
                .setButtonSkin(buttonSkin)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.5f))
                .setFontColor(FontColor.BLACK)
                .setWrappedText(labelText, getBtnWidthValue() / 1.2f)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToLevel(campaignLevel, historyPreferencesService, screenManager);
            }
        });
        Table highScoreTable = new Table();
        float width = getBtnWidthValue() / 1.1f;
        MyWrappedLabel highScoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.YELLOW.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1.3f,
                        3f))
                .setWrappedLineLabel(width)
                .setText(MainGameLabel.l_highscore.getText(historyPreferencesService.getHighScore(campaignLevel))).build());
        categBtn.getCenterRow().row();
        categBtn.add(highScoreTable).width(getBtnWidthValue()).padBottom(MainDimen.vertical_general_margin.getDimen() * 2);
        Table scoreImg = new Table();
        scoreImg.add(highScoreLabel).width(width);
        highScoreTable.add(scoreImg).width(width);
        return categBtn;
    }

    public static void goToLevel(HistoryCampaignLevelEnum campaignLevel, HistoryPreferencesService historyPreferencesService, HistoryScreenManager screenManager) {
        QuestionCreator questionCreator = new QuestionCreator();
        GameContext gameContext = new GameContextService().createGameContext(questionCreator.getAllQuestions(Arrays.asList(HistoryDifficultyLevel.values()),
                new CampaignLevelEnumService(campaignLevel).getCategoryEnum()));
        historyPreferencesService.clearLevelsPlayed(campaignLevel);
        screenManager.showCampaignGameScreen(gameContext);
    }

    private Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(AstronomySpecificResource.title_clouds_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
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
