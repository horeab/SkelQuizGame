package libgdx.implementations.screens.implementations.conthistory;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.conthistory.ConthistoryGame;
import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class ConthistoryGameScreen extends GameScreen<ConthistoryScreenManager> {

    public static int TOTAL_QUESTIONS = 9;
    private ConthistoryContainers containers = new ConthistoryContainers();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;
    private CampaignLevel campaignLevel;

    public ConthistoryGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        if (Game.getInstance().getCurrentUser() != null) {
            new GameStatsDbApiService().incrementGameStatsQuestionsWon(Game.getInstance().getCurrentUser().getId(), Long.valueOf(DateUtils.getNowMillis()).toString());
        }
        allTable = new Table();
        float dimen = MainDimen.vertical_general_margin.getDimen();
        QuestionContainerCreatorService questionContainerCreatorService = new ConthistoryQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        Table headerTable = new HeaderCreator().createHeaderTable(gameContext);
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeight(5));
        allTable.add(headerTable).height(headerTable.getHeight()).row();
        allTable.add(questionTable).growY().row();
        float topPad = 0;
        if (gameContext.getCurrentUserCreatorDependencies() instanceof HangmanGameCreatorDependencies) {
            topPad = ScreenDimensionsManager.getScreenHeight(15);
        }
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    @Override
    public void goToNextQuestionScreen() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameFailed(gameUser)) {
            new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        }
        Table table = getRoot().findActor(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        if (table != null) {
            table.addAction(Actions.fadeOut(0.2f));
            table.remove();
        }
        allTable.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                allTable.remove();
                createAllTable();
            }
        })));
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void setBackgroundColor(RGBColor backgroundColor) {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            super.setBackgroundColor(RGBColor.RED);
        } else {
            super.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void animateGameFinished() {
        super.animateGameFinished();
        if (LevelFinishedService.getPercentageOfWonQuestions(gameContext.getCurrentUserGameUser()) == 100f) {
//            ActorAnimation.animateImageCenterScreenFadeOut(AnatomySpecificResource.star, 0.3f);
        }
    }

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            QuizStarsService starsService = ConthistoryGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);
            screenManager.showMainScreen();
        } else if (levelFinishedService.isGameFailed(gameUser)) {
            new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        }
//        screenManager.showMainScreen();
    }
}
