package libgdx.screens.implementations.kennstde;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screens.GameScreen;
import libgdx.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.screens.implementations.geoquiz.HeaderCreator;
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
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
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

    public void executeLevelFinished() {
        screenManager.showMainScreen();
    }
}
