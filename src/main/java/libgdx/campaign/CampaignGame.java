package libgdx.campaign;


import libgdx.dbapi.UniqueDbOperationContainer;
import libgdx.game.Game;
import libgdx.game.GameId;
import libgdx.game.MainDependencyManager;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.LoginService;
import libgdx.login.GuestUserLoginService;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public abstract class CampaignGame<
        TAppInfoService extends AppInfoService,
        TMainDependencyManager extends MainDependencyManager,
        TSubGameDependencyManager extends CampaignGameDependencyManager,
        TScreen extends AbstractScreen,
        TScreenManager extends AbstractScreenManager,
        TGameId extends Enum & GameId> extends Game<TAppInfoService, TMainDependencyManager, TSubGameDependencyManager, TScreen, TScreenManager, TGameId> {

    public CampaignGame(TAppInfoService appInfoService,
                        TMainDependencyManager mainDependencyManager) {
        super(appInfoService, mainDependencyManager);
    }

    @Override
    public void create() {
        super.create();
        loginService = createLoginService();
        UniqueDbOperationContainer.getInstance().clear();
    }

    protected LoginService createLoginService() {
        return new GuestUserLoginService();
    }

    @Override
    public TSubGameDependencyManager getSubGameDependencyManager() {
        return super.getSubGameDependencyManager();
    }

    public static CampaignGame getInstance() {
        return (CampaignGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        TScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
