package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.astronomy.AstronomyCampaignLevelEnum;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.astronomy.spec.AstronomyPreferencesManager;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.astronomy.spec.PlanetsUtil;
import libgdx.implementations.screens.implementations.anatomy.AnatomyGameScreen;
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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AstronomyDetailedCampaignScreen extends AbstractScreen<AstronomyScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private AstronomyGameType astronomyGameType;
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private AstronomyPreferencesManager astronomyPreferencesManager = new AstronomyPreferencesManager();

    public AstronomyDetailedCampaignScreen(AstronomyGameType astronomyGameType) {
        this.astronomyGameType = astronomyGameType;
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }


    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);
    }

    private Table createAllTable() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        Table table = new Table();
        float levelTableHeight = ScreenDimensionsManager.getScreenHeightValue(90);
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
                campaignLevelEnums = Arrays.asList(
                        AstronomyCampaignLevelEnum.LEVEL_0_1,
                        AstronomyCampaignLevelEnum.LEVEL_0_2,
                        AstronomyCampaignLevelEnum.LEVEL_0_3,
                        AstronomyCampaignLevelEnum.LEVEL_0_5
                );
            } else if (astronomyGameType == AstronomyGameType.ASTRONOMY_IMAGES_QUIZ) {
                campaignLevelEnums = Arrays.asList(
                        AstronomyCampaignLevelEnum.LEVEL_0_4,
                        AstronomyCampaignLevelEnum.LEVEL_0_6,
                        AstronomyCampaignLevelEnum.LEVEL_0_7
                );
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
        String labelText = new CampaignLevelEnumService(campaignLevel).getLabelText();
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("ASTRONOMY_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .padBetweenImageAndText(1f)
                .setFontScale(FontManager.getSmallFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(labelText)
                .build();
        MyWrappedLabel levelScoreLabel = createLevelScoreLabel(15, 11);
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                QuestionConfig questionConfig = enumService.getQuestionConfig(AnatomyGameScreen.TOTAL_QUESTIONS);
                AstronomyGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel, astronomyGameType);
            }
        });
        return createTableWithBtnAndScore(categBtn, levelScoreLabel);
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
        return ScreenDimensionsManager.getScreenHeightValue(15);
    }

    private Table createPlanetGameTypeGameButton(final AstronomyPlanetsGameType planetsGameType, int index, List<Planet> allPlanets) {
        MyButton categBtn = createGameBtn(planetsGameType.levelName, GameButtonSkin.valueOf("ASTRONOMY_PLANET_GAME_" + index));
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getScreenManager().showCampaignDragDropGameScreen(planetsGameType, allPlanets);
            }
        });
        MyWrappedLabel levelScoreLabel = createLevelScoreLabel(PlanetsUtil.getAllAvailableLevelsToPlay(allPlanets, planetsGameType).size(), astronomyPreferencesManager.getLevelScore(planetsGameType));
        return createTableWithBtnAndScore(categBtn, levelScoreLabel);
    }

    private Table createTableWithBtnAndScore(MyButton categBtn, MyWrappedLabel levelScoreLabel) {
        float sideDimen = getStartGameButtonSideDimen();
        Table table = new Table();
        table.add(levelScoreLabel).row();
        table.add(categBtn).width(sideDimen).height(sideDimen);
        return table;
    }

    private MyButton createGameBtn(String labelName, GameButtonSkin buttonSkin) {
        String labelText = SpecificPropertiesUtils.getText(labelName + "_short");
        if (StringUtils.isBlank(labelText)) {
            labelText = SpecificPropertiesUtils.getText(labelName);
        }
        return new ImageButtonBuilder(buttonSkin, getAbstractScreen())
                .padBetweenImageAndText(1f)
                .setFontScale(FontManager.getNormalFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(labelText)
                .build();
    }

    private MyWrappedLabel createLevelScoreLabel(int totalItems, int currentLevelScore) {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        currentLevelScore == totalItems ? FontColor.LIGHT_GREEN.getColor() : FontColor.WHITE.getColor(),
                        currentLevelScore == totalItems ? FontColor.GREEN.getColor() : FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * (currentLevelScore == totalItems ? 1.6f : 1.4f),
                        currentLevelScore == totalItems ? 3f : 2f))
                .setText(currentLevelScore + "/" + totalItems).build());
    }

    private MyWrappedLabel createTitleLabel() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1.2f,
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
