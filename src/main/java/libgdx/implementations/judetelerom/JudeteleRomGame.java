package libgdx.implementations.judetelerom;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.judetelerom.JudeteleRomScreenManager;

public class JudeteleRomGame extends CampaignGame<AppInfoService,
        JudeteleRomMainDependencyManager,
        JudeteleRomDependencyManager,
        AbstractScreen,
        JudeteleRomScreenManager,
        GameIdEnum
        > {

    public JudeteleRomGame(AppInfoService appInfoService) {
        super(appInfoService, new JudeteleRomMainDependencyManager());
    }

    public JudeteleRomDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static JudeteleRomGame getInstance() {
        return (JudeteleRomGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        JudeteleRomScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
