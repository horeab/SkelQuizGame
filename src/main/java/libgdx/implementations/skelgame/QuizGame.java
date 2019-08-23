package libgdx.implementations.skelgame;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.screen.AbstractScreen;
import libgdx.screens.QuizScreenManager;

public class QuizGame extends CampaignGame<AppInfoService,
        QuizGameMainDependencyManager,
        QuizGameDependencyManager,
        AbstractScreen,
        QuizScreenManager,
        QuizGameIdEnum
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
        QuizScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
