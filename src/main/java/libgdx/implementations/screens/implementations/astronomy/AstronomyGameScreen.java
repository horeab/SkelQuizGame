package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

import java.util.HashMap;
import java.util.Map;

public class AstronomyGameScreen extends GameScreen<AstronomyScreenManager> {

    public static int TOTAL_QUESTIONS = 9;
    private AstronomyContainers containers = new AstronomyContainers();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;
    private CampaignLevel campaignLevel;

    public AstronomyGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
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
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        QuestionContainerCreatorService questionContainerCreatorService;
        if (gameContext.getQuestion().getQuestionCategory().getCreatorDependencies() == ImageClickGameCreatorDependencies.class) {
            questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        } else {
            questionContainerCreatorService = new AstronomyQuestionContainerCreatorService(gameContext, this);
        }
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        Table headerTable = new HeaderCreator().createHeaderTable(createAllQuestionsMap());
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(5));
        allTable.add(headerTable).height(headerTable.getHeight()).row();
        allTable.add(questionTable).growY().row();
        float topPad = 0;
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    private Map<Integer, GameQuestionInfoStatus> createAllQuestionsMap() {
        Map<Integer, GameQuestionInfoStatus> map = new HashMap<>();
        for (GameQuestionInfo gameQuestionInfo : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            map.put(gameQuestionInfo.getQuestion().getQuestionLineInQuestionFile(), gameQuestionInfo.getStatus());
        }
        return map;
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

    @Override
    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            QuizStarsService starsService = AstronomyGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);
            screenManager.showMainScreen();
        } else if (levelFinishedService.isGameFailed(gameUser)) {
            new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        }
//        screenManager.showMainScreen();
    }
}
