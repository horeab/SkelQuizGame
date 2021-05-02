package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.astronomy.spec.AstronomyPreferencesManager;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.astronomy.spec.PlanetsUtil;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;

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
        Table table = new Table();
        if (astronomyGameType == AstronomyGameType.SOLAR_SYSTEM) {
            List<Planet> allPlanets = PlanetsUtil.getAllPlanets();
            int i = 0;
            float planetsGameSideDimen = getPlanetsGameTypeButtonSideDimen();
            for (AstronomyPlanetsGameType planetsGameType : AstronomyPlanetsGameType.values()) {
                if (i > 0 && i % 2 == 0) {
                    table.row();
                }
                float pad = planetsGameSideDimen / 3f;
                table.add(createPlanetGameTypeGameButton(planetsGameType, i, allPlanets))
                        .width(planetsGameSideDimen).height(planetsGameSideDimen)
                        .padLeft(pad)
                        .padRight(pad)
                        .padBottom(pad * 3);
                i++;
            }
        }
        return table;
    }

    private float getPlanetsGameTypeButtonSideDimen() {
        return ScreenDimensionsManager.getScreenHeightValue(15);
    }

    private Table createPlanetGameTypeGameButton(final AstronomyPlanetsGameType planetsGameType, int index, List<Planet> allPlanets) {
        String labelText = SpecificPropertiesUtils.getText(planetsGameType.levelName + "_short");
        if (StringUtils.isBlank(labelText)) {
            labelText = SpecificPropertiesUtils.getText(planetsGameType.levelName);
        }
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("ASTRONOMY_PLANET_GAME_" + index), getAbstractScreen())
                .padBetweenImageAndText(1f)
                .setFontScale(FontManager.getNormalFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(labelText)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getScreenManager().showCampaignDragDropGameScreen(planetsGameType, allPlanets);
            }
        });
        float planetsGameSideDimen = getPlanetsGameTypeButtonSideDimen();
        Table table = new Table();
        table.add(createLevelScoreLabel(planetsGameType, allPlanets)).row();
        table.add(categBtn).width(planetsGameSideDimen).height(planetsGameSideDimen);
        return table;
    }

    private MyWrappedLabel createLevelScoreLabel(AstronomyPlanetsGameType planetsGameType, List<Planet> allPlanets) {
        int size = PlanetsUtil.getAllAvailableLevelsToPlay(allPlanets, planetsGameType).size();
        int levelScore = astronomyPreferencesManager.getLevelScore(planetsGameType);
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        levelScore == size ? FontColor.LIGHT_GREEN.getColor() : FontColor.WHITE.getColor(),
                        levelScore == size ? FontColor.GREEN.getColor() : FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * (levelScore == size ? 1.6f : 1.4f),
                        levelScore == size ? 3f : 2f))
                .setText(levelScore + "/" + size).build());
    }

    private MyWrappedLabel createTitleLabel() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1.7f,
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
