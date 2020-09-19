package libgdx.implementations.history;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.screens.implementations.history.HistoryScreenManager;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class HistoryGame extends CampaignGame<AppInfoService,
        HistoryMainDependencyManager,
        HistoryDependencyManager,
        AbstractScreen,
        HistoryScreenManager,
        GameIdEnum
        > {

    public HistoryGame(AppInfoService appInfoService) {
        super(appInfoService, new HistoryMainDependencyManager());
    }

    public HistoryDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static HistoryGame getInstance() {
        return (HistoryGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        HistoryScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
