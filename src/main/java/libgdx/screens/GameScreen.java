package libgdx.screens;

import libgdx.game.Game;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public abstract class GameScreen<TScreenManager extends AbstractScreenManager> extends AbstractScreen<TScreenManager> {

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
