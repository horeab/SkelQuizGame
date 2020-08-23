package libgdx.implementations.hangmanarena;



import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.hangmanarena.HangmanArenaScreenManager;

public class HangmanArenaGame extends CampaignGame<AppInfoService,
        HangmanArenaGameMainDependencyManager,
        HangmanArenaDependencyManager,
        AbstractScreen,
        HangmanArenaScreenManager,
        GameIdEnum
        > {

    public HangmanArenaGame(AppInfoService appInfoService) {
        super(appInfoService, new HangmanArenaGameMainDependencyManager());
    }

    public HangmanArenaDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static HangmanArenaGame getInstance() {
        return (HangmanArenaGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        HangmanArenaScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
