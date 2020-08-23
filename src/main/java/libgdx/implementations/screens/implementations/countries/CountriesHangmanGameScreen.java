package libgdx.implementations.screens.implementations.countries;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.countries.hangman.CountriesHangmanQuestionContainerCreatorService;
import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.utils.ScreenDimensionsManager;

public class CountriesHangmanGameScreen extends GameScreen<CountriesScreenManager> {

    public static final int TOP_COUNTRIES_TO_BE_FOUND = 20;
    private Table allTable;

    public CountriesHangmanGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        allTable.add().growY().row();
        CountriesHangmanQuestionContainerCreatorService questionContainerCreatorService =
                new CountriesHangmanQuestionContainerCreatorService(gameContext, this);
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        float topPad = 0;
        if (gameContext.getCurrentUserCreatorDependencies() instanceof HangmanGameCreatorDependencies) {
            topPad = ScreenDimensionsManager.getScreenHeightValue(15);
        }
        allTable.add(questionContainerCreatorService.createTopCountriesTable()).row();
        allTable.add(questionContainerCreatorService.getPressedLettersLabel()).row();
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);

        addActor(allTable);
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