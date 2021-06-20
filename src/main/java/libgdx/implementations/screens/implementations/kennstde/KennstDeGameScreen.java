package libgdx.implementations.screens.implementations.kennstde;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kennstde.KennstDeGame;
import libgdx.implementations.kennstde.KennstDeSpecificResource;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.HangmanRefreshQuestionDisplayService;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class KennstDeGameScreen extends GameScreen<KennstDeScreenManager> {

    private CampaignLevel campaignLevel;
    private Table allTable;

    public KennstDeGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        createAllTable();
    }

    @Override
    public void afterBuildStage() {
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        QuestionContainerCreatorService questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        questionTable.setHeight(ScreenDimensionsManager.getScreenHeight(45));
        answersTable.setHeight(ScreenDimensionsManager.getScreenHeight(50));
        allTable.add(questionTable).height(questionTable.getHeight()).row();
        allTable.add(answersTable).height(answersTable.getHeight());
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    public void goToNextQuestionScreen() {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            ActorAnimation.animateImageCenterScreenFadeOut(KennstDeSpecificResource.success, 0.3f);
        }
        Table hangmanWordTable = getRoot().findActor(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        if (hangmanWordTable != null) {
            hangmanWordTable.addAction(Actions.fadeOut(0.2f));
            hangmanWordTable.remove();
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
    protected int getQuestionsPlayedForPopupAd() {
        return 6;
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
            ActorAnimation.animateImageCenterScreenFadeOut(KennstDeSpecificResource.star, 0.3f);
        }
    }

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            QuizStarsService starsService = KennstDeGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);

        }
        screenManager.showMainScreen();
    }
}
