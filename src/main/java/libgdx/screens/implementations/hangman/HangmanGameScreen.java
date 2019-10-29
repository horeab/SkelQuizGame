package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.screens.GameScreen;
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
        this.hangmanQuestionContainerCreatorService = new HangmanQuestionContainerCreatorService(gameContext, this);
    }

    @Override
    public void buildStage() {
        System.out.println(gameContext.getCurrentUserGameUser().getGameQuestionInfo().getQuestion().getQuestionString());
        allTable = new Table();
        allTable.setFillParent(true);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        allTable.add(new HangmanHeaderCreator().createHeaderTable(gameContext, hangmanQuestionContainerCreatorService.getHintButtons(), hangmanQuestionContainerCreatorService.createHintButtonsTable())).padTop(verticalGeneralMarginDimen).row();
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
        allTable.add(new HangmanQuestionContainerCreatorService(gameContext, this).createSquareAnswerOptionsTable(new ArrayList<>(hangmanQuestionContainerCreatorService.getAllAnswerButtons().values())))
                .growY();
        addActor(allTable);
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
                    screen.hangmanQuestionContainerCreatorService = new HangmanQuestionContainerCreatorService(gameContext, screen);
                    buildStage();
                }
            })));
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
