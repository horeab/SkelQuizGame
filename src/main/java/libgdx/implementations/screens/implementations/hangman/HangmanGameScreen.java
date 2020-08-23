package libgdx.implementations.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

import java.util.ArrayList;

public class HangmanGameScreen extends GameScreen<HangmanScreenManager> {

    public static int TOTAL_QUESTIONS = 5;

    private CampaignLevel campaignLevel;
    private Table allTable;
    private HangmanQuestionContainerCreatorService hangmanQuestionContainerCreatorService;

    public HangmanGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().getCurrentUser() != null) {
            new GameStatsDbApiService().incrementGameStatsQuestionsWon(Game.getInstance().getCurrentUser().getId(), Long.valueOf(DateUtils.getNowMillis()).toString());
        }
        System.out.println(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionString());
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
        this.hangmanQuestionContainerCreatorService = new HangmanQuestionContainerCreatorService(gameContext, this);
        headerTable.add(new HangmanHeaderCreator().createHeaderTable(gameContext, hangmanQuestionContainerCreatorService.createHintButtonsTable()));
        squareAnswerOptionsTable.add(new HangmanQuestionContainerCreatorService(gameContext, this).createSquareAnswerOptionsTable(new ArrayList<>(hangmanQuestionContainerCreatorService.getAllAnswerButtons().values())));
        hangmanQuestionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        new BackButtonBuilder().addHoverBackButton(this);
    }

    public static float getHangmanImgWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(55);
    }

    public static float getHangmanImgHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(35);
    }

    public void goToNextQuestionScreen() {
        if (new SinglePlayerLevelFinishedService().isGameFailed(gameContext.getCurrentUserGameUser())) {
            new LevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        } else {
            final HangmanGameScreen screen = this;
            allTable.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    allTable.remove();
                    buildStage();
                }
            })));
        }
    }

    @Override
    public void showPopupAd(Runnable runnable) {
        int questionsPlayed = new CampaignStoreService().getNrOfQuestionsPlayed();
        if (questionsPlayed > 0 && questionsPlayed % 7 == 0) {
            Game.getInstance().getAppInfoService().showPopupAd(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
    }

    public void executeLevelFinished() {
        SinglePlayerLevelFinishedService levelFinishedService = new SinglePlayerLevelFinishedService();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            new CampaignService().levelFinished(HangmanGame.getInstance().getDependencyManager().getStarsService().getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser)), campaignLevel);
        }
        if (new SinglePlayerLevelFinishedService().isGameFailed(gameContext.getCurrentUserGameUser())) {
            new LevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        } else {
            screenManager.showCampaignScreen();
        }
    }
}
