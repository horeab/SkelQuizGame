package libgdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.constants.user.AccountCreationSource;
import libgdx.dbapi.UsersDbApiService;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.LoginService;
import libgdx.game.model.BaseUserInfo;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;
import libgdx.screen.SplashScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.InAppPurchaseManager;
import libgdx.utils.InternetUtils;
import libgdx.utils.startgame.test.DefaultPurchaseManager;

public abstract class Game<
        TAppInfoService extends AppInfoService,
        TMainDependencyManager extends MainDependencyManager,
        TSubGameDependencyManager extends SubGameDependencyManager,
        TScreen extends AbstractScreen,
        TScreenManager extends AbstractScreenManager,
        TGameId extends Enum & GameId>
        extends com.badlogic.gdx.Game {

    private static Game instance;

    private boolean firstTimeMainMenuDisplayed = true;
    private Boolean hasInternet;

    public PurchaseManager purchaseManager;
    private InAppPurchaseManager inAppPurchaseManager;
    private TAppInfoService appInfoService;
    protected LoginService loginService;

    private AssetManager assetManager;
    private TSubGameDependencyManager subGameDependencyManager;
    private TMainDependencyManager mainDependencyManager;
    protected TScreenManager screenManager;
    private FontManager fontManager;
    private UsersDbApiService usersDbApiService;

    public Game(TAppInfoService appInfoService,
                TMainDependencyManager mainDependencyManager) {
        super();
        instance = this;
        this.appInfoService = appInfoService;
        this.mainDependencyManager = mainDependencyManager;
        this.usersDbApiService = new UsersDbApiService();
        subGameDependencyManager = (TSubGameDependencyManager) ((TGameId) EnumUtils.getEnumValue(mainDependencyManager.getGameIdClass(), appInfoService.getGameIdPrefix())).getDependencyManager();
        screenManager = (TScreenManager) mainDependencyManager.createScreenManager();
    }

    @Override
    public void create() {
        screenManager.initialize(this);
        initAssetManager();
    }

    public boolean hasInternet() {
        if (hasInternet == null) {
            this.hasInternet = InternetUtils.hasInternet();
        }
        return hasInternet;
    }

    public void setNewContext(TAppInfoService newAppInfoService) {
        this.appInfoService = newAppInfoService;
        fontManager = new FontManager();
        screenManager.showMainScreen();
    }

    public BaseUserInfo getCurrentUser() {
        String externalId = getLoginService().getExternalId();
        AccountCreationSource accountCreationSource = getLoginService().getAccountCreationSource();
        return new BaseUserInfo(usersDbApiService.getUserId(externalId, accountCreationSource), externalId, accountCreationSource, getLoginService().getFullName());
    }

    public InAppPurchaseManager getInAppPurchaseManager() {
        return inAppPurchaseManager;
    }


    public void executeAfterAssetsLoaded() {
        fontManager = new FontManager();
        displayScreenAfterAssetsLoad();
        initLogin();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            this.purchaseManager = new DefaultPurchaseManager();
        }
        this.inAppPurchaseManager = new InAppPurchaseManager(getSubGameDependencyManager().getExtraContentProductId());
    }

    private void initLogin() {
        if (!getLoginService().isUserLoggedIn()) {
            getLoginService().loginClick(AccountCreationSource.INTERNAL, new Runnable() {
                @Override
                public void run() {
                    getScreenManager().showMainScreen();
                }
            });
        }
    }

    public TMainDependencyManager getMainDependencyManager() {
        return mainDependencyManager;
    }

    private void initAssetManager() {
        super.setScreen(new SplashScreen());
        assetManager = new AssetManager();
        for (Res resource : mainDependencyManager.createResourceService().getAllRes()) {
            Class<?> classType = resource.getClassType();
            if (classType != null) {
                String path = resource.getPath();
                String labelResLanguage = path.substring(path.length() - 2);
                if (classType.equals(I18NBundle.class) && !labelResLanguage.equals(appInfoService.getLanguage())) {
                    continue;
                }
                assetManager.load(path, classType);
            }
        }
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public TAppInfoService getAppInfoService() {
        return appInfoService;
    }

    public String getGameId() {
        return getGameIdPrefix() + "_" + getAppInfoService().getLanguage();
    }

    public String getGameIdPrefix() {
        return getAppInfoService().getGameIdPrefix();
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public static Game getInstance() {
        return instance;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public boolean isFirstTimeMainMenuDisplayed() {
        return firstTimeMainMenuDisplayed;
    }

    public void setFirstTimeMainMenuDisplayed(boolean firstTimeMainMenuDisplayed) {
        this.firstTimeMainMenuDisplayed = firstTimeMainMenuDisplayed;
    }

    protected abstract void displayScreenAfterAssetsLoad();

    public TSubGameDependencyManager getSubGameDependencyManager() {
        return subGameDependencyManager;
    }

    public TScreenManager getScreenManager() {
        return screenManager;
    }

    public TScreen getAbstractScreen() {
        return (TScreen) getScreen();
    }

    public void setScreen(AbstractScreen screen) {
        super.setScreen(screen);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.clear();
    }
}
