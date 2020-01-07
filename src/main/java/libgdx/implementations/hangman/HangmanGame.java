package libgdx.implementations.hangman;



import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
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

    public HangmanGame(AppInfoService appInfoService) {
        super(appInfoService, new HangmanGameMainDependencyManager());
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
