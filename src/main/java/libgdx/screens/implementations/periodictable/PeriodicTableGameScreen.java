package libgdx.screens.implementations.periodictable;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.periodictable.PeriodicTableGame;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screens.GameScreen;
import libgdx.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class PeriodicTableGameScreen extends GameScreen<PeriodicTableScreenManager> {

    public static int TOTAL_QUESTIONS = 9;
    private PeriodicTableContainers containers = new PeriodicTableContainers();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;
    private CampaignLevel campaignLevel;

    public PeriodicTableGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        QuestionContainerCreatorService questionContainerCreatorService;
        if (gameContext.getQuestion().getQuestionCategory().getCreatorDependencies() == ImageClickGameCreatorDependencies.class) {
            questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        } else {
            questionContainerCreatorService = new PeriodicTableQuestionContainerCreatorService(gameContext, this);
        }
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        Table headerTable = new HeaderCreator().createHeaderTable(gameContext);
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(5));
        allTable.add(headerTable).height(headerTable.getHeight()).row();
        allTable.add(questionTable).growY().row();
        float topPad = 0;
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
    protected int getQuestionsPlayedForPopupAd() {
        return 7;
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
            QuizStarsService starsService = PeriodicTableGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);
            screenManager.showMainScreen();
        } else if (levelFinishedService.isGameFailed(gameUser)) {
            new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        }
//        screenManager.showMainScreen();
    }
}