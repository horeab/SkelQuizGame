package libgdx.implementations.anatomy;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
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

    public AnatomyGame(AppInfoService appInfoService) {
        super(appInfoService, new AnatomyMainDependencyManager());
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
