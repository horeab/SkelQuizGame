package libgdx.implementations.astronomy;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.astronomy.AstronomyScreenManager;

public class AstronomyGame extends CampaignGame<AppInfoService,
        AstronomyMainDependencyManager,
        AstronomyDependencyManager,
        AbstractScreen,
        AstronomyScreenManager,
        GameIdEnum
        > {

    public AstronomyGame(AppInfoService appInfoService) {
        super(appInfoService, new AstronomyMainDependencyManager());
    }

    public AstronomyDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static AstronomyGame getInstance() {
        return (AstronomyGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        AstronomyScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
