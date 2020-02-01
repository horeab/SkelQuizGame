package libgdx.screens.implementations.conthistory;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HangmanRefreshQuestionDisplayService;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screens.GameScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class ConthistoryGameScreen extends GameScreen<ConthistoryScreenManager> {

    public static int TOTAL_QUESTIONS = 9;
    private ConthistoryContainers containers = new ConthistoryContainers();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;

    public ConthistoryGameScreen(GameContext gameContext) {
        super(gameContext);
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
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(5));
        allTable.add(headerTable).height(headerTable.getHeight()).row();
        allTable.add(questionTable).growY().row();
        float topPad = 0;
        if (gameContext.getCurrentUserCreatorDependencies() instanceof HangmanGameCreatorDependencies) {
            topPad = ScreenDimensionsManager.getScreenHeightValue(15);
        }
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    @Override
    public void goToNextQuestionScreen() {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
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
    protected void setBackgroundColor(RGBColor backgroundColor) {
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
            screenManager.showMainScreen();
        }
//        screenManager.showMainScreen();
    }
}
