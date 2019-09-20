package libgdx.screens.implementations.geoquiz;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public class CampaignGameScreen extends GameScreen {

    private CampaignLevel campaignLevel;

    public CampaignGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
        Table allTable = new Table();
        QuestionContainerCreatorService questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        Table headerTable = new HeaderCreator().createHeaderTable(gameContext);
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(5));
        questionTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(45));
        answersTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(50));
        allTable.add(headerTable).height(headerTable.getHeight()).row();
        allTable.add(questionTable).height(questionTable.getHeight()).row();
        allTable.add(answersTable).height(answersTable.getHeight());
        allTable.setFillParent(true);
        addActor(allTable);
    }

    public void goToNextQuestionScreen() {
        screenManager.showCampaignGameScreen(gameContext, campaignLevel);
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

    public void executeLevelFinished() {
        SinglePlayerLevelFinishedService levelFinishedService = new SinglePlayerLevelFinishedService();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            new CampaignService().levelFinished(QuizGame.getInstance().getDependencyManager().getStarsService().getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser)), campaignLevel);
        }
        new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
    }
}
