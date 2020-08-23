package libgdx.implementations.geoquiz;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.geoquiz.QuizScreenManager;

public class QuizGame extends CampaignGame<AppInfoService,
        QuizGameMainDependencyManager,
        QuizGameDependencyManager,
        AbstractScreen,
        QuizScreenManager,
        GameIdEnum
        > {

    public QuizGame(AppInfoService appInfoService) {
        super(appInfoService, new QuizGameMainDependencyManager());
    }

    public QuizGameDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static QuizGame getInstance() {
        return (QuizGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        QuizScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
