package libgdx.implementations.judetelerom;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;

public class JudeteleRomGame extends CampaignGame<AppInfoService,
        JudeteleRomMainDependencyManager,
        JudeteleRomDependencyManager,
        AbstractScreen,
        QuizScreenManager,
        GameIdEnum
        > {

    public JudeteleRomGame(FacebookService facebookService,
                           BillingService billingService,
                           AppInfoService appInfoService) {
        super(facebookService, billingService, appInfoService, new JudeteleRomMainDependencyManager());
    }

    public JudeteleRomDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static JudeteleRomGame getInstance() {
        return (JudeteleRomGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        QuizScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}