package libgdx.implementations.screens.implementations.countries;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Arrays;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.countries.CountriesCategoryEnum;
import libgdx.implementations.countries.CountriesSpecificResource;
import libgdx.implementations.countries.hangman.CountriesAtoZQuestionContainerCreatorService;
import libgdx.implementations.countries.hangman.CountriesPopulationAreaNeighbQuestionContainerCreatorService;
import libgdx.implementations.countries.hangman.CountriesPressedLettersQuestionContainerCreatorService;
import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesGameScreen extends GameScreen<CountriesScreenManager> {

    public static final int TOP_COUNTRIES_TO_BE_FOUND = 20;
    private Table allTable;
    private CampaignLevelEnumService campaignLevelEnumService;

    public CountriesGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        campaignLevelEnumService = new CampaignLevelEnumService(campaignLevel);
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        Stack stack = new Stack();
        stack.add(createContentTable());
        stack.setFillParent(true);
        allTable.add(stack);
        createBackground();
        addActor(allTable);
    }

    private Table createContentTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.add().growY().row();
        CountriesPressedLettersQuestionContainerCreatorService questionContainerCreatorService = null;
        if (Arrays.asList(CountriesCategoryEnum.cat0, CountriesCategoryEnum.cat1, CountriesCategoryEnum.cat3)
                .contains(campaignLevelEnumService.getCategoryEnum())) {
            questionContainerCreatorService = new CountriesPopulationAreaNeighbQuestionContainerCreatorService(gameContext, this);
        } else if (campaignLevelEnumService.getCategoryEnum() == CountriesCategoryEnum.cat2) {
            questionContainerCreatorService = new CountriesAtoZQuestionContainerCreatorService(gameContext, this);
        }
        table.add(questionContainerCreatorService.createAllGameView()).row();
        return table;
    }

    private void createBackground() {
        int scrollSpeed = 55;
        Image image1 = GraphicUtils.getImage(CountriesSpecificResource.moving_background);
        image1.setY(0);
        image1.setFillParent(true);
        Image image2 = GraphicUtils.getImage(CountriesSpecificResource.moving_background);
        image2.setY(-ScreenDimensionsManager.getScreenHeight());
        image2.setFillParent(true);
        addActor(image1);
        addActor(image2);
        animateMoveUp(scrollSpeed, image1);
        animateMoveUp(scrollSpeed, image2);
    }

    public void animateMoveUp(final float amount, final Actor actor) {
        RunnableAction run = new RunnableAction();
        run.setRunnable(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                animateMoveUp(amount, actor);
                int screenHeight = ScreenDimensionsManager.getScreenHeight();
                System.out.println(actor.getY() + "");
                if (actor.getY() > screenHeight) {
                    actor.setY(-screenHeight);
                }
            }
        });
        float duration = 0.5f;
        actor.addAction(Actions.sequence(Actions.moveBy(0, amount, duration), run));
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