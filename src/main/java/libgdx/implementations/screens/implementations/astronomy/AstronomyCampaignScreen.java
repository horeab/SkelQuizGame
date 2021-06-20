package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomyCampaignLevelEnum;
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.astronomy.spec.AstronomyPreferencesManager;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.astronomy.spec.PlanetsUtil;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SettingsUtils;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import java.util.ArrayList;
import java.util.List;

public class AstronomyCampaignScreen extends AbstractScreen<AstronomyScreenManager> {

    public static final int NR_QUESTIONS_FIND_PLANET = 10;
    private CampaignService campaignService = new CampaignService();
    private AstronomyPreferencesManager astronomyPreferencesManager = new AstronomyPreferencesManager();
    public static final int TOTAL_STARS_TO_DISPLAY = 3;

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
        SettingsUtils settingsUtils = new SettingsUtils();
        settingsUtils.setSoundMusicButtonsTable(SoundUtils.createSoundMusicButtonsTable(AstronomySpecificResource.background_music));
        settingsUtils.addSettingsTable(getAbstractScreen());
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(AstronomySpecificResource.title_clouds_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        stack.setWidth(ScreenDimensionsManager.getScreenWidth());
        return stack;
    }

    private Table createAllTable() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        Table table = new Table();
        String appName = Game.getInstance().getAppInfoService().getAppName();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_GREEN.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * (appName.length() >= 10 ? 2 : 2.4f),
                        4f))
                .setText(appName).build());

        float btnHeight = getBtnHeightValue();
        float dimen = MainDimen.vertical_general_margin.getDimen();
        Stack titleStack = createTitleStack(titleLabel);
        table.add(titleStack).height(btnHeight / 2).width(titleStack.getWidth()).padTop(dimen * 3).row();
        long totalStarsWon = 0;
        table.row();
        table.add(addCategButtons()).height(ScreenDimensionsManager.getScreenHeight(80));

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

    private int getStarsBasedOnTotalLevels(int currentMaxLevels, int totalLevels) {
        if (currentMaxLevels >= totalLevels) {
            return TOTAL_STARS_TO_DISPLAY;
        } else if (currentMaxLevels * 2 > totalLevels) {
            return TOTAL_STARS_TO_DISPLAY - 1;
        } else {
            return 0;
        }
    }

    private int getMaxLevelsForCampaignList(List<AstronomyCampaignLevelEnum> campaignLevelEnums, int totalQuestions) {
        int maxLevels = 0;
        for (AstronomyCampaignLevelEnum campaignLevelEnum : campaignLevelEnums) {
            if (astronomyPreferencesManager
                    .getLevelScore((AstronomyCategoryEnum) CampaignLevelEnumService.getCategoryEnum(campaignLevelEnum.getName()))
                    >= totalQuestions) {
                maxLevels++;
            }
        }
        return maxLevels;
    }

    private Table addCategButtons() {
        Table table = new Table();
        Table btnTable0 = new Table();
        addButtonToTable(btnTable0, AstronomyGameType.SOLAR_SYSTEM);
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidth(20));
        addButtonToTable(btnTable0, AstronomyGameType.ASTRONOMY_QUIZ);
        table.add(btnTable0).padBottom(ScreenDimensionsManager.getScreenHeight(5));
        table.row();
        Table btnTable1 = new Table();
        addButtonToTable(btnTable1, AstronomyGameType.ASTRONOMY_IMAGES_QUIZ);
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidth(20));
        addButtonToTable(btnTable1, AstronomyGameType.FIND_PLANET);
        table.add(btnTable1).padBottom(ScreenDimensionsManager.getScreenHeight(20));
        return table;

    }

    private void addButtonToTable(Table table, AstronomyGameType astronomyGameType) {
        MyButton categButton = createCategButton(astronomyGameType);
        Table btnTable = new Table();
        btnTable.add(createStarsTable(astronomyGameType));
        btnTable.row();
        btnTable.add(categButton).colspan(3).width(categButton.getWidth()).height(categButton.getHeight());

        table.add(btnTable).padBottom(MainDimen.vertical_general_margin.getDimen() * 5f);
    }

    private Table createStarsTable(AstronomyGameType astronomyGameType) {
        int levelStars = getLevelNrOfStars(astronomyGameType);
        Table table = new Table();
        float sideDimen = ScreenDimensionsManager.getScreenWidth(7);
        if (levelStars == TOTAL_STARS_TO_DISPLAY) {
            addStarToTable(TOTAL_STARS_TO_DISPLAY, table, sideDimen, AstronomySpecificResource.star_green);
        } else {
            addStarToTable(levelStars, table, sideDimen, AstronomySpecificResource.star);
            addStarToTable(TOTAL_STARS_TO_DISPLAY - levelStars, table, sideDimen, AstronomySpecificResource.star_disabled);
        }
        return table;
    }

    private void addStarToTable(int to, Table table, float sideDimen, AstronomySpecificResource res) {
        float horiz = MainDimen.horizontal_general_margin.getDimen();
        float vert = MainDimen.vertical_general_margin.getDimen() / 2;
        for (int i = 0; i < to; i++) {
            table.add(GraphicUtils.getImage(res)).width(sideDimen).height(sideDimen)
                    .padRight(horiz)
                    .padBottom(vert);
        }
    }

    private int getLevelNrOfStars(AstronomyGameType astronomyGameType) {
        if (astronomyGameType == AstronomyGameType.ASTRONOMY_QUIZ) {
            List<AstronomyCampaignLevelEnum> list = new ArrayList<>(AstronomyDetailedCampaignScreen.ASTRONOMY_QUIZ_CAMPAIGN_LEVELS.keySet());
            int maxLevels = getMaxLevelsForCampaignList(list, AstronomyGameScreen.TOTAL_QUESTIONS);
            return getStarsBasedOnTotalLevels(maxLevels, list.size());
        } else if (astronomyGameType == AstronomyGameType.ASTRONOMY_IMAGES_QUIZ) {
            List<AstronomyCampaignLevelEnum> list = new ArrayList<>(AstronomyDetailedCampaignScreen.ASTRONOMY_IMAGES_QUIZ_CAMPAIGN_LEVELS.keySet());
            int maxLevels = getMaxLevelsForCampaignList(list, AstronomyGameScreen.TOTAL_QUESTIONS);
            return getStarsBasedOnTotalLevels(maxLevels, list.size());
        } else if (astronomyGameType == AstronomyGameType.FIND_PLANET) {
            int maxLevels = astronomyPreferencesManager
                    .getLevelScore((AstronomyCategoryEnum) CampaignLevelEnumService.getCategoryEnum(AstronomyCampaignLevelEnum.LEVEL_0_0.getName()));
            return getStarsBasedOnTotalLevels(maxLevels, NR_QUESTIONS_FIND_PLANET);
        } else if (astronomyGameType == AstronomyGameType.SOLAR_SYSTEM) {
            List<Planet> allPlanets = PlanetsUtil.getAllPlanets();
            int maxLevels = 0;
            AstronomyPlanetsGameType[] values = AstronomyPlanetsGameType.values();
            for (AstronomyPlanetsGameType planetsGameType : values) {
                if (astronomyPreferencesManager.getLevelScore(planetsGameType)
                        >= PlanetsUtil.getAllAvailableLevelsToPlay(allPlanets, planetsGameType).size()) {
                    maxLevels++;
                }
            }
            return getStarsBasedOnTotalLevels(maxLevels, values.length);
        }
        return 0;
    }

    private MyButton createCategButton(AstronomyGameType astronomyGameType) {
        String labelText = SpecificPropertiesUtils.getText(astronomyGameType.levelName);
        MyButton categBtn = new ImageButtonBuilder(astronomyGameType.buttonSkin, getAbstractScreen())
                .textButtonWidth(GameButtonSize.ASTRONOMY_MENU_BUTTON.getWidth() * 1.4f)
                .padBetweenImageAndText(1.3f)
                .setFontScale(FontManager.getNormalFontDim())
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
                    QuestionConfig questionConfig = enumService.getQuestionConfig(NR_QUESTIONS_FIND_PLANET);
                    getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), planetsCampaignLevel, astronomyGameType);
                } else {
                    getScreenManager().showDetailedCampaignScreen(astronomyGameType);
                }
            }
        });

        return categBtn;
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
    }
}
