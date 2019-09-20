package libgdx.screens.implementations.geoquiz;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.game.Game;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screen.AbstractScreen;
import libgdx.screens.QuizScreenManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public abstract class GameScreen extends AbstractScreen<QuizScreenManager> {

    GameContext gameContext;
    private SinglePlayerLevelFinishedService levelFinishedService;

    public GameScreen(GameContext gameContext) {
        this.gameContext = gameContext;
        this.levelFinishedService = new SinglePlayerLevelFinishedService();
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

    @Override
    protected void setBackgroundColor(RGBColor backgroundColor) {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            super.setBackgroundColor(RGBColor.RED);
        } else {
            super.setBackgroundColor(backgroundColor);
        }
    }

    public abstract void executeLevelFinished();

    public abstract void goToNextQuestionScreen();

    public void showPopupAd(Runnable runnable) {
        Game.getInstance().getAppInfoService().showPopupAd(runnable);
    }


}
