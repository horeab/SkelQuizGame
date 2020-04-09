package libgdx.implementations.periodictable;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.periodictable.PeriodicTableScreenManager;

public class PeriodicTableGame extends CampaignGame<AppInfoService,
        PeriodicTableMainDependencyManager,
        PeriodicTableDependencyManager,
        AbstractScreen,
        PeriodicTableScreenManager,
        GameIdEnum
        > {

    public PeriodicTableGame(AppInfoService appInfoService) {
        super(appInfoService, new PeriodicTableMainDependencyManager());
    }

    public PeriodicTableDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static PeriodicTableGame getInstance() {
        return (PeriodicTableGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        PeriodicTableScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
