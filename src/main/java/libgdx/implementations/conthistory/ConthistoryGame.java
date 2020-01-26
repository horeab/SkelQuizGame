package libgdx.implementations.conthistory;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.conthistory.ConthistoryScreenManager;
import libgdx.screens.implementations.judetelerom.JudeteleRomScreenManager;

public class ConthistoryGame extends CampaignGame<AppInfoService,
        ConthistoryMainDependencyManager,
        ConthistoryDependencyManager,
        AbstractScreen,
        ConthistoryScreenManager,
        GameIdEnum
        > {

    public ConthistoryGame(AppInfoService appInfoService) {
        super(appInfoService, new ConthistoryMainDependencyManager());
    }

    public ConthistoryDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static ConthistoryGame getInstance() {
        return (ConthistoryGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        ConthistoryScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
