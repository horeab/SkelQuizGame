package libgdx.implementations.flags;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.flags.FlagsScreenManager;
import libgdx.screens.implementations.judetelerom.JudeteleRomScreenManager;

public class FlagsGame extends CampaignGame<AppInfoService,
        FlagsMainDependencyManager,
        FlagsDependencyManager,
        AbstractScreen,
        FlagsScreenManager,
        GameIdEnum
        > {

    public FlagsGame(AppInfoService appInfoService) {
        super(appInfoService, new FlagsMainDependencyManager());
    }

    public FlagsDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static FlagsGame getInstance() {
        return (FlagsGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        FlagsScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
