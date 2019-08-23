package libgdx.implementations.skelgame;


import libgdx.constants.GameIdEnum;
import libgdx.game.Game;
import libgdx.game.ScreenManager;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.screens.AbstractScreen;

public class QuizGame extends Game<AppInfoService,
        QuizGameMainDependencyManager,
        QuizGameDependencyManager,
        AbstractScreen,
        ScreenManager,
        GameIdEnum
        > {

    public QuizGame(FacebookService facebookService,
                    BillingService billingService,
                    AppInfoService appInfoService) {
        super(facebookService, billingService, appInfoService, new QuizGameMainDependencyManager());
    }

    public QuizGameDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static QuizGame getInstance() {
        return (QuizGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        ScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
