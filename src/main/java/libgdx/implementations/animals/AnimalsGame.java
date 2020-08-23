package libgdx.implementations.animals;


import libgdx.campaign.CampaignGame;
import libgdx.game.Game;
import libgdx.game.external.AppInfoService;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.animals.AnimalsScreenManager;

public class AnimalsGame extends CampaignGame<AppInfoService,
        AnimalsGameMainDependencyManager,
        AnimalsDependencyManager,
        AbstractScreen,
        AnimalsScreenManager,
        GameIdEnum
        > {

    public AnimalsGame(AppInfoService appInfoService) {
        super(appInfoService, new AnimalsGameMainDependencyManager());
    }

    public AnimalsDependencyManager getDependencyManager() {
        return getSubGameDependencyManager();
    }

    public static AnimalsGame getInstance() {
        return (AnimalsGame) Game.getInstance();
    }

    @Override
    protected void displayScreenAfterAssetsLoad() {
        AnimalsScreenManager screenManager = getScreenManager();
        screenManager.showMainScreen();
    }
}
