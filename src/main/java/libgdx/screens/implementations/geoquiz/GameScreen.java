package libgdx.screens.implementations.geoquiz;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.game.Game;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.screen.AbstractScreen;
import libgdx.screens.QuizScreenManager;
import libgdx.utils.ScreenDimensionsManager;

public class GameScreen extends AbstractScreen<QuizScreenManager> {

    private GameContext gameContext;

    public GameScreen(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        Table allTable = new Table();
        QuestionContainerCreatorService questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        questionTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(60));
        answersTable.setHeight(ScreenDimensionsManager.getScreenHeightValue(40));
        allTable.add(questionTable).row();
        allTable.add(answersTable);
        allTable.setFillParent(true);
        addActor(allTable);
    }

    public void executeLevelFinished() {
    }

    public void showPopupAd() {
        Game.getInstance().getAppInfoService().showPopupAd();
    }

    public void goToNextQuestionScreen() {
        screenManager.showGameScreen(gameContext);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
    }

}
