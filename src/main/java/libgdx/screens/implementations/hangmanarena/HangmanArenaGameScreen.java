package libgdx.screens.implementations.hangmanarena;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.HangmanRefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.dimen.MainDimen;
import libgdx.screens.GameScreen;
import libgdx.screens.implementations.hangmanarena.spec.HangmanHeaderCreator;
import libgdx.screens.implementations.hangmanarena.spec.HangmanScreenBackgroundCreator;
import libgdx.utils.ScreenDimensionsManager;

import java.util.ArrayList;

public class HangmanArenaGameScreen extends GameScreen<HangmanArenaScreenManager> {

    public static int TOTAL_QUESTIONS = 5;

    private CampaignLevel campaignLevel;
    private Table allTable;
    private HangmanQuestionContainerCreatorService hangmanQuestionContainerCreatorService;

    public HangmanArenaGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        createGameTable(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createGameTable(GameQuestionInfo gameQuestionInfo) {
        HangmanScreenBackgroundCreator hangmanScreenBackgroundCreator = new HangmanScreenBackgroundCreator(getAbstractScreen(), gameContext.getCurrentUserGameUser());
        hangmanScreenBackgroundCreator.createBackground();

        System.out.println(gameQuestionInfo.getQuestion().getQuestionString());
        allTable = new Table();
        allTable.setFillParent(true);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table headerTable = new Table();
        allTable.add(headerTable).padTop(verticalGeneralMarginDimen).row();
        Table wordTable = new Table();
        wordTable.setName(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        Table image = new Table();
        image.setName(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_IMAGE);
        allTable.add(wordTable).growY().row();
        allTable.add(image)
                .padTop(verticalGeneralMarginDimen)
                .height(getHangmanImgHeight())
                .width(getHangmanImgWidth())
                .row();
        Table squareAnswerOptionsTable = new Table();
        allTable.add(squareAnswerOptionsTable)
                .growY();
        addActor(allTable);
        this.hangmanQuestionContainerCreatorService = new HangmanQuestionContainerCreatorService(gameContext, this) {
            @Override
            public void processGameInfo(GameQuestionInfo gameQuestionInfo) {
                hangmanScreenBackgroundCreator.refreshBackground(gameService.getNrOfWrongAnswersPressed(gameQuestionInfo.getAnswerIds()));
                super.processGameInfo(gameQuestionInfo);
              }
        };
        HangmanHeaderCreator hangmanHeaderCreator = new HangmanHeaderCreator();
        headerTable.add(hangmanHeaderCreator.createHeaderTable(gameContext,
                hangmanQuestionContainerCreatorService.createHintButtonsTable()));
        squareAnswerOptionsTable.add(hangmanHeaderCreator
                .refreshRemainingAnswersTable(gameContext)).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        squareAnswerOptionsTable.add(new HangmanQuestionContainerCreatorService(gameContext, this)
                .createSquareAnswerOptionsTable(new ArrayList<>(hangmanQuestionContainerCreatorService.getAllAnswerButtons().values())));
        hangmanQuestionContainerCreatorService.processGameInfo(gameQuestionInfo);
    }

    public static float getHangmanImgWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(55);
    }

    public static float getHangmanImgHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(35);
    }
    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
    }

    @Override
    public void executeLevelFinished() {

    }

    @Override
    public void goToNextQuestionScreen() {

    }
}
