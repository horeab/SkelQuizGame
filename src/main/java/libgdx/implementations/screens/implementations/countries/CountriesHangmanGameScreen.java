package libgdx.implementations.screens.implementations.countries;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.countries.hangman.CountriesHangmanQuestionContainerCreatorService;
import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

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
        float margins = MainDimen.vertical_general_margin.getDimen();
        allTable.add(createHeaderTable()).padBottom(margins / 1.1f).width(ScreenDimensionsManager.getScreenWidth()).row();
        allTable.add(questionContainerCreatorService.createTopCountriesTable()).padBottom(margins).row();
        allTable.add(questionContainerCreatorService.getPressedLettersLabel()).padBottom(margins * 1.5f).row();
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);

        addActor(allTable);
    }

    private Table createHeaderTable() {
        Table table = new Table();
        MyWrappedLabel categLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("Which are the 20 most populous countries")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.1f)).build());
        table.add(categLabel).width(ScreenDimensionsManager.getScreenWidthValue(60));
        MyWrappedLabel counterLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("174")
                .setFontColor(FontColor.RED)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.8f)).build());
        table.add(counterLabel).width(ScreenDimensionsManager.getScreenWidthValue(40));
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