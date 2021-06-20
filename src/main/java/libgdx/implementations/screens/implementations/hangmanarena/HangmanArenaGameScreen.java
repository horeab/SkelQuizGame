package libgdx.implementations.screens.implementations.hangmanarena;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.hangmanarena.HangmanArenaGame;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.hangmanarena.spec.HangmanHeaderCreator;
import libgdx.implementations.screens.implementations.hangmanarena.spec.HangmanScreenBackgroundCreator;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

import java.util.ArrayList;

public class HangmanArenaGameScreen extends GameScreen<HangmanArenaScreenManager> {

    public static int TOTAL_QUESTIONS = 5;

    private CampaignLevel campaignLevel;
    private Table allTable;
    private HangmanQuestionContainerCreatorService hangmanQuestionContainerCreatorService;
    private HangmanScreenBackgroundCreator hangmanScreenBackgroundCreator;

    public HangmanArenaGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
        hangmanScreenBackgroundCreator = new HangmanScreenBackgroundCreator(getAbstractScreen(), gameContext.getCurrentUserGameUser());
    }

    @Override
    public void buildStage() {
        hangmanScreenBackgroundCreator.createBackground();
        createGameTable(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createGameTable(GameQuestionInfo gameQuestionInfo) {
        System.out.println(gameQuestionInfo.getQuestion().getQuestionString());
        allTable = new Table();
        allTable.setFillParent(true);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table headerTable = new Table();
        allTable.add(headerTable).padTop(verticalGeneralMarginDimen).row();
        Table wordTable = new Table();
        wordTable.setName(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        allTable.add(wordTable).growY().row();
        Table image = new Table();
        image.setName(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_IMAGE);
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
                new HangmanHeaderCreator().refreshRemainingAnswersTable(gameContext);
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
        return ScreenDimensionsManager.getScreenWidth(55);
    }

    public static float getHangmanImgHeight() {
        return ScreenDimensionsManager.getScreenHeight(35);
    }

    @Override
    protected int getQuestionsPlayedForPopupAd() {
        return 7;
    }

    @Override
    public void executeLevelFinished() {
        SinglePlayerLevelFinishedService levelFinishedService = new SinglePlayerLevelFinishedService();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            new CampaignService().levelFinished(HangmanArenaGame.getInstance().getDependencyManager().getStarsService().getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser)), campaignLevel);
        }
        if (new SinglePlayerLevelFinishedService().isGameFailed(gameContext.getCurrentUserGameUser())) {
            new LevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        } else {
            screenManager.showCampaignScreen();
        }
    }

    @Override
    public void goToNextQuestionScreen() {
        if (new SinglePlayerLevelFinishedService().isGameFailed(gameContext.getCurrentUserGameUser())) {
            new LevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        } else {
            allTable.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    allTable.remove();
                    hangmanScreenBackgroundCreator.refreshSkyImages(HangmanGameService.GAME_OVER_WRONG_LETTERS, false);
                    createGameTable(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
                }
            })));
        }
    }
    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
    }
}
