package libgdx.screens.implementations.judetelerom;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.anatomy.AnatomyGame;
import libgdx.implementations.anatomy.AnatomySpecificResource;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.screens.GameScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class JudeteleRomGameScreen extends GameScreen<JudeteleRomScreenManager> {

    public static int TOTAL_QUESTIONS = 5;
    private CampaignLevel campaignLevel;
    private Table allTable;

    public JudeteleRomGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
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
        QuestionContainerCreatorService questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        questionTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(45));
        allTable.add(questionTable).height(questionTable.getHeight()).row();
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    @Override
    public void goToNextQuestionScreen() {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            ActorAnimation.animateImageCenterScreenFadeOut(AnatomySpecificResource.success, 0.3f);
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
            ActorAnimation.animateImageCenterScreenFadeOut(AnatomySpecificResource.star, 0.3f);
        }
    }

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            QuizStarsService starsService = AnatomyGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);

        }
        screenManager.showMainScreen();
    }
}
