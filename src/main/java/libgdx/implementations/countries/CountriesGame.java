package libgdx.implementations.countries;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.screens.implementations.countries.CountriesScreenManager;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;

public class CountriesGame extends CampaignGame<AppInfoService,
        CountriesMainDependencyManager,
        CountriesDependencyManager,
        AbstractScreen,
        CountriesScreenManager,
        GameIdEnum
        > {

    public CountriesGame(AppInfoService appInfoService) {
        super(appInfoService, new CountriesMainDependencyManager());
    }

    public CountriesDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static CountriesGame getInstance() {
        return (CountriesGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        CountriesScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
