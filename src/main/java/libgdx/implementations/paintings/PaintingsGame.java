package libgdx.implementations.paintings;


import com.badlogic.gdx.pay.PurchaseManager;

import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.painting.PaintingsScreenManager;

public class PaintingsGame extends CampaignGame<AppInfoService,
        PaintingsMainDependencyManager,
        PaintingsDependencyManager,
        AbstractScreen,
        PaintingsScreenManager,
        GameIdEnum
        > {

    public PaintingsGame(AppInfoService appInfoService) {
        super(appInfoService, new PaintingsMainDependencyManager());
    }

    public PaintingsDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static PaintingsGame getInstance() {
        return (PaintingsGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        PaintingsScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
