package libgdx.implementations.hangman;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.game.external.BillingService;
import libgdx.game.external.FacebookService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangman.HangmanScreenManager;

public class HangmanGame extends CampaignGame<AppInfoService,
        HangmanGameMainDependencyManager,
        HangmanDependencyManager,
        AbstractScreen,
        HangmanScreenManager,
        GameIdEnum
        > {

    public HangmanGame(FacebookService facebookService,
                       BillingService billingService,
                       AppInfoService appInfoService) {
        super(facebookService, billingService, appInfoService, new HangmanGameMainDependencyManager());
    }

    public HangmanDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static HangmanGame getInstance() {
        return (HangmanGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        HangmanScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
