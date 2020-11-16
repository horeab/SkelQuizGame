package libgdx.implementations.screens.implementations.countries;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Arrays;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesSpecificResource;
import libgdx.implementations.countries.hangman.CountriesAtoZQuestionContainerCreatorService;
import libgdx.implementations.countries.hangman.CountriesPopulationAreaNeighbQuestionContainerCreatorService;
import libgdx.implementations.countries.hangman.CountriesPressedLettersQuestionContainerCreatorService;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesGameScreen extends GameScreen<CountriesScreenManager> {

    public static final int TOP_COUNTRIES_TO_BE_FOUND = 20;
    private Table allTable;
    private CampaignLevelEnumService campaignLevelEnumService;
    private CampaignLevel campaignLevel;

    public CountriesGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        campaignLevelEnumService = new CampaignLevelEnumService(campaignLevel);
        this.campaignLevel = campaignLevel;
    }

    public CampaignLevel getCampaignLevel() {
        return campaignLevel;
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this, MainDimen.horizontal_general_margin.getDimen() * 2,
                ScreenDimensionsManager.getScreenHeightValue(41));
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        Stack stack = new Stack();
        stack.add(createContentTable());
        stack.setFillParent(true);
        allTable.add(stack);
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(CountriesSpecificResource.moving_background);
        addActor(allTable);
    }

    private Table createContentTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.add().growY().row();
        CountriesPressedLettersQuestionContainerCreatorService questionContainerCreatorService = null;
        if (Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1,
                CountriesCategoryEnum.cat3, CountriesCategoryEnum.cat4, CountriesCategoryEnum.cat5)
                .contains(campaignLevelEnumService.getCategoryEnum())) {
            questionContainerCreatorService = new CountriesPopulationAreaNeighbQuestionContainerCreatorService(gameContext, this);
        } else if (campaignLevelEnumService.getCategoryEnum() == CountriesCategoryEnum.cat2) {
            questionContainerCreatorService = new CountriesAtoZQuestionContainerCreatorService(gameContext, this);
        }
        table.add(questionContainerCreatorService.createAllGameView()).row();
        return table;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void animateGameFinished() {
        super.animateGameFinished();
    }

    @Override
    public void executeLevelFinished() {
    }

    @Override
    public void goToNextQuestionScreen() {

    }

}