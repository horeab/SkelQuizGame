package libgdx.implementations.kennstde;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.kennstde.KennstDeScreenManager;

public class KennstDeGame extends CampaignGame<AppInfoService,
        KennstDeMainDependencyManager,
        KennstDeDependencyManager,
        AbstractScreen,
        KennstDeScreenManager,
        GameIdEnum
        > {

    public KennstDeGame(AppInfoService appInfoService) {
        super(appInfoService, new KennstDeMainDependencyManager());
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
