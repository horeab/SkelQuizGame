package libgdx.implementations.paintings;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.anatomy.AnatomyScreenManager;

public class PaintingsGame extends CampaignGame<AppInfoService,
        PaintingsMainDependencyManager,
        PaintingsDependencyManager,
        AbstractScreen,
        AnatomyScreenManager,
        GameIdEnum
        > {

    public PaintingsGame(FacebookService facebookService,
                         BillingService billingService,
                         AppInfoService appInfoService) {
        super(facebookService, billingService, appInfoService, new PaintingsMainDependencyManager());
    }

    public PaintingsDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static PaintingsGame getInstance() {
        return (PaintingsGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        AnatomyScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
