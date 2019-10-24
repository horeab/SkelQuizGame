package libgdx.implementations.kennstde;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangman.HangmanScreenManager;
import libgdx.screens.implementations.kennstde.KennstDeScreenManager;

public class KennstDeGame extends CampaignGame<AppInfoService,
        KennstDeMainDependencyManager,
        KennstDeDependencyManager,
        AbstractScreen,
        KennstDeScreenManager,
        GameIdEnum
        > {

    public KennstDeGame(FacebookService facebookService,
                        BillingService billingService,
                        AppInfoService appInfoService) {
        super(facebookService, billingService, appInfoService, new KennstDeMainDependencyManager());
    }

    public KennstDeDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static KennstDeGame getInstance() {
        return (KennstDeGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        KennstDeScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
