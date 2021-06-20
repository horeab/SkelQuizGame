package libgdx.implementations.screens.implementations.math;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.paintings.PaintingsGame;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class MathGameScreen extends GameScreen<MathScreenManager> {

    public static int TOTAL_QUESTIONS = 5;
    private CampaignLevel campaignLevel;
    private Table allTable;

    public MathGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
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
        QuestionContainerCreatorService questionContainerCreatorService = new MathQuizQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        questionTable.setHeight(ScreenDimensionsManager.getScreenHeight(45));
        answersTable.setHeight(ScreenDimensionsManager.getScreenHeight(50));
        allTable.add(questionTable).height(questionTable.getHeight())
                .padTop(ScreenDimensionsManager.getScreenHeight(4))
                .padBottom(ScreenDimensionsManager.getScreenHeight(1)).row();

        allTable.add(answersTable).height(answersTable.getHeight());

        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    @Override
    public void goToNextQuestionScreen() {
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
    public void animateGameFinished() {
        super.animateGameFinished();
    }

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            QuizStarsService starsService = PaintingsGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);

        }
        screenManager.showMainScreen();
    }
}
