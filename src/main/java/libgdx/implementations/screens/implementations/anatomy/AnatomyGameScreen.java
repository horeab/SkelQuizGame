package libgdx.implementations.screens.implementations.anatomy;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.anatomy.AnatomyGame;
import libgdx.implementations.anatomy.AnatomySpecificResource;
import libgdx.implementations.anatomy.spec.AnatomyGameType;
import libgdx.implementations.anatomy.spec.AnatomyPreferencesManager;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

import java.util.List;
import java.util.Map;

public class AnatomyGameScreen extends GameScreen<AnatomyScreenManager> {

    private CampaignLevel campaignLevel;
    private Table allTable;
    private MyButton backButton;
    private AnatomyGameType anatomyGameType;
    private AnatomyPreferencesManager anatomyPreferencesManager = new AnatomyPreferencesManager();
    private boolean gamWonSuccessAlreadyDisplayed = false;

    public AnatomyGameScreen(GameContext gameContext, CampaignLevel campaignLevel, AnatomyGameType anatomyGameType) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
        this.anatomyGameType = anatomyGameType;
    }

    @Override
    public void buildStage() {
        if (anatomyGameType == AnatomyGameType.GENERALK) {
            AnatomyLevelScreen.addScreenBackground(getRootCampaignLevelForValue(campaignLevel), this);
        }
        createAllTable();
        backButton = new BackButtonBuilder().addHoverBackButton(this, ScreenDimensionsManager.getScreenWidth(0.5f),
                BackButtonBuilder.getY());
    }

    private void createAllTable() {

        allTable = new Table();
        QuestionContainerCreatorService questionContainerCreatorService;
        Table answersTable = null;
        Table questionTable;
        if (anatomyGameType == AnatomyGameType.IDENTIFY) {
            questionContainerCreatorService = new AnatomyImageQuestionContainerCreatorService(gameContext, this);
            questionTable = questionContainerCreatorService.createQuestionTable();
        } else {
            questionTable = new Table();
            questionContainerCreatorService = new AnatomyQuestionContainerCreatorService(gameContext, this);
            Table actQuestionTable = questionContainerCreatorService.createQuestionTable();
            actQuestionTable.setBackground(GraphicUtils.getColorBackground(RGBColor.WHITE.toColor(0.65f)));
            questionTable.add(actQuestionTable);
            answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        }
        questionTable.setHeight(ScreenDimensionsManager.getScreenHeight(45));
        allTable.add(questionTable).height(questionTable.getHeight()).row();
        if (answersTable != null) {
            questionTable.padTop(MainDimen.vertical_general_margin.getDimen() * 6);
            allTable.add(answersTable).padTop(0).growY();
        }
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
        if (backButton != null) {
            backButton.toFront();
        }
    }

    public static CampaignLevel getRootCampaignLevelForValue(CampaignLevel campaignLevel) {
        for (Map.Entry<AnatomyCampaignLevelEnum, List<CampaignLevel>> e : AnatomyCampaignScreen.campaign0AllLevels.entrySet()) {
            if (e.getValue().contains(campaignLevel)) {
                return e.getKey();
            }
        }
        return null;
    }

    @Override
    public void goToNextQuestionScreen() {
        if (!gamWonSuccessAlreadyDisplayed && levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            gamWonSuccessAlreadyDisplayed = true;
            ActorAnimation.animateImageCenterScreenFadeOut(AnatomySpecificResource.success, 0.3f);
        }
        anatomyPreferencesManager.putLevelScore((AnatomyCampaignLevelEnum) campaignLevel, gameContext.getCurrentUserGameUser().getWonQuestions());
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
        screenManager.showLevelScreen((AnatomyCampaignLevelEnum) getRootCampaignLevelForValue(campaignLevel));
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
        anatomyPreferencesManager.putLevelScore((AnatomyCampaignLevelEnum) campaignLevel, gameContext.getCurrentUserGameUser().getWonQuestions());
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
        screenManager.showLevelScreen((AnatomyCampaignLevelEnum) getRootCampaignLevelForValue(campaignLevel));
    }
}
