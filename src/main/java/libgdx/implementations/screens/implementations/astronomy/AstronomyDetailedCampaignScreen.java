package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.QuestionConfig;
import libgdx.constants.Language;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.labelimage.InAppPurchaseTable;
import libgdx.implementations.astronomy.AstronomyCampaignLevelEnum;
import libgdx.implementations.astronomy.AstronomyCategoryEnum;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.astronomy.spec.AstronomyPreferencesManager;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.astronomy.spec.PlanetsUtil;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class AstronomyDetailedCampaignScreen extends AbstractScreen<AstronomyScreenManager> {

    public static final Map<AstronomyCampaignLevelEnum, Boolean> ASTRONOMY_QUIZ_CAMPAIGN_LEVELS;
    public static final Map<AstronomyCampaignLevelEnum, Boolean> ASTRONOMY_IMAGES_QUIZ_CAMPAIGN_LEVELS;

    static {
        Map<AstronomyCampaignLevelEnum, Boolean> map1 = new LinkedHashMap<>();
        map1.put(AstronomyCampaignLevelEnum.LEVEL_0_1, false);
        map1.put(AstronomyCampaignLevelEnum.LEVEL_0_2, false);
        map1.put(AstronomyCampaignLevelEnum.LEVEL_0_3, true);
        map1.put(AstronomyCampaignLevelEnum.LEVEL_0_5, true);
        ASTRONOMY_QUIZ_CAMPAIGN_LEVELS = Collections.unmodifiableMap(map1);

        Map<AstronomyCampaignLevelEnum, Boolean> map2 = new LinkedHashMap<>();
        map2.put(AstronomyCampaignLevelEnum.LEVEL_0_4, false);
        map2.put(AstronomyCampaignLevelEnum.LEVEL_0_6, false);
        map2.put(AstronomyCampaignLevelEnum.LEVEL_0_7, true);
        ASTRONOMY_IMAGES_QUIZ_CAMPAIGN_LEVELS = Collections.unmodifiableMap(map2);
    }

    private AstronomyGameType astronomyGameType;
    private AstronomyPreferencesManager astronomyPreferencesManager = new AstronomyPreferencesManager();

    public AstronomyDetailedCampaignScreen(AstronomyGameType astronomyGameType) {
        this.astronomyGameType = astronomyGameType;
    }


    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Table createAllTable() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        Table table = new Table();
        float levelTableHeight = ScreenDimensionsManager.getScreenHeight(90);
        table.add(createTitleLabel())
                .height(ScreenDimensionsManager.getScreenHeight() - levelTableHeight / 1.05f)
                .row();
        table.add(createLevelsTable()).height(levelTableHeight)
                .width(ScreenDimensionsManager.getScreenWidth());
        return table;
    }

    private Table createLevelsTable() {
        Table allButtonTable = new Table();
        if (astronomyGameType == AstronomyGameType.SOLAR_SYSTEM) {
            List<Planet> allPlanets = PlanetsUtil.getAllPlanets();
            int i = 0;
            for (AstronomyPlanetsGameType planetsGameType : AstronomyPlanetsGameType.values()) {
                if (i > 0 && i % 2 == 0) {
                    allButtonTable.row();
                }
                Table tableWithStartGameBtn = createPlanetGameTypeGameButton(planetsGameType, i, allPlanets);
                addStartGameToTable(allButtonTable, tableWithStartGameBtn, 1);
                i++;
            }
        } else {
            List<AstronomyCampaignLevelEnum> campaignLevelEnums = new ArrayList<>();
            if (astronomyGameType == AstronomyGameType.ASTRONOMY_QUIZ) {
                campaignLevelEnums = new ArrayList<>(ASTRONOMY_QUIZ_CAMPAIGN_LEVELS.keySet());
            } else if (astronomyGameType == AstronomyGameType.ASTRONOMY_IMAGES_QUIZ) {
                campaignLevelEnums = new ArrayList<>(ASTRONOMY_IMAGES_QUIZ_CAMPAIGN_LEVELS.keySet());
            }
            int i = 0;
            for (AstronomyCampaignLevelEnum campaignLevelEnum : campaignLevelEnums) {
                int colspan = 1;
                if (i > 0 && i % 2 == 0) {
                    allButtonTable.row();
                    if (campaignLevelEnums.size() == 3) {
                        colspan = 2;
                    }
                }
                addStartGameToTable(allButtonTable, createQuizGameButton(campaignLevelEnum), colspan);
                i++;
            }
        }
        return allButtonTable;
    }

    private Table createQuizGameButton(final CampaignLevel campaignLevel) {
        boolean isExtraContent = !Utils.isValidExtraContent()
                &&
                (ASTRONOMY_QUIZ_CAMPAIGN_LEVELS.containsKey(campaignLevel) && ASTRONOMY_QUIZ_CAMPAIGN_LEVELS.get(campaignLevel)
                        ||
                        ASTRONOMY_IMAGES_QUIZ_CAMPAIGN_LEVELS.containsKey(campaignLevel) && ASTRONOMY_IMAGES_QUIZ_CAMPAIGN_LEVELS.get(campaignLevel)
                );
        String labelText = new CampaignLevelEnumService(campaignLevel).getLabelText();
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("ASTRONOMY_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .textButtonWidth(GameButtonSize.ASTRONOMY_MENU_BUTTON.getWidth() * 1.3f)
                .padBetweenImageAndText(getPadBetweenImageAndText(isExtraContent))
                .setFontScale(FontManager.getNormalFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(labelText)
                .build();
        CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
        MyWrappedLabel levelScoreLabel = createLevelScoreLabel(AstronomyGameScreen.TOTAL_QUESTIONS, astronomyPreferencesManager.getLevelScore((AstronomyCategoryEnum) enumService.getCategoryEnum()));
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                QuestionConfig questionConfig = enumService.getQuestionConfig(AstronomyGameScreen.TOTAL_QUESTIONS);
                AstronomyGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel, astronomyGameType);
            }
        });
        Table tableWithBtnAndScore = createTableWithBtnAndScore(categBtn, levelScoreLabel);
        InAppPurchaseTable inAppPurchaseTable = new InAppPurchaseTable();
        if (isExtraContent) {
            tableWithBtnAndScore = inAppPurchaseTable.create(tableWithBtnAndScore,
                    Language.en.name(), "Unlock extra questions \n+ Remove Ads", GameButtonSize.ASTRONOMY_MENU_BUTTON.getWidth() * 1.5f);
        }
        return tableWithBtnAndScore;
    }

    private float getPadBetweenImageAndText(boolean isExtraContent) {
        return isExtraContent ? 0.4f : 1f;
    }

    private void addStartGameToTable(Table allButtonTable, Table planetGameTypeGameButton, int colspan) {
        float planetsGameSideDimen = getStartGameButtonSideDimen();
        float pad = planetsGameSideDimen / 3f;
        allButtonTable.add(planetGameTypeGameButton)
                .colspan(colspan)
                .width(planetsGameSideDimen).height(planetsGameSideDimen)
                .padLeft(pad)
                .padRight(pad)
                .padBottom(pad * 3);
    }

    private float getStartGameButtonSideDimen() {
        return ScreenDimensionsManager.getScreenHeight(15);
    }

    private Table createPlanetGameTypeGameButton(final AstronomyPlanetsGameType planetsGameType, int index, List<Planet> allPlanets) {
        AstronomyPlanetsGameType[] values = AstronomyPlanetsGameType.values();
        int indexOf = Arrays.asList(values).indexOf(planetsGameType);
        int length = values.length;
        boolean isExtraContent = !Utils.isValidExtraContent()
                && (indexOf == length - 2 || indexOf == length - 1);
        MyButton categBtn = createGameBtn(planetsGameType.levelName, GameButtonSkin.valueOf("ASTRONOMY_PLANET_GAME_" + index), isExtraContent);
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getScreenManager().showCampaignDragDropGameScreen(planetsGameType, allPlanets);
            }
        });
        MyWrappedLabel levelScoreLabel = createLevelScoreLabel(PlanetsUtil.getAllAvailableLevelsToPlay(allPlanets, planetsGameType).size(), astronomyPreferencesManager.getLevelScore(planetsGameType));
        Table tableWithBtnAndScore = createTableWithBtnAndScore(categBtn, levelScoreLabel);
        InAppPurchaseTable inAppPurchaseTable = new InAppPurchaseTable();
        if (isExtraContent) {
            tableWithBtnAndScore = inAppPurchaseTable.create(tableWithBtnAndScore,
                    Language.en.name(), "Unlock extra questions \n+ Remove Ads", GameButtonSize.ASTRONOMY_MENU_BUTTON.getWidth() * 1.5f);
        }

        return tableWithBtnAndScore;
    }

    private Table createTableWithBtnAndScore(MyButton categBtn, MyWrappedLabel levelScoreLabel) {
        float sideDimen = getStartGameButtonSideDimen();
        Table table = new Table();
        table.add(levelScoreLabel).row();
        table.add(categBtn).width(sideDimen).height(sideDimen);
        return table;
    }

    private MyButton createGameBtn(String labelName, GameButtonSkin buttonSkin, boolean isExtraContent) {
        String labelText = SpecificPropertiesUtils.getText(labelName + "_short");
        if (StringUtils.isBlank(labelText)) {
            labelText = SpecificPropertiesUtils.getText(labelName);
        }
        return new ImageButtonBuilder(buttonSkin, getAbstractScreen())
                .textButtonWidth(GameButtonSize.ASTRONOMY_MENU_BUTTON.getWidth() * 1.3f)
                .padBetweenImageAndText(getPadBetweenImageAndText(isExtraContent))
                .setFontScale(FontManager.getNormalFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(labelText)
                .build();
    }

    private MyWrappedLabel createLevelScoreLabel(int totalItems, int currentLevelScore) {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        getScoreLabelColor(totalItems, currentLevelScore),
                        currentLevelScore == totalItems ? FontColor.GREEN.getColor() : FontColor.DARK_GRAY.getColor(),
                        FontConfig.FONT_SIZE * (currentLevelScore == totalItems ? 1.6f : 1.6f),
                        currentLevelScore == totalItems ? 3f : 3f))
                .setText(currentLevelScore + "/" + totalItems).build());
    }

    private Color getScoreLabelColor(int totalItems, int currentLevelScore) {
        if (currentLevelScore == 0) {
            return RGBColor.LIGHT_RED2.toColor();
        }
        return currentLevelScore == totalItems ? FontColor.LIGHT_GREEN.getColor() : FontColor.WHITE.getColor();
    }

    private MyWrappedLabel createTitleLabel() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1.4f,
                        4f))
                .setText(SpecificPropertiesUtils.getText(astronomyGameType.levelName)).build());
    }


    @Override
    public void onBackKeyPress() {
        getScreenManager().showMainScreen();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
