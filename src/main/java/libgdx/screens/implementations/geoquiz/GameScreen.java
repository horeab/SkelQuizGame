package libgdx.screens.implementations.geoquiz;

import libgdx.game.Game;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screen.AbstractScreen;

public abstract class GameScreen extends AbstractScreen<QuizScreenManager> {

    protected GameContext gameContext;
    protected SinglePlayerLevelFinishedService levelFinishedService;

    public GameScreen(GameContext gameContext) {
        this.gameContext = gameContext;
        this.levelFinishedService = new SinglePlayerLevelFinishedService();
    }


    public abstract void executeLevelFinished();

    public abstract void goToNextQuestionScreen();

    public void showPopupAd(Runnable runnable) {
        Game.getInstance().getAppInfoService().showPopupAd(runnable);
    }


}
