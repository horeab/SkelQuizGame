package libgdx.implementations.screens.implementations.countries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionConfig;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.countries.CountriesCampaignLevelEnum;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesDifficultyLevel;
import libgdx.implementations.countries.CountriesGame;
import libgdx.implementations.countries.CountriesSpecificResource;
import libgdx.implementations.screens.implementations.anatomy.AnatomyGameScreen;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;
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

public class CountriesCampaignScreen extends AbstractScreen<CountriesScreenManager> {

    private CountriesSettingsService settingsService = new CountriesSettingsService();
    private CampaignService campaignService = new CampaignService();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private Table bigCategTable;

    public CountriesCampaignScreen() {
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
        table.add(createAllTable());
        addActor(table);
        if (settingsService.isHighScore()) {
            settingsService.setHighScore(false);
            ActorAnimation.animateImageCenterScreenFadeOut(CountriesSpecificResource.star, 2f);
        }
        SoundUtils.addSoundTable(getAbstractScreen(), null);
        Table smallTable = getRoot().findActor(getSmallCategTableName(settingsService.getSelectedLevel().getIndex()));
        smallTable.setBackground(getSmallTableBackground());
    }

    private Drawable getSmallTableBackground() {
        return GraphicUtils.getColorBackground(FontColor.LIGHT_BLUE.getColor());
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(CountriesSpecificResource.title_clouds_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
    }

    private Table createAllTable() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(CountriesSpecificResource.moving_background);
        Table table = new Table();
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 16 ? 2 : 2.5f;
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * mult,
                        4f))
                .setText(appName).build());

        float dimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createTitleStack(titleLabel)).height(ScreenDimensionsManager.getScreenHeightValue(20)).padTop(dimen * 2).row();
        long totalStarsWon = 0;
        table.add(createCategTable()).expand();

        if (campaignService.getFinishedCampaignLevels().size() == CountriesCampaignLevelEnum.values().length) {
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

    private Table createCategTable() {
        Table table = new Table();
        CampaignLevel selectedLevel = settingsService.getSelectedLevel();
        Table categButton = createBigCategButton(selectedLevel);
        table.add(categButton).fill();
        table.add(createSmallCategButtons()).padLeft(MainDimen.horizontal_general_margin.getDimen());
        return table;
    }

    private Table createSmallCategButtons() {
        Table table = new Table();
        addButtonToTable(table, CountriesCampaignLevelEnum.LEVEL_0_0);
        addButtonToTable(table, CountriesCampaignLevelEnum.LEVEL_0_1);
        addButtonToTable(table, CountriesCampaignLevelEnum.LEVEL_0_2);
        addButtonToTable(table, CountriesCampaignLevelEnum.LEVEL_0_3);
        addButtonToTable(table, CountriesCampaignLevelEnum.LEVEL_0_5);
        addButtonToTable(table, CountriesCampaignLevelEnum.LEVEL_0_4);
        return table;
    }

    private void addButtonToTable(Table table, CountriesCampaignLevelEnum campaignLevel) {
        MyButton categButton = createSmallCategButton(campaignLevel);
        Table btnTable = new Table();
        btnTable.setName(getSmallCategTableName(campaignLevel.getIndex()));
        btnTable.add(categButton).width(categButton.getWidth()).height(categButton.getHeight());
        table.add(btnTable).width(categButton.getWidth()).height(categButton.getHeight()).
                padBottom(MainDimen.vertical_general_margin.getDimen()).row();
    }

    private void resetSmallCategTableBackgr() {
        for (CountriesCampaignLevelEnum campaignLevelEnum : CountriesCampaignLevelEnum.values()) {
            Table table = getRoot().findActor(getSmallCategTableName(campaignLevelEnum.getIndex()));
            table.setBackground(GraphicUtils.getNinePatch(MainResource.transparent_background));
        }
    }

    private String getSmallCategTableName(int index) {
        return "smallCategTable" + index;
    }

    private MyButton createSmallCategButton(final CampaignLevel campaignLevel) {
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("COUNTRIES_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .setFontScale(FontManager.getSmallFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.COUNTRIES_SMALL_MENU_BUTTON)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bigCategTable.clear();
                bigCategTable.setVisible(false);
                createBigCategButton(campaignLevel);
                Utils.fadeInActor(bigCategTable, 0.6f);
                resetSmallCategTableBackgr();
                Table table = getRoot().findActor(getSmallCategTableName(campaignLevel.getIndex()));
                table.setBackground(getSmallTableBackground());

            }
        });
        return categBtn;
    }

    private Table createBigCategButton(final CampaignLevel campaignLevel) {
        CountriesCategoryEnum categoryEnum = (CountriesCategoryEnum) CampaignLevelEnumService.getCategoryEnum(campaignLevel.getName());
        if (bigCategTable == null) {
            bigCategTable = new Table();
        }
        String labelText = new CampaignLevelEnumService(campaignLevel).getLabelText();
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("COUNTRIES_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .textButtonWidth(GameButtonSize.COUNTRIES_BIG_MENU_BUTTON.getWidth() * 1.6f)
                .setFontScale(FontManager.getNormalFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.COUNTRIES_BIG_MENU_BUTTON)
                .setText(StringUtils.capitalize(labelText))
                .build();
        categBtn.setDisabled(true);
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                QuestionConfig questionConfig = enumService.getQuestionConfig(AnatomyGameScreen.TOTAL_QUESTIONS);
                CountriesGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
            }
        });
        categBtn.setVisible(false);
        Utils.fadeInActor(categBtn, 0.6f);
        MainDimen vertical_general_margin = MainDimen.vertical_general_margin;
        float dimen = vertical_general_margin.getDimen();
        bigCategTable.setWidth(categBtn.getWidth());
        bigCategTable.setHeight(categBtn.getHeight());
        bigCategTable.add(createAchTable(campaignLevel)).width(scoreWidth()).height(dimen * 4).row();
        bigCategTable.add(categBtn).padTop(dimen * 3).width(categBtn.getWidth()).height(categBtn.getHeight()).row();
        bigCategTable.setBackground(GraphicUtils.getNinePatch(CountriesSpecificResource.valueOf("level_0_" + campaignLevel.getIndex() + "_backgr")));
        MyButton startGameBtn = new ImageButtonBuilder(GameButtonSkin.COUNTRIES_STARTGAME, getAbstractScreen())
                .animateZoomInZoomOut()
                .setFixedButtonSize(GameButtonSize.COUNTRIES_START_GAME_BUTTON)
                .build();
        startGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Question question = new Question(new Random().nextInt(), CountriesDifficultyLevel._0, categoryEnum, "");
                GameContext gameContext = new GameContextService().createGameContext(Collections.singletonList(question));
                screenManager.showCampaignGameScreen(gameContext, campaignLevel);
                settingsService.setSelectedLevel(campaignLevel);
            }
        });
        bigCategTable.add(startGameBtn).padTop(dimen * 6).width(startGameBtn.getWidth()).height(startGameBtn.getHeight());
        bigCategTable.add().growY();
        return bigCategTable;
    }

    private float scoreWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(70);
    }

    private Table createAchTable(CampaignLevel campaignLevel) {
        Table table = new Table();
        Table achTable = new Table();
        long scoreWon = campaignStoreService.getScoreWon(campaignLevel);
        String prefix = scoreWon > 0 ? "+" : "";
        float width = scoreWidth();
        MyWrappedLabel foundC = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(width / 2)
                .setFontConfig(new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 0.9f))
                .setText(MainGameLabel.l_highscore.getText("")).build());
        MyWrappedLabel score = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(width / 3)
                .setFontConfig(new FontConfig(FontColor.LIGHT_GREEN.getColor(), FontColor.GREEN.getColor(), FontConfig.FONT_SIZE * 1.2f, 3f))
                .setText(prefix + scoreWon).build());
        score.setTransform(true);
        float scaleFactor = 0.3f;
        float duration = 0.2f;
        foundC.setAlignment(Align.right);
        score.setAlignment(Align.left);
        score.addAction(Actions.sequence(Actions.scaleBy(scaleFactor, scaleFactor, duration),
                Actions.scaleBy(-scaleFactor, -scaleFactor, duration)));
        achTable.add(foundC);
        achTable.add(score);
        table.add(achTable);
        table.setWidth(width);
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
