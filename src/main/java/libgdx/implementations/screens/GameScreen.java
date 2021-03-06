package libgdx.implementations.screens;

import libgdx.campaign.CampaignStoreService;
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

    public void animateGameFinished() {
    }

    public void showPopupAd(Runnable runnable) {
        int questionsPlayed = new CampaignStoreService().getNrOfQuestionsPlayed();
        if (questionsPlayed > 0 && questionsPlayed % getQuestionsPlayedForPopupAd() == 0) {
            Game.getInstance().getAppInfoService().showPopupAd(runnable);
        } else {
            runnable.run();
        }
    }

    protected int getQuestionsPlayedForPopupAd() {
        return 10;
    }


}
