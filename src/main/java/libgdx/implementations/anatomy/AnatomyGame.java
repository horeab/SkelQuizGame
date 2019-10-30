package libgdx.implementations.anatomy;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.anatomy.AnatomyScreenManager;

public class AnatomyGame extends CampaignGame<AppInfoService,
        AnatomyMainDependencyManager,
        AnatomyDependencyManager,
        AbstractScreen,
        AnatomyScreenManager,
        GameIdEnum
        > {

    public AnatomyGame(FacebookService facebookService,
                       BillingService billingService,
                       AppInfoService appInfoService) {
        super(facebookService, billingService, appInfoService, new AnatomyMainDependencyManager());
    }

    public AnatomyDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static AnatomyGame getInstance() {
        return (AnatomyGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        AnatomyScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
